package com.d9tilov.android.common.android.ui.logout

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.d9tilov.android.common.android.BuildConfig
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.internal.managers.FragmentComponentManager
import dagger.hilt.components.SingletonComponent

fun Context.logout() {
    EntryPointAccessors
        .fromApplication<LogoutEntryPoint>(applicationContext)
        .logoutHandler()
        .onLogout()
    val intent =
        this.packageManager.getLaunchIntentForPackage(
            if (BuildConfig.DEBUG) "com.d9tilov.moneymanager.debug" else "com.d9tilov.moneymanager",
        )
    if (intent != null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.startActivity(intent)
        (FragmentComponentManager.findActivity(this) as Activity).finish()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LogoutEntryPoint {
    fun logoutHandler(): LogoutHandler
}
