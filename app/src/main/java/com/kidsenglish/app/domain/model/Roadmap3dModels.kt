package com.kidsenglish.app.domain.model

import androidx.compose.ui.graphics.Color

/**
 * World definitions following the educational curriculum.
 */
enum class WorldType {
    GREEN_VALLEY,     // Alphabet & Nature (Current active prototype)
    OCEAN_WORLD,      // Phonics & Sea Creatures
    CANDY_MOUNTAINS,  // Numbers & Colors
    ANIMAL_KINGDOM    // Vocabulary & Daily Life
}

data class WorldDefinition(
    val id: String,
    val type: WorldType,
    val title: String,
    val titleUz: String,
    val subtitleUz: String,
    val bannerEmoji: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val totalLessons: Int,
    val isUnlocked: Boolean
)

/**
 * Node states on the 3D Adventure Roadmap.
 */
enum class NodeState3D {
    LOCKED,
    AVAILABLE,
    CURRENT,
    COMPLETED,
    PERFECT // 3 Stars earned
}

/**
 * 3D Coordinate in world space.
 */
data class Vector3D(
    val x: Float,
    val y: Float,
    val z: Float
)

/**
 * Interactive 3D lesson platform node along the winding path.
 */
data class RoadmapNode3D(
    val id: String,
    val worldId: String,
    val lessonNumber: Int,
    val title: String,
    val titleUz: String,
    val subtitle: String,
    val emoji: String,
    val position: Vector3D,
    val state: NodeState3D,
    val starsEarned: Int = 0,
    val isMilestone: Boolean = false
)

/**
 * Camera state for 3D navigation.
 */
data class Camera3D(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = -650f,
    val pitch: Float = 22f, // Downward tilt for isometric depth
    val fov: Float = 600f
)
