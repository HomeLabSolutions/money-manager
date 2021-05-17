package com.d9tilov.moneymanager.core.util

import android.view.View
import androidx.core.view.isVisible

fun View.gone(): Boolean {
    if (isVisible) {
        visibility = View.GONE
        return true
    }
    return false
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show(): Boolean {
    if (!isVisible) {
        visibility = View.VISIBLE
        return true
    }
    return false
}
