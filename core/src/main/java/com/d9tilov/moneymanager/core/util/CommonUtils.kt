package com.d9tilov.moneymanager.core.util

import android.R
import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes

fun showLoadingDialog(context: Context, rootView: View): ProgressBar {
    val layout = rootView as ViewGroup
    val params = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.MATCH_PARENT
    )

    val mProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLargeInverse)
    mProgressBar.isIndeterminate = true

    val rl = RelativeLayout(context)

    rl.gravity = Gravity.CENTER
    rl.addView(mProgressBar)

    layout.addView(rl, params)

    return mProgressBar
}

fun hideLoadingDialog(rootView: ViewGroup?, parentView: View?) {
    if (rootView == null || parentView == null)
        return
    rootView.removeView(parentView)
}

fun Context?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast.makeText(this, textId, duration)
    val v: TextView? = toast.view.findViewById(R.id.message)
    v?.gravity = CENTER
    this?.let { toast.show() }
}
