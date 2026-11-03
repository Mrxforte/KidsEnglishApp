package com.kidsenglish.app.feature.roadmap.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout. widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.icons.AppIcons
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.RoadmapActivityCard
import com.kidsenglish.app.domain.model.RoadmapTopicCategory
import com.kidsenglish.app.feature.roadmap.presentation.components.ActivitySceneIllustration
import com.kidsenglish.app.feature.roadmap.presentation.components.AlphabetWatermarkBackground
import com.kidsenglish.app.feature.roadmap.presentation.components.HorizontalSectionsView
import com.kidsenglish.app.feature.roadmap.presentation.components.RoadmapTopHudBar
import com.kidsenglish.app.feature.roadmap.presentation.components.TopicsGridView
import kotlinx.coroutines.delay

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.kidsenglish.app.core.utils.KidsAudioManager

/**
 * Stateful route composable connecting [RoadmapViewModel] and [KidsAudioManager]
 * to the stateless [RoadmapScreen].
 */
@Composable
fun RoadmapRoute(
    modifier: Modifier = Modifier,
    viewModel: RoadmapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val audioManager = remember { KidsAudioManager() }

    LaunchedEffect(uiState.isBackgroundMusicEnabled) {
        audioManager.setBackgroundMusicEnabled(uiState.isBackgroundMusicEnabled)
    }

    DisposableEffect(Unit) {
        onDispose {
            audioManager.stopBackgroundMusic()
        }
    }

    RoadmapScreen(
        uiState = uiState,
        onToggleGridMode = {
            audioManager.playCardTapChime(uiState.isSoundEffectsEnabled)
            viewModel.toggleTopicsGridMode()
        },
        onTopicCategorySelected = { category ->
            audioManager.playCardTapChime(uiState.isSoundEffectsEnabled)
            viewModel.onTopicCategorySelected(category)
        },
        onSectionScrollConsumed = viewModel::onSectionScrollConsumed,
        onActivityCardClicked = { card ->
            if (card.isLocked) {
                audioManager.playLockedCardBoing(uiState.isSoundEffectsEnabled)
            } else {
                audioManager.playCardTapChime(uiState.isSoundEffectsEnabled)
            }
            viewModel.onActivityCardClicked(card)
        },
        onCompleteSelectedActivity = {
            audioManager.playStickerRewardFanfare(uiState.isSoundEffectsEnabled)
            viewModel.completeSelectedActivity()
        },
        onDismissActivityModal = viewModel::dismissActivityModal,
        onOpenSettings = {
            audioManager.playCardTapChime(uiState.isSoundEffectsEnabled)
            viewModel.toggleSettingsDialog(true)
        },
        onDismissSettings = { viewModel.toggleSettingsDialog(false) },
        onUpdateAudioPreferences = viewModel::updateAudioPreferences,
        onClearFeedbackMessage = viewModel::clearFeedbackMessage,
        modifier = modifier
    )
}

/**
 * Stateless Horizontal Levels Roadmap Template screen matching the 5 reference screenshots.
 *
 * Features:
 * - Full-screen alphabet-watermarked blue-to-purple gradient background.
 * - Top Horizontal HUD Bar with 3D Hexagonal All-Topics Grid toggle, Settings button,
 *   Purple Hexagonal Puzzle Piece "Stickers today: X/6" ribbon, cartoon monster buddies,
 *   and top-right Crimson mode ribbon.
 * - Seamless crossfade between the **3x6 All-Topics Tilted Card Grid** (Screenshot 1)
 *   and the **Horizontal Scrolling Topic & Activity Cards Roadmap** (Screenshots 2–5).
 */
@Composable
fun RoadmapScreen(
    uiState: RoadmapUiState,
    onToggleGridMode: () -> Unit,
    onTopicCategorySelected: (RoadmapTopicCategory) -> Unit,
    onSectionScrollConsumed: () -> Unit,
    onActivityCardClicked: (RoadmapActivityCard) -> Unit,
    onCompleteSelectedActivity: () -> Unit,
    onDismissActivityModal: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismissSettings: () -> Unit,
    onUpdateAudioPreferences: (Boolean, Boolean) -> Unit,
    onClearFeedbackMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(uiState.transientFeedbackMessageRes) {
        if (uiState.transientFeedbackMessageRes != null) {
            delay(2200L)
            onClearFeedbackMessage()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(AppConstants.TestTags.ROADMAP_SCREEN_ROOT)
    ) {
        // Layer 1: Scattered alphabet watermark background
        AlphabetWatermarkBackground()

        // Layer 2: Top HUD Bar + Main Horizontal Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            RoadmapTopHudBar(
                isTopicsGridMode = uiState.isTopicsGridMode,
                stickersEarnedToday = uiState.stickersEarnedToday,
                dailyStickerTarget = uiState.dailyStickerTarget,
                onToggleGridMode = onToggleGridMode,
                onOpenSettings = onOpenSettings
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Crossfade(
                    targetState = uiState.isTopicsGridMode,
                    label = "roadmap_view_mode_crossfade"
                ) { isGridMode ->
                    if (isGridMode) {
                        TopicsGridView(
                            categories = uiState.topicCategories,
                            onCategoryClick = onTopicCategorySelected
                        )
                    } else {
                        HorizontalSectionsView(
                            sections = uiState.horizontalSections,
                            scrollToSectionIndex = uiState.scrollToSectionIndex,
                            onSectionScrollConsumed = onSectionScrollConsumed,
                            onActivityCardClick = onActivityCardClicked
                        )
                    }
                }

                // Floating feedback pill banner when earning a sticker or tapping a locked card
                val feedbackRes = uiState.transientFeedbackMessageRes
                if (feedbackRes != null) {
                    Surface(
                        shape = RoundedCornerShape(AppDimensions.RadiusPill),
                        color = AppColors.SplashBadgeBackground,
                        shadowElevation = AppDimensions.ElevationMedium,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = AppDimensions.SpaceS)
                    ) {
                        Text(
                            text = stringResource(id = feedbackRes),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.SplashBadgeText,
                            modifier = Modifier.padding(
                                horizontal = AppDimensions.SpaceL,
                                vertical = AppDimensions.SpaceXS
                            )
                        )
                    }
                }
            }
        }

        // Interactive Activity Preview & Sticker Reward Modal
        val activeCard = uiState.selectedActivity
        if (activeCard != null) {
            ActivityPlayModal(
                card = activeCard,
                onStartAndEarnSticker = onCompleteSelectedActivity,
                onDismiss = onDismissActivityModal
            )
        }

        // Audio Settings Modal
        if (uiState.showSettingsDialog) {
            RoadmapSettingsModal(
                isSoundEffectsEnabled = uiState.isSoundEffectsEnabled,
                isBackgroundMusicEnabled = uiState.isBackgroundMusicEnabled,
                onToggleSoundEffects = { enabled ->
                    onUpdateAudioPreferences(enabled, uiState.isBackgroundMusicEnabled)
                },
                onToggleBackgroundMusic = { enabled ->
                    onUpdateAudioPreferences(uiState.isSoundEffectsEnabled, enabled)
                },
                onDismiss = onDismissSettings
            )
        }
    }
}

