package com.kidsenglish.app.feature.roadmap.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.RoadmapActivityCard
import com.kidsenglish.app.domain.model.RoadmapTopicCategory
import com.kidsenglish.app.domain.repository.AppStartupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing state and user interactions for the Horizontal Levels Roadmap Template.
 */
@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val appStartupRepository: AppStartupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RoadmapUiState(
            topicCategories = appStartupRepository.getTopicCategories(emptySet()),
            horizontalSections = appStartupRepository.getHorizontalSections(emptySet())
        )
    )
    val uiState: StateFlow<RoadmapUiState> = _uiState.asStateFlow()

    init {
        observeProgress()
    }

    private fun observeProgress() {
        viewModelScope.launch {
            appStartupRepository.observeStartupConfig().collect { config ->
                _uiState.update { current ->
                    current.copy(
                        stickersEarnedToday = config.stickersEarnedToday,
                        isSoundEffectsEnabled = config.isSoundEffectsEnabled,
                        isBackgroundMusicEnabled = config.isBackgroundMusicEnabled,
                        topicCategories = appStartupRepository.getTopicCategories(config.completedActivityIds),
                        horizontalSections = appStartupRepository.getHorizontalSections(config.completedActivityIds)
                    )
                }
            }
        }
    }

    /**
     * Toggles between the 3x6 All-Topics Tilted Card Grid (Screenshot 1)
     * and the Horizontal Scrolling Sections Roadmap (Screenshots 2–5).
     */
    fun toggleTopicsGridMode() {
        _uiState.update { current ->
            current.copy(
                isTopicsGridMode = !current.isTopicsGridMode,
                transientFeedbackMessageRes = null
            )
        }
    }

    /**
     * Handles tapping one of the 18 Topic Category cards in the 3x6 grid:
     * switches to the Horizontal Roadmap view and scrolls to the corresponding section.
     */
    fun onTopicCategorySelected(category: RoadmapTopicCategory) {
        _uiState.update { current ->
            current.copy(
                isTopicsGridMode = false,
                scrollToSectionIndex = category.targetSectionIndex,
                transientFeedbackMessageRes = null
            )
        }
    }

    /**
     * Clears the pending section scroll request once the LazyRow has scrolled.
     */
    fun onSectionScrollConsumed() {
        _uiState.update { current ->
            current.copy(scrollToSectionIndex = null)
        }
    }

    /**
     * Handles tapping an illustrated Activity Card on the Horizontal Roadmap.
     * Locked cards show a friendly prompt; unlocked cards open the interactive activity preview modal.
     */
    fun onActivityCardClicked(card: RoadmapActivityCard) {
        if (card.isLocked) {
            _uiState.update { current ->
                current.copy(transientFeedbackMessageRes = AppStrings.Roadmap.LOCKED_MESSAGE)
            }
        } else {
            _uiState.update { current ->
                current.copy(
                    selectedActivity = card,
                    transientFeedbackMessageRes = null
                )
            }
        }
    }

    /**
     * Completes the currently selected activity and awards a daily puzzle sticker.
     */
    fun completeSelectedActivity() {
        val activeCard = _uiState.value.selectedActivity ?: return
        viewModelScope.launch {
            appStartupRepository.completeActivityAndEarnSticker(activeCard.id)
            _uiState.update { current ->
                current.copy(
                    selectedActivity = null,
                    transientFeedbackMessageRes = AppStrings.Roadmap.STICKER_EARNED_MESSAGE
                )
            }
        }
    }

    fun dismissActivityModal() {
        _uiState.update { current ->
            current.copy(selectedActivity = null)
        }
    }

    fun toggleSettingsDialog(show: Boolean) {
        _uiState.update { current ->
            current.copy(showSettingsDialog = show)
        }
    }

    fun updateAudioPreferences(soundEffects: Boolean, backgroundMusic: Boolean) {
        viewModelScope.launch {
            appStartupRepository.updateAudioPreferences(
                soundEffectsEnabled = soundEffects,
                backgroundMusicEnabled = backgroundMusic
            )
        }
    }

    fun clearFeedbackMessage() {
        _uiState.update { current ->
            current.copy(transientFeedbackMessageRes = null)
        }
    }
}
