package com.d9tilov.android.widget.domain.contract

fun interface SpendingWidgetStateObserver {
    suspend fun notifyStateChanged()
}
