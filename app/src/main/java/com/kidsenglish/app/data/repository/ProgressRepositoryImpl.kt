package com.kidsenglish.app.data.repository

import com.kidsenglish.app.core.database.dao.ProgressDao
import com.kidsenglish.app.core.database.entity.LessonProgressEntity
import com.kidsenglish.app.core.database.entity.UserProgressEntity
import com.kidsenglish.app.domain.repository.IProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgressRepositoryImpl(
    private val progressDao: ProgressDao
) : IProgressRepository {

    override fun getUserProgress(): Flow<UserProgressEntity> {
        return progressDao.getUserProgress().map { entity ->
            entity ?: UserProgressEntity(
                userId = "primary_learner",
                totalStars = 128,
                streakDays = 7,
                currentLevelNumber = 3
            )
        }
    }

    override fun getAllLessonProgress(): Flow<List<LessonProgressEntity>> {
        return progressDao.getAllLessonProgress()
    }

    override suspend fun saveLessonCompletion(
        lessonId: String,
        worldId: String,
        lessonNumber: Int,
        starsEarned: Int,
        correctAnswers: Int,
        totalQuestions: Int
    ) {
        val scorePercent = if (totalQuestions > 0) correctAnswers.toFloat() / totalQuestions else 1.0f

        val lessonRecord = LessonProgressEntity(
            lessonId = lessonId,
            worldId = worldId,
            lessonNumber = lessonNumber,
            starsEarned = starsEarned,
            isCompleted = true,
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            scorePercentage = scorePercent,
            lastPlayedTimestamp = System.currentTimeMillis()
        )
        progressDao.insertOrUpdateLesson(lessonRecord)

        val currentUser = progressDao.getUserProgressSync() ?: UserProgressEntity()
        val nextLevel = maxOf(currentUser.currentLevelNumber, lessonNumber + 1)
        val updatedUser = currentUser.copy(
            totalStars = currentUser.totalStars + starsEarned,
            currentLevelNumber = nextLevel
        )
        progressDao.insertOrUpdateUser(updatedUser)
    }

    override suspend fun resetProgress() {
        progressDao.clearAllLessonProgress()
        progressDao.resetUserProgress()
    }
}
