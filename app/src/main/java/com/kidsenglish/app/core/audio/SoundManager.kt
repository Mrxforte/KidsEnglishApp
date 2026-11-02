package com.kidsenglish.app.core.audio

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Backwards-compatible facade that delegates directly to the centralized AudioManager and SoundEffectManager.
 */
class SoundManager(context: Context) {

    private val audioManager = AudioManager(context)

    var isMusicEnabled: Boolean
        get() = audioManager.settings.value.isMusicEnabled
        set(value) {
            audioManager.updateSettings(audioManager.settings.value.copy(isMusicEnabled = value))
        }

    var isSfxEnabled: Boolean
        get() = audioManager.settings.value.isSfxEnabled
        set(value) {
            audioManager.updateSettings(audioManager.settings.value.copy(isSfxEnabled = value))
        }

    var musicVolume: Float
        get() = audioManager.settings.value.musicVolume
        set(value) {
            audioManager.updateSettings(audioManager.settings.value.copy(musicVolume = value))
        }

    var sfxVolume: Float
        get() = audioManager.settings.value.sfxVolume
        set(value) {
            audioManager.updateSettings(audioManager.settings.value.copy(sfxVolume = value))
        }

    fun playBackgroundMusic(resId: Int = 0) {
        audioManager.startMusic()
    }

    fun stopBackgroundMusic() {
        audioManager.pauseMusic()
    }

    fun lowerMusicForVoice() {
        audioManager.duckForVoice()
    }

    fun restoreMusicVolume() {
        audioManager.restoreAfterVoice()
    }

    fun playUnlockSound() {
        audioManager.playSfx(AudioEffect.LEVEL_UNLOCK)
    }

    fun playStarSound() {
        audioManager.playSfx(AudioEffect.STAR)
    }

    fun playCorrectSound() {
        audioManager.playSfx(AudioEffect.CORRECT)
    }

    fun playTapSound() {
        audioManager.playSfx(AudioEffect.TAP)
    }

    fun release() {
        audioManager.release()
    }
}

/**
 * Remember a SoundManager across recompositions and clean it up.
 */
@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    val manager = remember { SoundManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            manager.release()
        }
    }
    return manager
}
