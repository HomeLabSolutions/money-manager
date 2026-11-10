package com.d9tilov.android.widget.ui

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.d9tilov.android.widget.domain.contract.SpendingWidgetStateObserver

class SpendingWidgetStateObserverImpl(
    private val context: Context,
) : SpendingWidgetStateObserver {
    override suspend fun notifyStateChanged() {
        SpendingWidget().updateAll(context)
    }
}
