package com.d9tilov.android.widget.ui

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.ui.unit.DpSize
import androidx.glance.appwidget.compose
import com.d9tilov.android.widget.domain.model.SpendingWidgetState

object SpendingWidgetViews {
    private val widget = SpendingWidget()

    internal suspend fun create(
        context: Context,
        state: SpendingWidgetState,
        size: DpSize,
    ): RemoteViews = widget.compose(context, size = size, state = state)
}
