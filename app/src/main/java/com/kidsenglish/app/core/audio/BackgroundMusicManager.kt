package com.kidsenglish.app.core.audio

import android.content.Context
import android.media.MediaPlayer

/**
 * Manages looping background music, smooth volume transitions, ducking during voice speech,
 * and lifecycle awareness.
 */
class BackgroundMusicManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var isDucked: Boolean = false
    private var cachedSettings: AudioSettings = AudioSettings()

    fun updateSettings(settings: AudioSettings) {
        cachedSettings = settings
        if (!settings.isMusicEnabled || settings.masterVolume <= 0f) {
            pause()
        } else {
            val vol = calculateEffectiveVolume()
            mediaPlayer?.setVolume(vol, vol)
        }
    }

    fun play() {
        if (!cachedSettings.isMusicEnabled || cachedSettings.masterVolume <= 0f) return
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                val vol = calculateEffectiveVolume()
                player.setVolume(vol, vol)
                player.start()
            }
        }
    }

    fun pause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            }
        }
    }

    fun stop() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayer = null
    }

    /**
     * Ducks music to 25% when educational voice or TTS begins speaking.
     */
    fun duckForVoice() {
        isDucked = true
        val vol = calculateEffectiveVolume()
        mediaPlayer?.setVolume(vol, vol)
    }

    /**
     * Restores music to normal volume once voice speech completes.
     */
    fun restoreAfterVoice() {
        isDucked = false
        val vol = calculateEffectiveVolume()
        mediaPlayer?.setVolume(vol, vol)
    }

    private fun calculateEffectiveVolume(): Float {
        val base = cachedSettings.musicVolume * cachedSettings.masterVolume
        return if (isDucked) base * 0.25f else base
    }

    fun release() {
        stop()
    }
}
