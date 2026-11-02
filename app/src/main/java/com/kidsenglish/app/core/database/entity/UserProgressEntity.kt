package com.kidsenglish.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persisted summary stats for the learner: cumulative stars, streak, and current unlocked node.
 */
@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val userId: String = "primary_learner",
    val totalStars: Int = 128,
    val streakDays: Int = 7,
    val currentLevelNumber: Int = 3,
    val lastActiveDate: String = ""
)
