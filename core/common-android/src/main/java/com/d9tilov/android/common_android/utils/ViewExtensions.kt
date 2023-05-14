package com.d9tilov.android.common_android.utils

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

fun View.showWithAnimation() {
    this.run {
        alpha = 0f
        show()
        animate()
            .alpha(1f)
            .setDuration(ANIMATION_DURATION)
            .setListener(null)
    }
}

fun View.hideWithAnimation() {
    this.run {
        alpha = 1f
        hide()
        animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION)
            .setListener(null)
    }
}

const val ANIMATION_DURATION = 300L
const val IMAGE_SIZE_IN_PX = 136
