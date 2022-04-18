package com.d9tilov.moneymanager.base.di

import android.app.Application
import android.content.Context
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideApplication(application: Application): App = application as App

    @Provides
    @Singleton
    fun providePreferenceStore(@ApplicationContext context: Context): PreferencesStore =
        PreferencesStore(context)
}
