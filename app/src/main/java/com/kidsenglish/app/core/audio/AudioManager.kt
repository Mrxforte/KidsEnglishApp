package com.kidsenglish.app.core.audio

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Unified Audio Architecture manager coordinating sound effects, background music,
 * priority ducking for voice/TTS, and user settings.
 */
class AudioManager(private val context: Context) {

    private val _settings = MutableStateFlow(AudioSettings())
    val settings: StateFlow<AudioSettings> = _settings.asStateFlow()

    val sfxManager = SoundEffectManager(context)
    val bgmManager = BackgroundMusicManager(context)

    private val audioFocusManager = AudioFocusManager(
        context = context,
        onAudioFocusLost = { canDuck ->
            if (canDuck) bgmManager.duckForVoice() else bgmManager.pause()
        },
        onAudioFocusGained = {
            bgmManager.restoreAfterVoice()
            bgmManager.play()
        }
    )

    fun updateSettings(newSettings: AudioSettings) {
        _settings.value = newSettings
        bgmManager.updateSettings(newSettings)
    }

    fun playSfx(effect: AudioEffect) {
        sfxManager.playEffect(effect, _settings.value)
    }

    fun startMusic() {
        if (audioFocusManager.requestFocus()) {
            bgmManager.play()
        }
    }

    fun pauseMusic() {
        bgmManager.pause()
    }

    fun duckForVoice() {
        bgmManager.duckForVoice()
    }

    fun restoreAfterVoice() {
        bgmManager.restoreAfterVoice()
    }

    fun release() {
        audioFocusManager.abandonFocus()
        sfxManager.release()
        bgmManager.release()
    }
}

@Composable
fun rememberAudioManager(): AudioManager {
    val context = LocalContext.current
    val manager = remember { AudioManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            manager.release()
        }
    }
    return manager
}
