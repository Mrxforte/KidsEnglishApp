package com.kidsenglish.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persisted record of a completed or in-progress lesson.
 */
@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey
    val lessonId: String,
    val worldId: String,
    val lessonNumber: Int,
    val starsEarned: Int, // 0..3
    val isCompleted: Boolean,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val scorePercentage: Float,
    val lastPlayedTimestamp: Long
)
