package com.d9tilov.android.datastore.di

import android.content.Context
import com.d9tilov.android.datastore.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
    @Provides
    @Singleton
    fun providePreferenceStore(
        @ApplicationContext context: Context,
    ): PreferencesStore = PreferencesStore(context)
}
