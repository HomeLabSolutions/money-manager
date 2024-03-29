package com.d9tilov.moneymanager.core.util

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Activity.showKeyboard(view: View?) {
    view?.let { showSoftKeyboard(it) }
}

fun Fragment.showKeyboard(view: View?) {
    view?.let { activity?.showSoftKeyboard(it) }
}

fun Context.showSoftKeyboard(view: View) {
    view.requestFocus()
    Handler().post {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

fun Activity.hideKeyboard() {
    val view = currentFocus
    view?.let { hideKeyboard(it) }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
