package com.kidsenglish.app.domain.model

/**
 * Represents a single lesson node on the roadmap path.
 */
data class RoadmapNode(
    val id: String,
    val lessonNumber: Int,
    val title: String,                 // e.g. "A — Apple"
    val titleUz: String,               // e.g. "A — Olma"
    val topicId: String,
    val state: NodeState,
    val starsEarned: Int = 0,          // 0..3
    val maxStars: Int = 3,
    val isCurrent: Boolean = false,
    val iconEmoji: String = "🍎",      // Fun emoji fallback for demo
    val progressPercent: Float = 0f    // for topic header
)

enum class NodeState {
    LOCKED,
    AVAILABLE,
    CURRENT,
    COMPLETED
}

/**
 * A major topic section on the roadmap (e.g. Funny ABC)
 */
data class RoadmapTopic(
    val id: String,
    val number: Int,
    val title: String,                 // "Funny ABC"
    val titleUz: String,               // "Qiziqarli ABC"
    val descriptionUz: String,
    val emoji: String,
    val totalLessons: Int,
    val completedLessons: Int,
    val nodes: List<RoadmapNode>,
    val isUnlocked: Boolean = true
) {
    val completionPercent: Float
        get() = if (totalLessons == 0) 0f else completedLessons.toFloat() / totalLessons
}

/**
 * Full roadmap data for the vertical journey
 */
data class RoadmapData(
    val topics: List<RoadmapTopic>,
    val currentNodeId: String? = null,
    val totalStars: Int = 0,
    val streakDays: Int = 0
)
