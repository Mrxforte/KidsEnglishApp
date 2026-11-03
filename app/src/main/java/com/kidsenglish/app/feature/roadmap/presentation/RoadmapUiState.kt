package com.kidsenglish.app.feature.roadmap.presentation

import androidx.annotation.StringRes
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.domain.model.RoadmapActivityCard
import com.kidsenglish.app.domain.model.RoadmapHorizontalSection
import com.kidsenglish.app.domain.model.RoadmapTopicCategory

/**
 * Immutable UI state for the Horizontal (Landscape) Levels Roadmap Template.
 *
 * Supports switching between:
 * 1. The 3x6 All-Topics Tilted Card Grid (`isTopicsGridMode = true`, matching Screenshot 1)
 * 2. The Horizontal Scrolling Topic & Activity Cards Roadmap (`isTopicsGridMode = false`, matching Screenshots 2–5)
 */
data class RoadmapUiState(
    val isTopicsGridMode: Boolean = false,
    val stickersEarnedToday: Int = 0,
    val dailyStickerTarget: Int = AppConstants.DAILY_STICKER_TARGET,
    val topicCategories: List<RoadmapTopicCategory> = emptyList(),
    val horizontalSections: List<RoadmapHorizontalSection> = emptyList(),
    val scrollToSectionIndex: Int? = null,
    val selectedActivity: RoadmapActivityCard? = null,
    val showSettingsDialog: Boolean = false,
    val isSoundEffectsEnabled: Boolean = true,
    val isBackgroundMusicEnabled: Boolean = true,
    @StringRes val transientFeedbackMessageRes: Int? = null
)
