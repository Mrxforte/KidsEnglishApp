package com.kidsenglish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kidsenglish.app.domain.model.RoadmapNode
import com.kidsenglish.app.ui.lesson.LessonScreen
import com.kidsenglish.app.ui.lesson.createSampleLessonC
import com.kidsenglish.app.ui.roadmap.RoadmapScreen
import com.kidsenglish.app.ui.roadmap.createSampleRoadmapData
import com.kidsenglish.app.ui.theme.KidsEnglishTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KidsEnglishTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var currentLessonId by remember { mutableStateOf<String?>(null) }

                    if (currentLessonId == null) {
                        RoadmapScreen(
                            roadmapData = createSampleRoadmapData(),
                            onNodeClick = { node: RoadmapNode ->
                                if (node.state.name != "LOCKED") {
                                    currentLessonId = node.id
                                }
                            }
                        )
                    } else {
                        // For now we always open the sample C lesson
                        // Later this will load the correct lesson by ID
                        LessonScreen(
                            lesson = createSampleLessonC(),
                            onBack = { currentLessonId = null },
                            onLessonCompleted = { stars ->
                                // TODO: save progress
                                currentLessonId = null
                            }
                        )
                    }
                }
            }
        }
    }
}
