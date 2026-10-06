package com.d9tilov.android.insights.presentation.notification

import android.app.PendingIntent
import android.widget.RemoteViews
import com.d9tilov.android.notification.AndroidNotificationData
import com.d9tilov.android.notification.domain.NotificationData

class WeeklyInsightNotificationData(
    data: NotificationData,
    override val contentIntent: PendingIntent,
) : AndroidNotificationData(data) {
    override val customContentView: RemoteViews? = null
}
