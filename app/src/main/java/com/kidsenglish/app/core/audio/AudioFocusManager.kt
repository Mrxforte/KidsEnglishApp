package com.kidsenglish.app.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager as AndroidAudioManager
import android.os.Build

/**
 * Handles Android system audio focus requests, ducking, and pauses when notifications or phone calls occur.
 */
class AudioFocusManager(
    private val context: Context,
    private val onAudioFocusLost: (canDuck: Boolean) -> Unit,
    private val onAudioFocusGained: () -> Unit
) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AndroidAudioManager
    private var focusRequest: AudioFocusRequest? = null

    fun requestFocus(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            val request = AudioFocusRequest.Builder(AndroidAudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { focusChange ->
                    handleFocusChange(focusChange)
                }
                .build()

            focusRequest = request
            audioManager.requestAudioFocus(request) == AndroidAudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                { handleFocusChange(it) },
                AndroidAudioManager.STREAM_MUSIC,
                AndroidAudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            ) == AndroidAudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    private fun handleFocusChange(focusChange: Int) {
        when (focusChange) {
            AndroidAudioManager.AUDIOFOCUS_LOSS -> onAudioFocusLost(false)
            AndroidAudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> onAudioFocusLost(false)
            AndroidAudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> onAudioFocusLost(true)
            AndroidAudioManager.AUDIOFOCUS_GAIN -> onAudioFocusGained()
        }
    }

    fun abandonFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus { }
        }
    }
}
