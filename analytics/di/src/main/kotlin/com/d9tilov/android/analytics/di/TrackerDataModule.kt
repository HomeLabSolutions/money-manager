package com.d9tilov.android.analytics.di

import android.content.Context
import com.d9tilov.android.analytics.data.FirebaseAnalyticsSender
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.datastore.PreferencesStore
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {
    @Provides
    @Singleton
    fun provideFirebaseTracker(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope,
        preferencesStore: PreferencesStore,
    ): FirebaseAnalytics {
        val tracker: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
        coroutineScope.launch {
            tracker.setUserId(preferencesStore.uid.firstOrNull())
        }
        return tracker
    }

    @Provides
    @Singleton
    fun provideAnalyticsSender(firebaseAnalytics: FirebaseAnalytics): AnalyticsSender =
        FirebaseAnalyticsSender(firebaseAnalytics)
}
