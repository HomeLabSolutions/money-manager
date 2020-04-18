package com.d9tilov.moneymanager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.fragment.app.Fragment
import com.d9tilov.moneymanager.core.di.CoreComponent
import com.d9tilov.moneymanager.core.di.DaggerCoreComponent
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho

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
    }

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.create()
    }

    companion object {
        @JvmStatic
        fun coreComponent(context: Context) =
            (context.applicationContext as App).coreComponent
    }
}

fun Activity.coreComponent() = App.coreComponent(this)
fun Fragment.coreComponent() = App.coreComponent(requireContext())
