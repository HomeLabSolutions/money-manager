package com.d9tilov.android.common.android.di

import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_DEFAULT
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@BindingContainer
@ContributesTo(AppScope::class)
class CoroutinesModule {
    @Provides
    @SingleIn(AppScope::class)
    @Named(DISPATCHER_IO)
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @SingleIn(AppScope::class)
    @Named(DISPATCHER_DEFAULT)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
