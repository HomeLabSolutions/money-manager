package com.d9tilov.android.common.android.di

import android.content.Context
import com.d9tilov.android.common.android.location.LocationProvider
import com.d9tilov.android.common.android.location.LocationProviderImpl
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {
    @Provides
    @Singleton
    fun provideLocationProvider(
        @ApplicationContext context: Context,
        @Named(DISPATCHER_IO) ioDispatcher: CoroutineDispatcher,
    ): LocationProvider = LocationProviderImpl(context, ioDispatcher)
}
