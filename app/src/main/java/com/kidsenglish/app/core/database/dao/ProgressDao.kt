package com.kidsenglish.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kidsenglish.app.core.database.entity.LessonProgressEntity
import com.kidsenglish.app.core.database.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId")
    suspend fun getLessonProgress(lessonId: String): LessonProgressEntity?

    @Query("SELECT * FROM lesson_progress")
    fun getAllLessonProgress(): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLesson(progress: LessonProgressEntity)

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    fun getUserProgress(userId: String = "primary_learner"): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    suspend fun getUserProgressSync(userId: String = "primary_learner"): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(userProgress: UserProgressEntity)

    @Query("DELETE FROM lesson_progress")
    suspend fun clearAllLessonProgress()

    @Query("UPDATE user_progress SET totalStars = 0, currentLevelNumber = 1 WHERE userId = :userId")
    suspend fun resetUserProgress(userId: String = "primary_learner")
}
