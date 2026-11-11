package com.d9tilov.moneymanager

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.work.Configuration
import androidx.work.WorkerFactory
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.cachecontrol.CacheControlCacheStrategy
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.common.android.worker.WorkerFactoryOwner
import com.d9tilov.android.currency.data.impl.sync.initializers.Sync
import com.d9tilov.android.insights.presentation.worker.WeeklyInsightWorker
import com.d9tilov.android.transaction.regular.data.impl.worker.RegularTransactionSyncWorker
import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import com.d9tilov.android.widget.ui.SpendingWidgetEntryPoint
import com.d9tilov.moneymanager.di.AppGraph
import com.google.android.material.color.DynamicColors
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.zacsweers.metro.createGraphFactory
import timber.log.Timber
import timber.log.Timber.DebugTree

class App :
    Application(),
    SingletonImageLoader.Factory,
    Configuration.Provider,
    WorkerFactoryOwner,
    SpendingWidgetEntryPoint {
    val appGraph: AppGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

    override val workerFactory: WorkerFactory
        get() = appGraph.workerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun spendingWidgetInteractor(): SpendingWidgetInteractor = appGraph.spendingWidgetInteractor()

    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: Context): ImageLoader =
        ImageLoader
            .Builder(context)
            .crossfade(true)
            .components {
                add(OkHttpNetworkFetcherFactory(cacheStrategy = { CacheControlCacheStrategy() }))
            }.build()

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
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        Sync.initialize(this)
        PeriodicBackupWorker.startPeriodicJob(this)
        RegularTransactionSyncWorker.startPeriodicJob(this)
        WeeklyInsightWorker.startPeriodicJob(this)
    }
}
