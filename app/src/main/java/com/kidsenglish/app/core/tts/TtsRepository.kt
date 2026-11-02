package com.kidsenglish.app.core.tts

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository orchestrating Gemini TTS and Android device speech engine.
 */
class TtsRepository(context: Context) {

    private val ttsCache = TtsCache(context)
    private val androidEngine = AndroidTtsEngine(context)
    private val geminiEngine = GeminiTtsEngine(context, ttsCache, androidEngine)

    private val activeEngine: TtsEngine = geminiEngine

    fun initialize(onReady: (Boolean) -> Unit) {
        activeEngine.initialize(onReady)
    }

    fun speak(
        request: TtsRequest,
        onStart: () -> Unit = {},
        onDone: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        activeEngine.speak(request, onStart, onDone, onError)
    }

    fun stop() {
        activeEngine.stop()
    }

    fun isSpeaking(): Boolean = activeEngine.isSpeaking()

    fun release() {
        activeEngine.release()
    }
}
