package com.kidsenglish.app.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kidsenglish.app.core.database.dao.ProgressDao
import com.kidsenglish.app.core.database.entity.LessonProgressEntity
import com.kidsenglish.app.core.database.entity.UserProgressEntity

@Database(
    entities = [
        LessonProgressEntity::class,
        UserProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KidsEnglishDatabase : RoomDatabase() {

    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var INSTANCE: KidsEnglishDatabase? = null

        fun getInstance(context: Context): KidsEnglishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KidsEnglishDatabase::class.java,
                    "kids_english.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
