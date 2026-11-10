package com.d9tilov.android.common.android.di

import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_DEFAULT
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

val coroutinesModule =
    module {
        single(named(DISPATCHER_IO)) { create(::provideIoDispatcher) }
        single(named(DISPATCHER_DEFAULT)) { create(::provideDefaultDispatcher) }
    }
