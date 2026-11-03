package com.kidsenglish.app.domain.repository

import com.kidsenglish.app.domain.model.AppStartupConfig
import com.kidsenglish.app.domain.model.RoadmapHorizontalSection
import com.kidsenglish.app.domain.model.RoadmapTopicCategory
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository interface for managing application startup initialization,
 * persisted sticker progress, and the Horizontal Levels Roadmap template data.
 */
interface AppStartupRepository {

    /**
     * Observes the current [AppStartupConfig].
     */
    fun observeStartupConfig(): Flow<AppStartupConfig>

    /**
     * Records an application launch and initializes core preferences.
     */
    suspend fun recordAppLaunch(): AppStartupConfig

    /**
     * Records completion of an activity card and awards a daily sticker.
     */
    suspend fun completeActivityAndEarnSticker(activityId: String)

    /**
     * Updates sound effects and background music preferences.
     */
    suspend fun updateAudioPreferences(
        soundEffectsEnabled: Boolean,
        backgroundMusicEnabled: Boolean
    )

    /**
     * Returns the 18 Topic Categories for the 3x6 Horizontal Grid Template.
     */
    fun getTopicCategories(completedIds: Set<String>): List<RoadmapTopicCategory>

    /**
     * Returns the Horizontal Roadmap Sections (FREE starter games, ABC, COLOURS, NUMBERS, etc.).
     */
    fun getHorizontalSections(completedIds: Set<String>): List<RoadmapHorizontalSection>
}
