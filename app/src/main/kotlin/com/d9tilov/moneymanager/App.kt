package com.d9tilov.moneymanager

import android.app.Application
import android.os.StrictMode
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.currency.data.impl.sync.initializers.Sync
import com.google.android.material.color.DynamicColors
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
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
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Sync.initialize(this)
        PeriodicBackupWorker.startPeriodicJob(this)
    }
}
