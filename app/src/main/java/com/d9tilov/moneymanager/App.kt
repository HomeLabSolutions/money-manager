package com.d9tilov.moneymanager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.base.di.AppComponent
import com.d9tilov.moneymanager.base.di.AppModule
import com.d9tilov.moneymanager.base.di.DaggerAppComponent
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val nightMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            MODE_NIGHT_AUTO_BATTERY
        }
        setDefaultNightMode(nightMode)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
    }

    companion object {
        @JvmStatic
        fun appComponent(context: Context) =
            (context.applicationContext as App).appComponent
    }
}

fun Activity.appComponent() = App.appComponent(this)
fun Fragment.appComponent() = App.appComponent(requireContext())
