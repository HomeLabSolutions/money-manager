package com.d9tilov.moneymanager.core.util

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.gone() {
    if (isGone) return
    visibility = View.GONE
}

fun View.hide() {
    if (isInvisible) return
    visibility = View.INVISIBLE
}

fun View.show() {
    if (isVisible) return
    visibility = View.VISIBLE
}
