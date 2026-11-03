package com.kidsenglish.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.theme.AppTheme
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.RoadmapTopicCategory
import com.kidsenglish.app.domain.model.TopicColorStyle
import com.kidsenglish.app.domain.model.TopicIconType
import com.kidsenglish.app.feature.roadmap.presentation.RoadmapScreen
import com.kidsenglish.app.feature.roadmap.presentation.RoadmapUiState
import com.kidsenglish.app.feature.splash.presentation.SplashScreen
import com.kidsenglish.app.feature.splash.presentation.SplashUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class SplashScreenRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_rendersBrandElementsAndCentralizedResources() {
        composeTestRule.setContent {
            AppTheme {
                SplashScreen(
                    uiState = SplashUiState(
                        loadingProgress = 0.65f,
                        statusMessageRes = AppStrings.Splash.LOADING_MESSAGE
                    )
                )
            }
        }

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.SPLASH_SCREEN_ROOT, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.SPLASH_BADGE, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.SPLASH_MASCOT, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.SPLASH_TITLE, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.SPLASH_SUBTITLE, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.SPLASH_LOADING_INDICATOR, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun roadmapScreen_rendersHudAndTopicsGrid() {
        var gridToggleClicked = false

        composeTestRule.setContent {
            AppTheme {
                RoadmapScreen(
                    uiState = RoadmapUiState(
                        isTopicsGridMode = true,
                        stickersEarnedToday = 2,
                        topicCategories = listOf(
                            RoadmapTopicCategory(
                                id = "abc",
                                titleRes = AppStrings.Roadmap.TOPIC_ABC,
                                iconType = TopicIconType.ABC_LETTERS,
                                colorStyle = TopicColorStyle.PURPLE,
                                wordsLearnedProgress = 0.4f,
                                targetSectionIndex = 0,
                                tiltDegrees = -1.5f
                            )
                        )
                    ),
                    onToggleGridMode = { gridToggleClicked = true },
                    onTopicCategorySelected = {},
                    onSectionScrollConsumed = {},
                    onActivityCardClicked = {},
                    onCompleteSelectedActivity = {},
                    onDismissActivityModal = {},
                    onOpenSettings = {},
                    onDismissSettings = {},
                    onUpdateAudioPreferences = { _, _ -> },
                    onClearFeedbackMessage = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.ROADMAP_SCREEN_ROOT, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(AppConstants.TestTags.TOPICS_GRID_CONTAINER, useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
