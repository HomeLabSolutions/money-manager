package com.d9tilov.android.analytics.di

import com.d9tilov.android.datastore.PreferencesStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(ActivityRetainedComponent::class)
object TrackerDataModule {

    @Provides
    @ActivityRetainedScoped
    fun provideFirebaseTracker(
        preferencesStore: PreferencesStore
    ): FirebaseAnalytics {
        val tracker: FirebaseAnalytics = Firebase.analytics
        runBlocking { tracker.setUserId(preferencesStore.uid.first()) }
        return tracker
    }
}
