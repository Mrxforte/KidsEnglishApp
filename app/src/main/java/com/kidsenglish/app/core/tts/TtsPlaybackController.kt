package com.kidsenglish.app.core.tts

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Compose state controller for speech pronunciation, supporting repeat and slow speech modes.
 */
class TtsPlaybackController(context: Context) {

    private val repository = TtsRepository(context)

    private val _playbackState = MutableStateFlow<TtsPlaybackState>(TtsPlaybackState.Idle)
    val playbackState: StateFlow<TtsPlaybackState> = _playbackState.asStateFlow()

    private var lastRequest: TtsRequest? = null

    init {
        repository.initialize { isReady ->
            if (!isReady) {
                _playbackState.value = TtsPlaybackState.Error("TTS initialization pending")
            }
        }
    }

    fun speakWord(word: String, isSlow: Boolean = false) {
        speak(
            TtsRequest(
                text = word,
                isSlow = isSlow,
                voiceProfile = if (isSlow) VoiceProfile.CLEAR_PHONICS else VoiceProfile.TUTOR_FRIENDLY
            )
        )
    }

    fun speakSentence(sentence: String) {
        speak(
            TtsRequest(
                text = sentence,
                isSlow = false,
                voiceProfile = VoiceProfile.TUTOR_FRIENDLY
            )
        )
    }

    fun repeatLast() {
        lastRequest?.let { speak(it) }
    }

    fun stop() {
        repository.stop()
        _playbackState.value = TtsPlaybackState.Idle
    }

    private fun speak(request: TtsRequest) {
        lastRequest = request
        _playbackState.value = TtsPlaybackState.Loading(request.text)

        repository.speak(
            request = request,
            onStart = {
                _playbackState.value = TtsPlaybackState.Playing(request.text, request.isSlow)
            },
            onDone = {
                _playbackState.value = TtsPlaybackState.Idle
            },
            onError = { err ->
                _playbackState.value = TtsPlaybackState.Error(err)
            }
        )
    }

    fun release() {
        repository.release()
    }
}

@Composable
fun rememberTtsPlaybackController(): TtsPlaybackController {
    val context = LocalContext.current
    val controller = remember { TtsPlaybackController(context) }

    DisposableEffect(Unit) {
        onDispose {
            controller.release()
        }
    }
    return controller
}
