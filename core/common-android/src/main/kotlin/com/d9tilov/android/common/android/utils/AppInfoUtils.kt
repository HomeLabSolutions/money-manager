package com.d9tilov.android.common.android.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.d9tilov.android.core.constants.DataConstants.TAG
import timber.log.Timber

fun getAppVersion(context: Context): String {
    return try {
        val packageInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0),
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
        val a = packageInfo.versionName ?: "N/A"
        Timber.tag(TAG).d("Version: $a")
        a
    } catch (e: UnsupportedOperationException) {
        Timber.tag(TAG).e("Can't get app version. Unsupported operation: $e")
        "N/A"
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.tag(TAG).e("Can't get app version. Name not found: $e")
        "N/A"
    }
}
