package com.d9tilov.android.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MoneyManagerNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun createChannel(
        id: String,
        name: String,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat.from(context).createNotificationChannel(
                NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT),
            )
        }
    }

    fun createBuilder(channelId: String): NotificationCompat.Builder =
        NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(com.d9tilov.android.common.android.R.drawable.ic_currency_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

    fun notify(
        id: Int,
        builder: NotificationCompat.Builder,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(id, builder.build())
    }

    fun cancelAll() {
        NotificationManagerCompat.from(context).cancelAll()
    }
}
