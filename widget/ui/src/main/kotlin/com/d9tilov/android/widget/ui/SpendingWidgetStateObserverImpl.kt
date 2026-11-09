package com.d9tilov.android.widget.ui

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.d9tilov.android.widget.domain.contract.SpendingWidgetStateObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpendingWidgetStateObserverImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : SpendingWidgetStateObserver {
    override suspend fun notifyStateChanged() {
        SpendingWidget().updateAll(context)
    }
}
