package com.d9tilov.moneymanager.di

import android.app.Application
import android.content.Context
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.common.android.ui.logout.LogoutHandler
import com.d9tilov.android.transaction.regular.data.impl.worker.RegularTransactionSyncWorker
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.insights.WeeklyInsightNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideApplication(application: Application): App = application as App

    @Provides
    fun provideLogoutHandler(
        application: Application,
        notification: WeeklyInsightNotification,
    ): LogoutHandler =
        LogoutHandler {
            PeriodicBackupWorker.stopPeriodicJob(application)
            RegularTransactionSyncWorker.stopPeriodicJob(application)
            notification.cancel()
        }

    @Provides
    @Singleton
    fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}
