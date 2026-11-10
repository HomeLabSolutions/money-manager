package com.d9tilov.android.common.android.di

import android.content.Context
import com.d9tilov.android.common.android.location.LocationProvider
import com.d9tilov.android.common.android.location.LocationProviderImpl
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

fun provideLocationProvider(
    context: Context,
    @Named(DISPATCHER_IO) ioDispatcher: CoroutineDispatcher,
): LocationProvider = LocationProviderImpl(context, ioDispatcher)

val locationModule =
    module {
        single { create(::provideLocationProvider) }
    }
