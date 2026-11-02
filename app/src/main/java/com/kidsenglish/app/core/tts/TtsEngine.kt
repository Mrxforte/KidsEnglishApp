package com.kidsenglish.app.core.tts

/**
 * Common abstraction for educational text-to-speech synthesis engines.
 */
interface TtsEngine {
    val engineType: TtsEngineType

    fun initialize(onReady: (Boolean) -> Unit)
    fun speak(
        request: TtsRequest,
        onStart: () -> Unit = {},
        onDone: () -> Unit = {},
        onError: (String) -> Unit = {}
    )
    fun stop()
    fun isSpeaking(): Boolean
    fun release()
}
