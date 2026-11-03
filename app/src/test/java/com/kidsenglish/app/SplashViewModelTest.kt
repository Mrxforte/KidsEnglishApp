package com.kidsenglish.app

import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.AppStartupConfig
import com.kidsenglish.app.domain.model.RoadmapHorizontalSection
import com.kidsenglish.app.domain.model.RoadmapTopicCategory
import com.kidsenglish.app.domain.model.TopicColorStyle
import com.kidsenglish.app.domain.model.TopicIconType
import com.kidsenglish.app.domain.repository.AppStartupRepository
import com.kidsenglish.app.domain.usecase.InitializeAppStartupUseCase
import com.kidsenglish.app.feature.roadmap.presentation.RoadmapViewModel
import com.kidsenglish.app.feature.splash.presentation.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeAppStartupRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeAppStartupRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `splashViewModel initializes startup config and transitions to ready state`() = runTest {
        val useCase = InitializeAppStartupUseCase(fakeRepository)
        val viewModel = SplashViewModel(useCase)

        // Advance coroutines through all startup steps
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isReady)
        assertTrue(state.phase is com.kidsenglish.app.feature.splash.presentation.SplashPhase.Ready)
        assertEquals(1.0f, state.loadingProgress, 0.001f)
        assertEquals(
            1,
            (state.phase as com.kidsenglish.app.feature.splash.presentation.SplashPhase.Ready).config.launchCount
        )
        assertEquals(AppStrings.Splash.READY_MESSAGE, state.statusMessageRes)
    }

    @Test
    fun `roadmapViewModel toggles between 3x6 grid mode and horizontal sections mode`() = runTest {
        val viewModel = RoadmapViewModel(fakeRepository)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isTopicsGridMode)

        viewModel.toggleTopicsGridMode()
        assertTrue(viewModel.uiState.value.isTopicsGridMode)

        viewModel.toggleTopicsGridMode()
        assertFalse(viewModel.uiState.value.isTopicsGridMode)
    }

    private class FakeAppStartupRepository : AppStartupRepository {
        private val configFlow = MutableStateFlow(
            AppStartupConfig(
                isInitialized = false,
                launchCount = 0,
                stickersEarnedToday = 0
            )
        )

        override fun observeStartupConfig(): Flow<AppStartupConfig> = configFlow

        override suspend fun recordAppLaunch(): AppStartupConfig {
            val updated = configFlow.value.copy(
                isInitialized = true,
                launchCount = configFlow.value.launchCount + 1
            )
            configFlow.value = updated
            return updated
        }

        override suspend fun completeActivityAndEarnSticker(activityId: String) {
            configFlow.value = configFlow.value.copy(
                stickersEarnedToday = configFlow.value.stickersEarnedToday + 1,
                completedActivityIds = configFlow.value.completedActivityIds + activityId
            )
        }

        override suspend fun updateAudioPreferences(
            soundEffectsEnabled: Boolean,
            backgroundMusicEnabled: Boolean
        ) {
            configFlow.value = configFlow.value.copy(
                isSoundEffectsEnabled = soundEffectsEnabled,
                isBackgroundMusicEnabled = backgroundMusicEnabled
            )
        }

        override fun getTopicCategories(completedIds: Set<String>): List<RoadmapTopicCategory> {
            return listOf(
                RoadmapTopicCategory(
                    id = "abc",
                    titleRes = AppStrings.Roadmap.TOPIC_ABC,
                    iconType = TopicIconType.ABC_LETTERS,
                    colorStyle = TopicColorStyle.PURPLE,
                    wordsLearnedProgress = 0.5f,
                    targetSectionIndex = 0,
                    tiltDegrees = -1.5f
                )
            )
        }

        override fun getHorizontalSections(completedIds: Set<String>): List<RoadmapHorizontalSection> {
            return emptyList()
        }
    }
}
