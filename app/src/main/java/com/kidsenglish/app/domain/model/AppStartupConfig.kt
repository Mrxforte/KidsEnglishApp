package com.kidsenglish.app.domain.model

/**
 * Pure domain model representing the application startup configuration and user preferences.
 *
 * Contains zero Android framework dependencies.
 */
data class AppStartupConfig(
    val launchCount: Int = 1,
    val isFirstLaunch: Boolean = true,
    val isInitialized: Boolean = true,
    val stickersEarnedToday: Int = 0,
    val isSoundEffectsEnabled: Boolean = true,
    val isBackgroundMusicEnabled: Boolean = true,
    val completedActivityIds: Set<String> = emptySet()
)
