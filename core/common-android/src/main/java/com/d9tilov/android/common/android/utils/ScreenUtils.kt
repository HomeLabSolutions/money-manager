package com.d9tilov.android.common.android.utils // ktlint-disable filename

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE

fun Context.isTablet(): Boolean {
    val xlarge = resources.configuration.screenLayout and
        Configuration.SCREENLAYOUT_SIZE_MASK == SCREENLAYOUT_SIZE_XLARGE
    val large =
        resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
    return xlarge || large
}
