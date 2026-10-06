package com.d9tilov.android.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.d9tilov.android.notification.domain.MoneyManagerNotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MoneyManagerNotificationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MoneyManagerNotificationManager<AndroidNotificationData> {
    override fun notify(
        id: Int,
        notification: AndroidNotificationData,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val data = notification.data
        createChannel(data.channelId, data.channelName)
        val builder =
            createBuilder(data.channelId)
                .setContentTitle(data.title)
                .setContentText(data.text)
                .setContentIntent(notification.contentIntent)
        if (notification.customContentView != null) {
            builder
                .setCustomContentView(notification.customContentView)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        } else {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(data.text))
        }
        NotificationManagerCompat.from(context).notify(id, builder.build())
    }

    override fun cancelAll() {
        NotificationManagerCompat.from(context).cancelAll()
    }

    private fun createChannel(
        id: String,
        name: String,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat.from(context).createNotificationChannel(
                NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT),
            )
        }
    }

    private fun createBuilder(channelId: String): NotificationCompat.Builder =
        NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(com.d9tilov.android.common.android.R.drawable.ic_currency_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
}
