package com.d9tilov.moneymanager

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho
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
        val nightMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            MODE_NIGHT_AUTO_BATTERY
        }
        setDefaultNightMode(nightMode)
        AppEventsLogger.activateApp(this)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
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
