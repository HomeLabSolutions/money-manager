package com.d9tilov.android.analytics.di

import android.content.Context
import com.d9tilov.android.analytics.data.FirebaseAnalyticsSender
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.datastore.PreferencesStore
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

fun provideFirebaseTracker(
    context: Context,
    coroutineScope: CoroutineScope,
    preferencesStore: PreferencesStore,
): FirebaseAnalytics {
    val tracker: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    coroutineScope.launch {
        tracker.setUserId(preferencesStore.uid.firstOrNull())
    }
    return tracker
}

fun provideAnalyticsSender(firebaseAnalytics: FirebaseAnalytics): AnalyticsSender =
    FirebaseAnalyticsSender(firebaseAnalytics)

val trackerDataModule =
    module {
        single { create(::provideFirebaseTracker) }
        single { create(::provideAnalyticsSender) }
    }
