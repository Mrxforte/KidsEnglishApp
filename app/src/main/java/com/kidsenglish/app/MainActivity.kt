package com.kidsenglish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kidsenglish.app.core.audio.rememberAudioManager
import com.kidsenglish.app.core.database.KidsEnglishDatabase
import com.kidsenglish.app.core.database.entity.UserProgressEntity
import com.kidsenglish.app.core.theme.KidsEnglishAppTheme
import com.kidsenglish.app.data.repository.ProgressRepositoryImpl
import com.kidsenglish.app.domain.model.RoadmapNode
import com.kidsenglish.app.domain.model.RoadmapNode3D
import com.kidsenglish.app.feature.lesson.LessonPlayerScreen
import com.kidsenglish.app.feature.roadmap3d.Roadmap3dScreen
import com.kidsenglish.app.feature.settings.AudioSettingsDialog
import com.kidsenglish.app.feature.tablet.KidsTabletHomeScreen
import com.kidsenglish.app.ui.lesson.getLessonForLevel
import com.kidsenglish.app.ui.roadmap.MeningYolimScreen
import com.kidsenglish.app.ui.roadmap.RoadmapScreen
import com.kidsenglish.app.ui.roadmap.createSampleRoadmapData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

enum class ScreenView {
    TABLET_HOME,
    ROADMAP_3D,
    MENING_YOLIM,
    FULL_ROADMAP,
    LESSON
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            // 1. Centralized Audio Manager (SFX + Funny baby background sounds)
            val audioManager = rememberAudioManager()

            // 2. Room Database & Reactive Progress Repository
            val database = remember { KidsEnglishDatabase.getInstance(context) }
            val progressRepository = remember { ProgressRepositoryImpl(database.progressDao()) }
            val userProgress by progressRepository.getUserProgress().collectAsState(
                initial = UserProgressEntity(totalStars = 128, streakDays = 7, currentLevelNumber = 3)
            )

            // 3. Navigation State (Starting at Tablet Home matching reference image)
            var currentScreen by remember { mutableStateOf(ScreenView.TABLET_HOME) }
            var selectedLevel by remember { mutableIntStateOf(3) }
            var showSettingsDialog by remember { mutableStateOf(false) }

            KidsEnglishAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    when (currentScreen) {
                        ScreenView.TABLET_HOME -> {
                            KidsTabletHomeScreen(
                                audioManager = audioManager,
                                onAppSelected = { routeKey ->
                                    when (routeKey) {
                                        "abc_roadmap" -> currentScreen = ScreenView.ROADMAP_3D
                                        "animals" -> {
                                            selectedLevel = 3 // Cat lesson
                                            currentScreen = ScreenView.LESSON
                                        }
                                        "phones", "chat", "art" -> {
                                            selectedLevel = 1 // Apple lesson
                                            currentScreen = ScreenView.LESSON
                                        }
                                        else -> currentScreen = ScreenView.ROADMAP_3D
                                    }
                                }
                            )
                        }

                        ScreenView.ROADMAP_3D -> {
                            BackHandler {
                                currentScreen = ScreenView.TABLET_HOME
                            }
                            Roadmap3dScreen(
                                audioManager = audioManager,
                                totalStars = userProgress.totalStars,
                                streakDays = userProgress.streakDays,
                                currentLevelNumber = userProgress.currentLevelNumber,
                                onNodeClick = { node: RoadmapNode3D ->
                                    selectedLevel = node.lessonNumber
                                    currentScreen = ScreenView.LESSON
                                },
                                onSettingsClick = {
                                    showSettingsDialog = true
                                },
                                onLegacyPathClick = {
                                    currentScreen = ScreenView.TABLET_HOME
                                }
                            )
                        }

                        ScreenView.MENING_YOLIM -> {
                            BackHandler {
                                currentScreen = ScreenView.TABLET_HOME
                            }
                            MeningYolimScreen(
                                totalStars = userProgress.totalStars,
                                streakDays = userProgress.streakDays,
                                onBackClick = {
                                    currentScreen = ScreenView.TABLET_HOME
                                },
                                onLevelClick = { level ->
                                    selectedLevel = level
                                    currentScreen = ScreenView.LESSON
                                }
                            )
                        }

                        ScreenView.FULL_ROADMAP -> {
                            BackHandler {
                                currentScreen = ScreenView.TABLET_HOME
                            }
                            RoadmapScreen(
                                roadmapData = createSampleRoadmapData().copy(
                                    totalStars = userProgress.totalStars,
                                    streakDays = userProgress.streakDays
                                ),
                                onNodeClick = { node: RoadmapNode ->
                                    if (node.state.name != "LOCKED") {
                                        selectedLevel = node.lessonNumber
                                        currentScreen = ScreenView.LESSON
                                    }
                                }
                            )
                        }

                        ScreenView.LESSON -> {
                            BackHandler {
                                currentScreen = ScreenView.ROADMAP_3D
                            }
                            LessonPlayerScreen(
                                lesson = getLessonForLevel(selectedLevel),
                                audioManager = audioManager,
                                onBack = {
                                    currentScreen = ScreenView.ROADMAP_3D
                                },
                                onLessonCompleted = { starsEarned, correct, total ->
                                    coroutineScope.launch {
                                        progressRepository.saveLessonCompletion(
                                            lessonId = "lesson_$selectedLevel",
                                            worldId = "green_valley",
                                            lessonNumber = selectedLevel,
                                            starsEarned = starsEarned,
                                            correctAnswers = correct,
                                            totalQuestions = total
                                        )
                                    }
                                    currentScreen = ScreenView.ROADMAP_3D
                                }
                            )
                        }
                    }

                    // Settings Dialog Overlay
                    if (showSettingsDialog) {
                        AudioSettingsDialog(
                            audioManager = audioManager,
                            onDismiss = { showSettingsDialog = false },
                            onResetProgress = {
                                coroutineScope.launch {
                                    progressRepository.resetProgress()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
