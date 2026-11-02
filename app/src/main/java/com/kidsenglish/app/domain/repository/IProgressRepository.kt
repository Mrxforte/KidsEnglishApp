package com.kidsenglish.app.domain.repository

import com.kidsenglish.app.core.database.entity.LessonProgressEntity
import com.kidsenglish.app.core.database.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

interface IProgressRepository {
    fun getUserProgress(): Flow<UserProgressEntity>
    fun getAllLessonProgress(): Flow<List<LessonProgressEntity>>
    suspend fun saveLessonCompletion(
        lessonId: String,
        worldId: String,
        lessonNumber: Int,
        starsEarned: Int,
        correctAnswers: Int,
        totalQuestions: Int
    )
    suspend fun resetProgress()
}
