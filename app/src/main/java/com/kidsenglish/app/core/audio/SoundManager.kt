package com.kidsenglish.app.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Simple sound & music manager for the Kids English app.
 * Uses MediaPlayer for background music and SoundPool for short SFX.
 *
 * Replace the raw resource IDs with real audio files later:
 *  - res/raw/bg_music_happy.mp3
 *  - res/raw/sfx_unlock.mp3
 *  - res/raw/sfx_star.mp3
 *  - res/raw/sfx_correct.mp3
 *  - res/raw/sfx_tap.mp3
 */
class SoundManager(private val context: Context) {

    private var backgroundPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null

    // Sound IDs (loaded later when real files exist)
    private var unlockSoundId: Int = 0
    private var starSoundId: Int = 0
    private var correctSoundId: Int = 0
    private var tapSoundId: Int = 0

    var isMusicEnabled: Boolean = true
    var isSfxEnabled: Boolean = true
    var musicVolume: Float = 0.4f
    var sfxVolume: Float = 0.8f

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttributes)
            .build()

        // TODO: Load real sounds when assets are added
        // unlockSoundId = soundPool?.load(context, R.raw.sfx_unlock, 1) ?: 0
        // starSoundId   = soundPool?.load(context, R.raw.sfx_star, 1) ?: 0
        // etc.
    }

    fun playBackgroundMusic(resId: Int = 0) {
        if (!isMusicEnabled) return
        stopBackgroundMusic()

        // Placeholder – replace with real resource
        // backgroundPlayer = MediaPlayer.create(context, R.raw.bg_music_happy).apply {
        //     isLooping = true
        //     setVolume(musicVolume, musicVolume)
        //     start()
        // }
    }

    fun stopBackgroundMusic() {
        backgroundPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        backgroundPlayer = null
    }

    fun lowerMusicForVoice() {
        backgroundPlayer?.setVolume(musicVolume * 0.25f, musicVolume * 0.25f)
    }

    fun restoreMusicVolume() {
        backgroundPlayer?.setVolume(musicVolume, musicVolume)
    }

    fun playUnlockSound() {
        if (!isSfxEnabled) return
        // soundPool?.play(unlockSoundId, sfxVolume, sfxVolume, 1, 0, 1f)
        // For now just a log / future hook
    }

    fun playStarSound() {
        if (!isSfxEnabled) return
        // soundPool?.play(starSoundId, sfxVolume, sfxVolume, 1, 0, 1f)
    }

    fun playCorrectSound() {
        if (!isSfxEnabled) return
        // soundPool?.play(correctSoundId, sfxVolume, sfxVolume, 1, 0, 1f)
    }

    fun playTapSound() {
        if (!isSfxEnabled) return
        // soundPool?.play(tapSoundId, sfxVolume * 0.6f, sfxVolume * 0.6f, 1, 0, 1f)
    }

    fun release() {
        stopBackgroundMusic()
        soundPool?.release()
        soundPool = null
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
