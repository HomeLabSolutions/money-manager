package com.d9tilov.android.transaction.regular.data.impl.notification

import android.app.PendingIntent
import android.widget.RemoteViews
import com.d9tilov.android.notification.AndroidNotificationData
import com.d9tilov.android.notification.domain.NotificationData

class RegularTransactionNotificationData(
    data: NotificationData,
    override val contentIntent: PendingIntent,
    override val customContentView: RemoteViews,
) : AndroidNotificationData(data)
