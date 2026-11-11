package com.d9tilov.android.datastore.di

import android.content.Context
import com.d9tilov.android.core.di.ApplicationContext
import com.d9tilov.android.datastore.PreferencesStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
object DatastoreModule {
    @Provides
    @SingleIn(AppScope::class)
    fun providePreferenceStore(
        @ApplicationContext context: Context,
    ): PreferencesStore = PreferencesStore(context)
}
