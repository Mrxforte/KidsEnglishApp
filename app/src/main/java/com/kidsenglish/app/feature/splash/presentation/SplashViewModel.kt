package com.kidsenglish.app.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.AppStartupConfig
import com.kidsenglish.app.domain.usecase.InitializeAppStartupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing the state and startup initialization sequence for the Splash Screen.
 *
 * Exposes an immutable [StateFlow] of [SplashUiState] and delegates domain operations
 * to [InitializeAppStartupUseCase].
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initializeAppStartupUseCase: InitializeAppStartupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        startInitialization()
    }

    /**
     * Runs the application startup initialization sequence and updates [uiState] progressively.
     */
    private fun startInitialization() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    phase = SplashPhase.Initializing,
                    loadingProgress = 0.50f,
                    statusMessageRes = AppStrings.Splash.LOADING_MESSAGE
                )
            }

            delay(200L)

            val result = initializeAppStartupUseCase()
            val config = result.getOrDefault(
                AppStartupConfig(
                    launchCount = 1,
                    isFirstLaunch = true,
                    isInitialized = true
                )
            )

            delay(150L)

            _uiState.update { state ->
                state.copy(
                    phase = SplashPhase.Ready(config),
                    loadingProgress = 1.0f,
                    statusMessageRes = AppStrings.Splash.READY_MESSAGE
                )
            }
        }
    }
}
