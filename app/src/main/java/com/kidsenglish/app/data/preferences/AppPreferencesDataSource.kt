package com.kidsenglish.app.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.kidsenglish.app.domain.model.AppStartupConfig
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Local DataStore preferences data source managing persisted startup state, stickers, and audio toggles.
 */
@Singleton
class AppPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val LAUNCH_COUNT = intPreferencesKey("app_launch_count")
        val STICKERS_EARNED_TODAY = intPreferencesKey("stickers_earned_today")
        val SOUND_EFFECTS_ENABLED = booleanPreferencesKey("sound_effects_enabled")
        val BACKGROUND_MUSIC_ENABLED = booleanPreferencesKey("background_music_enabled")
        val COMPLETED_ACTIVITY_IDS = stringSetPreferencesKey("completed_activity_ids")
    }

    /**
     * Streams the persisted [AppStartupConfig] from Jetpack DataStore.
     */
    val startupConfigFlow: Flow<AppStartupConfig> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val count = preferences[PreferencesKeys.LAUNCH_COUNT] ?: 0
            val stickers = preferences[PreferencesKeys.STICKERS_EARNED_TODAY] ?: 0
            val sfx = preferences[PreferencesKeys.SOUND_EFFECTS_ENABLED] ?: true
            val bgm = preferences[PreferencesKeys.BACKGROUND_MUSIC_ENABLED] ?: true
            val completed = preferences[PreferencesKeys.COMPLETED_ACTIVITY_IDS] ?: emptySet()

            AppStartupConfig(
                launchCount = count,
                isFirstLaunch = count <= 1,
                isInitialized = count > 0,
                stickersEarnedToday = stickers,
                isSoundEffectsEnabled = sfx,
                isBackgroundMusicEnabled = bgm,
                completedActivityIds = completed
            )
        }

    /**
     * Increments the persisted launch counter and returns the updated [AppStartupConfig].
     */
    suspend fun incrementLaunchCount(): AppStartupConfig {
        val updatedPreferences = dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.LAUNCH_COUNT] ?: 0
            preferences[PreferencesKeys.LAUNCH_COUNT] = current + 1
        }
        val newCount = updatedPreferences[PreferencesKeys.LAUNCH_COUNT] ?: 1
        return AppStartupConfig(
            launchCount = newCount,
            isFirstLaunch = newCount == 1,
            isInitialized = true
        )
    }

    /**
     * Records a completed activity ID and increments daily stickers earned.
     */
    suspend fun recordActivityCompleted(activityId: String) {
        dataStore.edit { preferences ->
            val currentStickers = preferences[PreferencesKeys.STICKERS_EARNED_TODAY] ?: 0
            val currentSet = preferences[PreferencesKeys.COMPLETED_ACTIVITY_IDS] ?: emptySet()

            preferences[PreferencesKeys.STICKERS_EARNED_TODAY] = currentStickers + 1
            preferences[PreferencesKeys.COMPLETED_ACTIVITY_IDS] = currentSet + activityId
        }
    }

    /**
     * Persists user sound effects and background music preferences.
     */
    suspend fun setAudioPreferences(soundEffectsEnabled: Boolean, backgroundMusicEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_EFFECTS_ENABLED] = soundEffectsEnabled
            preferences[PreferencesKeys.BACKGROUND_MUSIC_ENABLED] = backgroundMusicEnabled
        }
    }
}
