package com.d9tilov.moneymanager.analytics.di

import com.d9tilov.FirebaseEventsTracker
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.analytics.EventsTracker
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TrackerDataModule {

    @Provides
    @Singleton
    fun provideTracker(
        application: App,
        preferencesStore: PreferencesStore
    ): EventsTracker = FirebaseEventsTracker(
        application,
        preferencesStore
    )
}
