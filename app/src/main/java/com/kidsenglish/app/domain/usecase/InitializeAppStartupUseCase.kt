package com.kidsenglish.app.domain.usecase

import com.kidsenglish.app.domain.model.AppStartupConfig
import com.kidsenglish.app.domain.repository.AppStartupRepository
import javax.inject.Inject

/**
 * Use case encapsulating the business logic for initializing the application
 * during the Splash Screen startup sequence.
 */
class InitializeAppStartupUseCase @Inject constructor(
    private val appStartupRepository: AppStartupRepository
) {
    /**
     * Executes the startup initialization and returns the resulting [AppStartupConfig].
     */
    suspend operator fun invoke(): Result<AppStartupConfig> {
        return runCatching {
            appStartupRepository.recordAppLaunch()
        }
    }
}
