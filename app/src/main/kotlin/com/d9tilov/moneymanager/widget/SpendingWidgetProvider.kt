package com.d9tilov.moneymanager.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class SpendingWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        SpendingWidgetWork.schedule(context)
    }

    override fun onDisabled(context: Context) {
        SpendingWidgetWork.cancel(context)
    }

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        super.onReceive(context, intent)
        when (intent.action) {
            refreshAction(context), Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_LOCALE_CHANGED, Intent.ACTION_MY_PACKAGE_REPLACED,
            -> SpendingWidgetWork.schedule(context)
        }
    }

    companion object {
        fun widgetIds(context: Context): IntArray =
            AppWidgetManager.getInstance(context).getAppWidgetIds(
                ComponentName(context, SpendingWidgetProvider::class.java),
            )

        fun hasWidgets(context: Context): Boolean = widgetIds(context).isNotEmpty()

        fun refreshAction(context: Context): String = "${context.packageName}.REFRESH_SPENDING_WIDGET"
    }
}
