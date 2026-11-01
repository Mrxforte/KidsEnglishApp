package com.kidsenglish.app.ui.roadmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kidsenglish.app.ui.theme.KidsEnglishTheme

/**
 * Simple Activity to preview the Roadmap UI.
 * In the real app this will be navigated to from the home / onboarding flow.
 */
class RoadmapPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidsEnglishTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RoadmapScreen(
                        roadmapData = createSampleRoadmapData(),
                        onNodeClick = { node ->
                            // TODO: Navigate to LessonScreen(node.id)
                            println("Clicked lesson: ${node.title} (${node.id})")
                        }
                    )
                }
            }
        }
    }
}
