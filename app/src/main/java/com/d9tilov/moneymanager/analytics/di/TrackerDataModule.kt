package com.d9tilov.moneymanager.analytics.di

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
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
    fun provideFirebaseTracker(
        preferencesStore: PreferencesStore
    ): FirebaseAnalytics {
        val tracker: FirebaseAnalytics = Firebase.analytics
        tracker.setUserId(preferencesStore.uid)
        return tracker
    }
}
