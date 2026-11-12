package com.d9tilov.android.analytics.di

import android.content.Context
import com.d9tilov.android.analytics.data.FirebaseAnalyticsSender
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.core.di.ApplicationContext
import com.d9tilov.android.datastore.PreferencesStore
import com.google.firebase.analytics.FirebaseAnalytics
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@BindingContainer
@ContributesTo(AppScope::class)
object TrackerDataModule {
    @Provides
    @SingleIn(AppScope::class)
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
    @SingleIn(AppScope::class)
    fun provideAnalyticsSender(firebaseAnalytics: FirebaseAnalytics): AnalyticsSender =
        FirebaseAnalyticsSender(firebaseAnalytics)
}
