package com.d9tilov.moneymanager.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.home.MainActivity

object SpendingWidgetViews {
    fun update(
        context: Context,
        state: SpendingWidgetState,
    ) {
        val ids = SpendingWidgetProvider.widgetIds(context)
        if (ids.isEmpty()) return
        AppWidgetManager.getInstance(context).updateAppWidget(ids, create(context, state))
    }

    internal fun create(
        context: Context,
        state: SpendingWidgetState,
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.spending_widget)
        views.setTextViewText(R.id.widget_title, context.getString(R.string.widget_name))
        views.setTextViewText(R.id.widget_today_label, context.getString(R.string.widget_spent_today))
        views.setTextViewText(R.id.widget_period_label, context.getString(R.string.widget_spent_period))
        views.setContentDescription(R.id.widget_refresh, context.getString(R.string.widget_refresh))
        views.setOnClickPendingIntent(R.id.spending_widget_root, openApp(context))
        views.setOnClickPendingIntent(R.id.widget_refresh, refresh(context))
        val ready = state as? SpendingWidgetState.Ready
        views.setViewVisibility(R.id.widget_amounts, if (ready == null) View.GONE else View.VISIBLE)
        views.setViewVisibility(R.id.widget_message, if (ready == null) View.VISIBLE else View.GONE)
        if (ready == null) {
            for (id in listOf(R.id.widget_available_value, R.id.widget_today_value, R.id.widget_period_value)) {
                views.setTextViewText(id, context.getString(R.string.widget_amount_placeholder))
            }
            views.setTextViewText(R.id.widget_message, context.getString(messageFor(state)))
        } else {
            bindAmounts(context, views, ready)
        }
        return views
    }

    private fun bindAmounts(
        context: Context,
        views: RemoteViews,
        state: SpendingWidgetState.Ready,
    ) {
        val symbol = state.currencyCode.getSymbolByCode()
        views.setTextViewText(R.id.widget_available_value, "$symbol${state.availableToday.reduceScaleStr()}")
        views.setTextViewText(R.id.widget_today_value, "$symbol${state.spentToday.reduceScaleStr()}")
        views.setTextViewText(R.id.widget_period_value, "$symbol${state.spentInPeriod.reduceScaleStr()}")
        val overspending = state.availableToday.signum() < 0
        views.setTextViewText(
            R.id.widget_available_label,
            context.getString(if (overspending) R.string.widget_overspending else R.string.widget_available_today),
        )
        views.setTextColor(
            R.id.widget_available_value,
            context.getColor(if (overspending) R.color.widget_overspending else R.color.widget_accent),
        )
    }

    private fun messageFor(state: SpendingWidgetState): Int =
        when (state) {
            SpendingWidgetState.SignedOut -> R.string.widget_sign_in
            SpendingWidgetState.SetupRequired -> R.string.widget_setup_required
            SpendingWidgetState.PremiumRequired -> R.string.widget_premium_required
            SpendingWidgetState.Unavailable -> R.string.widget_unavailable
            is SpendingWidgetState.Ready -> error("Ready state displays amounts")
        }

    private fun openApp(context: Context): PendingIntent {
        val intent =
            Intent(context, MainActivity::class.java)
                .setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private fun refresh(context: Context): PendingIntent {
        val intent =
            Intent(context, SpendingWidgetProvider::class.java)
                .setAction(SpendingWidgetProvider.refreshAction(context))
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }
}
