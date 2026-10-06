package com.d9tilov.android.insights.presentation.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.d9tilov.android.insights.domain.WeeklyInsightNotification
import com.d9tilov.android.insights.presentation.R
import com.d9tilov.android.insights.presentation.navigation.INSIGHTS_DEEP_LINK_URI
import com.d9tilov.android.notification.AndroidNotificationData
import com.d9tilov.android.notification.domain.MoneyManagerNotificationManager
import com.d9tilov.android.notification.domain.NotificationData
import com.d9tilov.android.notification.domain.NotificationSender
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeeklyInsightNotificationSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: MoneyManagerNotificationManager<AndroidNotificationData>,
) : NotificationSender<WeeklyInsightNotification> {
    override fun send(data: WeeklyInsightNotification) {
        val intent =
            checkNotNull(context.packageManager.getLaunchIntentForPackage(context.packageName)).apply {
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
        notificationManager.notify(
            NOTIFICATION_ID,
            WeeklyInsightNotificationData(
                data =
                    NotificationData(
                        channelId = CHANNEL_ID,
                        channelName = context.getString(R.string.weekly_insight_channel),
                        title = context.getString(R.string.weekly_insight_title),
                        text = data.text,
                    ),
                contentIntent = pendingIntent,
            ),
        )
    }

    private companion object {
        const val CHANNEL_ID = "weekly_insights"
        const val NOTIFICATION_ID = 501
    }
}
