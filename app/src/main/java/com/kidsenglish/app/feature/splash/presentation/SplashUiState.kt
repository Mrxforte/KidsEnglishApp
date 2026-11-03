package com.kidsenglish.app.feature.splash.presentation

import androidx.annotation.StringRes
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.AppStartupConfig

/**
 * Sealed interface representing the startup lifecycle phase of the Splash Screen.
 */
sealed interface SplashPhase {
    /**
     * Application is actively initializing core services and preferences.
     */
    data object Initializing : SplashPhase

    /**
     * Startup initialization has completed and the application is ready.
     */
    data class Ready(val config: AppStartupConfig) : SplashPhase
}

/**
 * Immutable UI state model driving the [SplashScreen].
 */
data class SplashUiState(
    val phase: SplashPhase = SplashPhase.Initializing,
    val loadingProgress: Float = 0.25f,
    @StringRes val statusMessageRes: Int = AppStrings.Splash.LOADING_MESSAGE
) {
    /**
     * Indicates whether startup initialization has completed.
     */
    val isReady: Boolean
        get() = phase is SplashPhase.Ready
}
