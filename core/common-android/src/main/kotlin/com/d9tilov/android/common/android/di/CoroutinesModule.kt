package com.d9tilov.android.common.android.di

import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_DEFAULT
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutinesModule {
    @Provides
    @Singleton
    @Named(DISPATCHER_IO)
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @Named(DISPATCHER_DEFAULT)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
