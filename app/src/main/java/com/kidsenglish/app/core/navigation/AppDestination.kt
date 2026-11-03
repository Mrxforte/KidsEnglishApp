package com.kidsenglish.app.core.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destinations for the Kids English application.
 */
sealed interface AppDestination {

    /**
     * Initial application launch destination displaying the Kids English Splash Screen.
     */
    @Serializable
    data object Splash : AppDestination

    /**
     * Main Horizontal Levels Roadmap Template destination displaying both the
     * 3x6 All-Topics Tilted Card Grid and the Horizontal Scrolling Topic & Activity Cards Roadmap
     * matching the 5 reference screenshots.
     */
    @Serializable
    data object LevelsRoadmap : AppDestination
}
