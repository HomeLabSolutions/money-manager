package com.d9tilov.moneymanager

import android.app.Application
import android.os.StrictMode
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.currency.data.impl.sync.initializers.Sync
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import com.d9tilov.android.transaction.regular.data.impl.worker.RegularTransactionSyncWorker
import com.d9tilov.moneymanager.insights.WeeklyInsightNotification
import com.d9tilov.moneymanager.insights.WeeklyInsightWorker
import com.google.android.material.color.DynamicColors
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            val threadPolicy =
                StrictMode.ThreadPolicy
                    .Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            StrictMode.setThreadPolicy(threadPolicy)
            val vmPolicy =
                StrictMode.VmPolicy
                    .Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            StrictMode.setVmPolicy(vmPolicy)
        }
        DynamicColors.applyToActivitiesIfAvailable(this)
        FirebaseApp.initializeApp(this)
        EntryPointAccessors
            .fromApplication<InsightsConsentEntryPoint>(this)
            .weeklyInsightNotification()
            .createNotificationChannel()
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        Sync.initialize(this)
        PeriodicBackupWorker.startPeriodicJob(this)
        RegularTransactionSyncWorker.startPeriodicJob(this)
        val insightsConsentRepository =
            EntryPointAccessors.fromApplication<InsightsConsentEntryPoint>(this).insightsConsentRepository()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            insightsConsentRepository.isGranted.distinctUntilChanged().collect { granted ->
                if (granted) {
                    WeeklyInsightWorker.startPeriodicJob(this@App)
                } else {
                    WeeklyInsightWorker.stopPeriodicJob(this@App)
                }
            }
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InsightsConsentEntryPoint {
    fun insightsConsentRepository(): InsightsConsentRepository

    fun weeklyInsightNotification(): WeeklyInsightNotification
}
