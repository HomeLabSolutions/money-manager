package com.d9tilov.moneymanager.insights

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.d9tilov.android.insights.presentation.navigation.INSIGHTS_DEEP_LINK_URI
import com.d9tilov.android.notification.MoneyManagerNotificationManager
import com.d9tilov.android.notification.NotificationSender
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.home.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeeklyInsightNotificationSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: MoneyManagerNotificationManager,
) : NotificationSender<WeeklyInsightNotification> {
    override fun send(data: WeeklyInsightNotification) {
        notificationManager.createChannel(CHANNEL_ID, context.getString(R.string.weekly_insight_channel))
        val intent =
            Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                this.data = INSIGHTS_DEEP_LINK_URI.toUri()
            }
        val pendingIntent =
            checkNotNull(
                TaskStackBuilder
                    .create(context)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(
                        NOTIFICATION_ID,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                    ),
            )
        val message =
            notificationManager
                .createBuilder(CHANNEL_ID)
                .setContentTitle(context.getString(R.string.weekly_insight_title))
                .setContentText(data.text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(data.text))
                .setContentIntent(pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, message)
    }

    private companion object {
        const val CHANNEL_ID = "weekly_insights"
        const val NOTIFICATION_ID = 501
    }
}
