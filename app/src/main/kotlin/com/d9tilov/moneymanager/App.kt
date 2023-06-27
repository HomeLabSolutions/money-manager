package com.d9tilov.moneymanager

import android.app.Application
import android.os.StrictMode
import com.d9tilov.android.currency.ui.sync.initializers.Sync
import com.google.android.material.color.DynamicColors
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Sync.initialize(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            val threadPolicy = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
            StrictMode.setThreadPolicy(threadPolicy)
            val vmPolicy = StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
            StrictMode.setVmPolicy(vmPolicy)
        }
    }

    companion object {
        const val TAG = "[MoneyManager]"
    }
}
