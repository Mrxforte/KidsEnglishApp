package com.kidsenglish.app.core.tts

/**
 * Request payload for educational text-to-speech synthesis.
 */
data class TtsRequest(
    val text: String,
    val isSlow: Boolean = false,
    val voiceProfile: VoiceProfile = VoiceProfile.TUTOR_FRIENDLY,
    val languageCode: String = "en-US"
)

enum class VoiceProfile {
    TUTOR_FRIENDLY,
    CLEAR_PHONICS,
    STORYTELLER
}

enum class TtsEngineType {
    AUTO,
    GEMINI_TTS,
    ANDROID_DEVICE_TTS
}

/**
 * State representing audio playback status in the UI.
 */
sealed class TtsPlaybackState {
    object Idle : TtsPlaybackState()
    data class Loading(val text: String) : TtsPlaybackState()
    data class Playing(val text: String, val isSlow: Boolean) : TtsPlaybackState()
    object Paused : TtsPlaybackState()
    data class Error(val message: String) : TtsPlaybackState()
}
