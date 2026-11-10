package com.d9tilov.moneymanager.di

import com.d9tilov.android.common.android.ui.logout.LogoutHandler
import com.d9tilov.moneymanager.logout.AppLogoutHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.single

fun provideLogoutHandler(handler: AppLogoutHandler): LogoutHandler = handler

fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

val appModule =
    module {
        factory { create(::provideLogoutHandler) }
        single { create(::providesApplicationScope) }
    }