/**
 * Child-friendly modal dialog launched when tapping any unlocked Activity Card on the Horizontal Roadmap.
 */
@Composable
private fun ActivityPlayModal(
    card: RoadmapActivityCard,
    onStartAndEarnSticker: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(AppDimensions.RadiusLarge),
            color = AppColors.Surface,
            shadowElevation = AppDimensions.ElevationHigh,
            modifier = Modifier.widthIn(max = AppDimensions.ModalMaxWidth)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(AppDimensions.SpaceL)
            ) {
                Text(
                    text = stringResource(id = card.titleRes),
                    style = MaterialTheme.typography.headlineLarge,
                    color = AppColors.TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppDimensions.SpaceM))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimensions.ModalPreviewCardHeight)
                        .clip(RoundedCornerShape(AppDimensions.RadiusMedium))
                        .background(AppColors.SurfaceVariant)
                ) {
                    ActivitySceneIllustration(
                        sceneType = card.sceneType,
                        isLocked = false,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(AppDimensions.SpaceL))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.SpaceS),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(AppDimensions.ButtonHeight)
                    ) {
                        Text(text = stringResource(id = AppStrings.Roadmap.MODAL_CLOSE))
                    }

                    Button(
                        onClick = onStartAndEarnSticker,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary,
                            contentColor = AppColors.OnPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(AppDimensions.ButtonHeight)
                    ) {
                        Icon(
                            imageVector = AppIcons.PlayAction,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.SpaceXXS))
                        Text(text = stringResource(id = AppStrings.Roadmap.MODAL_START_ACTIVITY))
                    }
                }
            }
        }
    }
}

/**
 * Settings dialog launched from the top-left Grey Hexagonal Settings button.
 */
@Composable
private fun RoadmapSettingsModal(
    isSoundEffectsEnabled: Boolean,
    isBackgroundMusicEnabled: Boolean,
    onToggleSoundEffects: (Boolean) -> Unit,
    onToggleBackgroundMusic: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(AppDimensions.RadiusLarge),
            color = AppColors.Surface,
            shadowElevation = AppDimensions.ElevationHigh,
            modifier = Modifier.widthIn(max = AppDimensions.ModalMaxWidth)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(AppDimensions.SpaceL)
            ) {
                Text(
                    text = stringResource(id = AppStrings.Roadmap.SETTINGS_TITLE),
                    style = MaterialTheme.typography.headlineLarge,
                    color = AppColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(AppDimensions.SpaceM))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.SoundWave,
                            contentDescription = null,
                            tint = AppColors.Primary
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.SpaceS))
                        Text(
                            text = stringResource(id = AppStrings.Roadmap.SETTINGS_SOUND_EFFECTS),
                            style = MaterialTheme.typography.titleLarge,
                            color = AppColors.TextPrimary
                        )
                    }
                    Switch(
                        checked = isSoundEffectsEnabled,
                        onCheckedChange = onToggleSoundEffects
                    )
                }

                Spacer(modifier = Modifier.height(AppDimensions.SpaceS))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.MusicNote,
                            contentDescription = null,
                            tint = AppColors.Primary
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.SpaceS))
                        Text(
                            text = stringResource(id = AppStrings.Roadmap.SETTINGS_BACKGROUND_MUSIC),
                            style = MaterialTheme.typography.titleLarge,
                            color = AppColors.TextPrimary
                        )
                    }
                    Switch(
                        checked = isBackgroundMusicEnabled,
                        onCheckedChange = onToggleBackgroundMusic
                    )
                }

                Spacer(modifier = Modifier.height(AppDimensions.SpaceL))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimensions.ButtonHeight)
                ) {
                    Text(text = stringResource(id = AppStrings.Roadmap.MODAL_CLOSE))
                }
            }
        }
    }
}
