package com.d9tilov.android.widget.ui

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.d9tilov.android.core.di.ApplicationContext
import com.d9tilov.android.widget.domain.contract.SpendingWidgetStateObserver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@Inject
class SpendingWidgetStateObserverImpl(
    @param:ApplicationContext private val context: Context,
) : SpendingWidgetStateObserver {
    override suspend fun notifyStateChanged() {
        SpendingWidget().updateAll(context)
    }
}
