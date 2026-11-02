package com.kidsenglish.app.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager as AndroidAudioManager
import android.media.AudioTrack
import android.media.SoundPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Manages playback of short, tactile sound effects with SoundPool and procedural acoustic synthesis.
 */
class SoundEffectManager(private val context: Context) {

    private var soundPool: SoundPool? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(8)
            .setAudioAttributes(attributes)
            .build()
    }

    /**
     * Plays the requested sound effect adhering to current settings.
     */
    fun playEffect(effect: AudioEffect, settings: AudioSettings) {
        if (!settings.isSfxEnabled || settings.masterVolume <= 0f) return

        val effectiveVolume = settings.sfxVolume * settings.masterVolume

        // Synthesize cheerful harmonic audio cues natively
        scope.launch {
            try {
                when (effect) {
                    AudioEffect.CLICK -> playTone(frequency = 523.25f, durationMs = 40, volume = effectiveVolume) // C5
                    AudioEffect.TAP -> playTone(frequency = 659.25f, durationMs = 35, volume = effectiveVolume) // E5
                    AudioEffect.CORRECT -> {
                        // High arpeggio (C5 -> E5 -> G5)
                        playTone(523.25f, 70, effectiveVolume)
                        playTone(659.25f, 70, effectiveVolume)
                        playTone(783.99f, 150, effectiveVolume)
                    }
                    AudioEffect.WRONG -> {
                        // Gentle low double-buzz (Bb3 -> Ab3)
                        playTone(233.08f, 100, effectiveVolume * 0.7f)
                        playTone(207.65f, 150, effectiveVolume * 0.7f)
                    }
                    AudioEffect.STAR -> {
                        // Sparkly chime (G5 -> C6)
                        playTone(783.99f, 80, effectiveVolume)
                        playTone(1046.50f, 200, effectiveVolume)
                    }
                    AudioEffect.COIN -> playTone(987.77f, 90, effectiveVolume) // B5
                    AudioEffect.LEVEL_UNLOCK -> {
                        playTone(523.25f, 80, effectiveVolume)
                        playTone(659.25f, 80, effectiveVolume)
                        playTone(783.99f, 80, effectiveVolume)
                        playTone(1046.50f, 220, effectiveVolume)
                    }
                    AudioEffect.LESSON_COMPLETE -> {
                        playTone(587.33f, 90, effectiveVolume) // D5
                        playTone(783.99f, 90, effectiveVolume) // G5
                        playTone(880.00f, 90, effectiveVolume) // A5
                        playTone(1174.66f, 300, effectiveVolume) // D6
                    }
                    AudioEffect.POP -> playTone(880.00f, 35, effectiveVolume)
                    AudioEffect.CHEER -> {
                        playTone(659.25f, 100, effectiveVolume)
                        playTone(1046.50f, 250, effectiveVolume)
                    }
                }
            } catch (_: Exception) {
                // Graceful fallback
            }
        }
    }

    /**
     * Procedural sine/harmonic tone generator running on AudioTrack.
     */
    private fun playTone(frequency: Float, durationMs: Int, volume: Float) {
        val sampleRate = 22050
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            // Apply gentle cosine envelope to prevent audio clicking
            val envelope = sin(Math.PI * i / numSamples).toFloat()
            val angle = 2.0 * Math.PI * i * frequency / sampleRate
            val sample = (sin(angle) * envelope * volume * Short.MAX_VALUE).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(buffer, 0, buffer.size)
        track.play()
        track.release()
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
