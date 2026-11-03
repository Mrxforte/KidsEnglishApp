package com.kidsenglish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.theme.AppTheme
import com.kidsenglish.app.core.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity entry point for the Kids English application.
 *
 * Integrates the AndroidX SplashScreen API for immediate cold-start responsiveness,
 * enables full-screen edge-to-edge rendering, and mounts the Compose [AppNavigation] graph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(
                    color = AppColors.Background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
