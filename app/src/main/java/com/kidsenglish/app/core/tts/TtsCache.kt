package com.kidsenglish.app.core.tts

import android.content.Context
import java.io.File
import java.security.MessageDigest

/**
 * Cache for generated speech audio files indexed by normalized text, speed, and voice.
 */
class TtsCache(private val context: Context) {

    private val cacheDir = File(context.cacheDir, "tts_audio").apply {
        if (!exists()) mkdirs()
    }

    fun getCachedFile(text: String, isSlow: Boolean, voiceProfile: VoiceProfile): File? {
        val key = generateKey(text, isSlow, voiceProfile)
        val file = File(cacheDir, "$key.wav")
        return if (file.exists() && file.length() > 0) file else null
    }

    fun createCacheFile(text: String, isSlow: Boolean, voiceProfile: VoiceProfile): File {
        val key = generateKey(text, isSlow, voiceProfile)
        return File(cacheDir, "$key.wav")
    }

    fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    private fun generateKey(text: String, isSlow: Boolean, voice: VoiceProfile): String {
        val raw = "${text.trim().lowercase()}_slow=${isSlow}_voice=${voice.name}"
        val digest = MessageDigest.getInstance("MD5").digest(raw.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
