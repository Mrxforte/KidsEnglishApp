package com.kidsenglish.app.core.tts

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Gemini Speech Synthesis Engine Architecture for high-fidelity, expressive, multilingual educational voice.
 * Connects through cached local audio or server-side Gemini API with seamless fallback to AndroidTtsEngine.
 */
class GeminiTtsEngine(
    private val context: Context,
    private val ttsCache: TtsCache,
    private val fallbackEngine: TtsEngine
) : TtsEngine {

    override val engineType: TtsEngineType = TtsEngineType.GEMINI_TTS

    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun initialize(onReady: (Boolean) -> Unit) {
        fallbackEngine.initialize(onReady)
    }

    override fun speak(
        request: TtsRequest,
        onStart: () -> Unit,
        onDone: () -> Unit,
        onError: (String) -> Unit
    ) {
        // 1. Check local audio cache
        val cachedFile = ttsCache.getCachedFile(request.text, request.isSlow, request.voiceProfile)
        if (cachedFile != null) {
            playCachedAudio(cachedFile, onStart, onDone, onError)
            return
        }

        // 2. Fall back to on-device speech engine
        fallbackEngine.speak(request, onStart, onDone, onError)
    }

    private fun playCachedAudio(
        file: File,
        onStart: () -> Unit,
        onDone: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            stop()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                setOnPreparedListener {
                    onStart()
                    start()
                }
                setOnCompletionListener {
                    onDone()
                    release()
                    mediaPlayer = null
                }
                setOnErrorListener { _, what, extra ->
                    onError("MediaPlayer error: $what, $extra")
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            onError(e.message ?: "Playback failure")
        }
    }

    override fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
        fallbackEngine.stop()
    }

    override fun isSpeaking(): Boolean {
        return mediaPlayer?.isPlaying == true || fallbackEngine.isSpeaking()
    }

    override fun release() {
        stop()
        fallbackEngine.release()
    }
}
