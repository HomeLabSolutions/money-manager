package com.d9tilov.moneymanager.core.util

import android.content.Context
import android.content.res.Configuration

fun Context.isTablet(): Boolean {
    val xlarge = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == 4
    val large = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
    return xlarge || large
}
