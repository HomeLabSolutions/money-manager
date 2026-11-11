package com.d9tilov.android.common.android.di

import android.content.Context
import com.d9tilov.android.common.android.location.LocationProvider
import com.d9tilov.android.common.android.location.LocationProviderImpl
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.di.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher

@BindingContainer
@ContributesTo(AppScope::class)
class LocationModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideLocationProvider(
        @ApplicationContext context: Context,
        @Named(DISPATCHER_IO) ioDispatcher: CoroutineDispatcher,
    ): LocationProvider = LocationProviderImpl(context, ioDispatcher)
}
