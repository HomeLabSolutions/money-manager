package com.d9tilov.moneymanager.di

import android.app.Application
import android.content.Context
import com.d9tilov.android.common.android.ui.logout.LogoutHandler
import com.d9tilov.android.core.di.ApplicationContext
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.logout.AppLogoutHandler
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@BindingContainer
@ContributesTo(AppScope::class)
object AppModule {
    @Provides
    @ApplicationContext
    fun provideApplicationContext(application: Application): Context = application

    @Provides
    @SingleIn(AppScope::class)
    fun provideContext(application: Application): Context = application

    @Provides
    @SingleIn(AppScope::class)
    fun provideApplication(application: Application): App = application as App

    @Provides
    fun provideLogoutHandler(handler: AppLogoutHandler): LogoutHandler = handler

    @Provides
    @SingleIn(AppScope::class)
    fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}
