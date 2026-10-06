package com.d9tilov.android.notification

import android.app.PendingIntent
import android.widget.RemoteViews
import com.d9tilov.android.notification.domain.NotificationData

abstract class AndroidNotificationData(
    val data: NotificationData,
) {
    abstract val contentIntent: PendingIntent
    abstract val customContentView: RemoteViews?
}
