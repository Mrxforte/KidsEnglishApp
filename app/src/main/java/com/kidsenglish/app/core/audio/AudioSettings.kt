package com.kidsenglish.app.core.audio

/**
 * Persisted audio configuration for music, sound effects, and voice.
 */
data class AudioSettings(
    val isMusicEnabled: Boolean = true,
    val isSfxEnabled: Boolean = true,
    val isVoiceEnabled: Boolean = true,
    val masterVolume: Float = 1.0f,
    val musicVolume: Float = 0.45f,
    val sfxVolume: Float = 0.85f,
    val voiceVolume: Float = 1.0f,
    val isSlowVoice: Boolean = false
)
