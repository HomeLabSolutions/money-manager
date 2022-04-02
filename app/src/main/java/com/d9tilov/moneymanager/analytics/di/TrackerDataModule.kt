package com.d9tilov.moneymanager.analytics.di

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {

    @Provides
    @Singleton
    fun provideFirebaseTracker(
        preferencesStore: PreferencesStore
    ): FirebaseAnalytics {
        val tracker: FirebaseAnalytics = Firebase.analytics
        GlobalScope.launch { tracker.setUserId(preferencesStore.uid.first()) }
        return tracker
    }
}
