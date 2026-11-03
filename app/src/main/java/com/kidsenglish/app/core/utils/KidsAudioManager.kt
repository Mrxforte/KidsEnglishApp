package com.kidsenglish.app.core.utils

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.math.sin

/**
 * Synthesizes gentle baby music-box background melodies and playful toy chime sound effects
 * using Android's native [AudioTrack] API without requiring external audio files.
 *
 * Respects user preferences (`isBackgroundMusicEnabled` and `isSoundEffectsEnabled`) and
 * cleanly releases audio resources when the screen leaves composition.
 */
class KidsAudioManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var bgMusicJob: Job? = null
    private var currentBgTrack: AudioTrack? = null

    /**
     * Starts or stops the soothing baby music-box / nursery xylophone background melody loop.
     */
    fun setBackgroundMusicEnabled(enabled: Boolean) {
        if (!enabled) {
            stopBackgroundMusic()
            return
        }
        if (bgMusicJob?.isActive == true) return

        bgMusicJob = scope.launch {
            // Gentle baby music-box melody ("Twinkle Twinkle" & cheerful nursery lullaby in C major)
            // Frequencies in Hz (C5, G5, A5, G5, F5, E5, D5, C5 + soft rest)
            val melodyNotesHz = listOf(
                523.25 to 420, // C5
                523.25 to 420, // C5
                783.99 to 420, // G5
                783.99 to 420, // G5
                880.00 to 420, // A5
                880.00 to 420, // A5
                783.99 to 720, // G5 (held)
                698.46 to 420, // F5
                698.46 to 420, // F5
                659.25 to 420, // E5
                659.25 to 420, // E5
                587.33 to 420, // D5
                587.33 to 420, // D5
                523.25 to 760, // C5 (held)
                783.99 to 420, // G5
                783.99 to 420, // G5
                698.46 to 420, // F5
                698.46 to 420, // F5
                659.25 to 420, // E5
                659.25 to 420, // E5
                587.33 to 720  // D5 (held)
            )

            while (isActive) {
                for ((freqHz, durationMs) in melodyNotesHz) {
                    if (!isActive) break
                    playMusicBoxTone(
                        frequencyHz = freqHz,
                        durationMs = durationMs,
                        volume = 0.14f,
                        trackHolder = { currentBgTrack = it }
                    )
                    delay(90L)
                }
                delay(450L)
            }
        }
    }

    /**
     * Stops the background baby music loop immediately.
     */
    fun stopBackgroundMusic() {
        bgMusicJob?.cancel()
        bgMusicJob = null
        runCatching {
            currentBgTrack?.pause()
            currentBgTrack?.flush()
            currentBgTrack?.release()
        }
        currentBgTrack = null
    }

    /**
     * Plays a cheerful 2-note baby toy chime when tapping a card or HUD button.
     */
    fun playCardTapChime(soundEffectsEnabled: Boolean) {
        if (!soundEffectsEnabled) return
        scope.launch {
            playMusicBoxTone(frequencyHz = 659.25, durationMs = 110, volume = 0.25f) // E5
            delay(30L)
            playMusicBoxTone(frequencyHz = 987.77, durationMs = 160, volume = 0.25f) // B5
        }
    }

    /**
     * Plays a happy 4-note baby celebration fanfare when earning a sticker.
     */
    fun playStickerRewardFanfare(soundEffectsEnabled: Boolean) {
        if (!soundEffectsEnabled) return
        scope.launch {
            val arpeggio = listOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6
            for (freq in arpeggio) {
                playMusicBoxTone(frequencyHz = freq, durationMs = 130, volume = 0.28f)
                delay(25L)
            }
        }
    }

    /**
     * Plays a soft playful boing tone when tapping a locked activity card.
     */
    fun playLockedCardBoing(soundEffectsEnabled: Boolean) {
        if (!soundEffectsEnabled) return
        scope.launch {
            playMusicBoxTone(frequencyHz = 392.00, durationMs = 110, volume = 0.22f) // G4
            delay(25L)
            playMusicBoxTone(frequencyHz = 329.63, durationMs = 160, volume = 0.22f) // E4
        }
    }

    private suspend fun playMusicBoxTone(
        frequencyHz: Double,
        durationMs: Int,
        volume: Float,
        trackHolder: ((AudioTrack?) -> Unit)? = null
    ) {
        runCatching {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
            val pcmBuffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val timeSec = i.toDouble() / sampleRate
                // Soft attack (first 12ms) + gentle exponential music-box bell decay
                val attack = (i / (sampleRate * 0.012)).coerceAtMost(1.0)
                val decay = exp(-3.8 * (i.toDouble() / numSamples))
                val envelope = attack * decay

                // Celesta / baby music-box timbre: fundamental + soft 2nd & 3rd harmonics
                val wave = sin(2.0 * Math.PI * frequencyHz * timeSec) +
                    0.28 * sin(4.0 * Math.PI * frequencyHz * timeSec) +
                    0.09 * sin(6.0 * Math.PI * frequencyHz * timeSec)

                val normalized = (wave / 1.37) * envelope * volume
                pcmBuffer[i] = (normalized * Short.MAX_VALUE).toInt()
                    .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    .toShort()
            }

            val byteSize = numSamples * 2
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(byteSize.coerceAtLeast(AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )))
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            if (audioTrack.state == AudioTrack.STATE_INITIALIZED) {
                trackHolder?.invoke(audioTrack)
                audioTrack.write(pcmBuffer, 0, numSamples)
                audioTrack.play()
                delay(durationMs.toLong())
                audioTrack.stop()
            }
            audioTrack.release()
            trackHolder?.invoke(null)
        }
    }
}
