package com.kidsenglish.app.core.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import java.util.UUID

/**
 * On-device Android TextToSpeech engine providing instant, reliable, zero-latency pronunciation.
 */
class AndroidTtsEngine(private val context: Context) : TtsEngine, TextToSpeech.OnInitListener {

    override val engineType: TtsEngineType = TtsEngineType.ANDROID_DEVICE_TTS

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var initCallback: ((Boolean) -> Unit)? = null

    private var activeStartCallback: (() -> Unit)? = null
    private var activeDoneCallback: (() -> Unit)? = null
    private var activeErrorCallback: ((String) -> Unit)? = null

    override fun initialize(onReady: (Boolean) -> Unit) {
        initCallback = onReady
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            isInitialized = (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED)
            if (isInitialized) {
                tts?.setPitch(1.15f) // Warm, friendly higher pitch for kids
                setupUtteranceListener()
            }
            initCallback?.invoke(isInitialized)
        } else {
            isInitialized = false
            initCallback?.invoke(false)
        }
    }

    private fun setupUtteranceListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                activeStartCallback?.invoke()
            }

            override fun onDone(utteranceId: String?) {
                activeDoneCallback?.invoke()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                activeErrorCallback?.invoke("Playback error")
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                activeErrorCallback?.invoke("TTS error code: $errorCode")
            }
        })
    }

    override fun speak(
        request: TtsRequest,
        onStart: () -> Unit,
        onDone: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isInitialized || tts == null) {
            onError("TTS engine is not ready yet")
            return
        }

        activeStartCallback = onStart
        activeDoneCallback = onDone
        activeErrorCallback = onError

        // Adjust rate: 0.65 for slow phonics, 0.95 for natural educational pacing
        val speechRate = if (request.isSlow) 0.65f else 0.95f
        tts?.setSpeechRate(speechRate)

        val utteranceId = UUID.randomUUID().toString()
        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
        }

        tts?.speak(request.text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    override fun stop() {
        tts?.stop()
    }

    override fun isSpeaking(): Boolean {
        return tts?.isSpeaking == true
    }

    override fun release() {
        stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
