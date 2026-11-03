package com.kidsenglish.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.data.repository.AppStartupRepositoryImpl
import com.kidsenglish.app.domain.repository.AppStartupRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Dependency Injection module providing application-scoped singletons
 * and binding Clean Architecture repository interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppStartupRepository(
        impl: AppStartupRepositoryImpl
    ): AppStartupRepository

    companion object {
        @Provides
        @Singleton
        fun providePreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                produceFile = {
                    context.preferencesDataStoreFile(AppConstants.PREFERENCES_NAME)
                }
            )
        }
    }
}
