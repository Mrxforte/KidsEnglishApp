package com.kidsenglish.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kidsenglish.app.feature.roadmap.presentation.RoadmapRoute
import com.kidsenglish.app.feature.splash.presentation.SplashRoute

/**
 * Root Navigation Compose host for the Kids English application.
 *
 * Configured with [AppDestination.LevelsRoadmap] as the primary start destination
 * so the application opens directly onto the Horizontal Levels Roadmap Template
 * matching the 5 reference screenshots.
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.LevelsRoadmap,
        modifier = modifier
    ) {
        composable<AppDestination.Splash> {
            SplashRoute(
                onSplashFinished = {
                    navController.navigate(AppDestination.LevelsRoadmap) {
                        popUpTo<AppDestination.Splash> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AppDestination.LevelsRoadmap> {
            RoadmapRoute()
        }
    }
}
