package com.d9tilov.moneymanager.insights

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.home.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeeklyInsightNotification @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun cancel() {
        context.getSystemService(NotificationManager::class.java).cancel(NOTIFICATION_ID)
    }

    fun show(insight: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.weekly_insight_channel),
                    NotificationManager.IMPORTANCE_DEFAULT,
                ),
            )
        }
        val intent =
            Intent(context, MainActivity::class.java).apply {
                action = ACTION_OPEN_INSIGHTS
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        val message =
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setSmallIcon(com.d9tilov.android.common.android.R.drawable.ic_currency_icon)
                .setContentTitle(context.getString(R.string.weekly_insight_title))
                .setContentText(insight)
                .setStyle(NotificationCompat.BigTextStyle().bigText(insight))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        manager.notify(NOTIFICATION_ID, message)
    }

    companion object {
        const val ACTION_OPEN_INSIGHTS = "com.d9tilov.moneymanager.OPEN_INSIGHTS"
        private const val CHANNEL_ID = "weekly_insights"
        private const val NOTIFICATION_ID = 501
    }
}
