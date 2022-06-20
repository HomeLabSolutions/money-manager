package com.d9tilov.moneymanager

import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

@HiltAndroidApp
class App : MultiDexApplication(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
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

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    companion object {
        const val TAG = "[MoneyManager]"
    }
}
