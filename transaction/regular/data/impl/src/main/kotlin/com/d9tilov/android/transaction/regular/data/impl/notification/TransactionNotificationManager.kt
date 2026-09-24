package com.d9tilov.android.transaction.regular.data.impl.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.RemoteViews
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.impl.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TransactionNotificationManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        fun notifyAboutRegularTransactions(transactions: List<RegularTransaction>) {
            val notifiableTransactions = transactions.filter { it.pushEnabled }
            if (notifiableTransactions.isEmpty()) return

            val missingPermission =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    context.checkSelfPermission(
                        Manifest.permission.POST_NOTIFICATIONS,
                    ) != PackageManager.PERMISSION_GRANTED
            if (missingPermission) return

            val manager = context.getSystemService(NotificationManager::class.java)
            createNotificationChannel(manager)
            val launchIntent = checkNotNull(context.packageManager.getLaunchIntentForPackage(context.packageName))
            val contentIntent =
                PendingIntent.getActivity(
                    context,
                    0,
                    launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            val notificationTitle = context.getString(R.string.regular_transaction_notification_title)
            notifiableTransactions.forEach { transaction ->
                val transactionId = transaction.id.hashCode()
                val styledNotificationText = getNotificationText(transaction)
                val builder =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Notification.Builder(context, REGULAR_TRANSACTION_CHANNEL_ID)
                    } else {
                        Notification.Builder(context)
                    }
                val notificationContent =
                    RemoteViews(context.packageName, R.layout.regular_transaction_notification).apply {
                        setTextViewText(R.id.notification_title, notificationTitle)
                        setImageViewResource(R.id.category_icon, transaction.category.icon)
                        setTextViewText(R.id.notification_text, styledNotificationText)
                    }
                manager.notify(
                    transactionId,
                    builder
                        .setSmallIcon(com.d9tilov.android.common.android.R.drawable.ic_currency_icon)
                        .setContentTitle(notificationTitle)
                        .setContentText(styledNotificationText)
                        .setCustomContentView(notificationContent)
                        .setStyle(Notification.DecoratedCustomViewStyle())
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)
                        .build(),
                )
            }
        }

        private fun getNotificationText(transaction: RegularTransaction): CharSequence {
            val category = transaction.category.name

            return SpannableString(
                "$category: ${transaction.currencyCode.getSymbolByCode()}${transaction.sum.reduceScaleStr()}",
            ).apply {
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    category.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            }
        }

        private fun createNotificationChannel(manager: NotificationManager) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

            manager.createNotificationChannel(
                NotificationChannel(
                    REGULAR_TRANSACTION_CHANNEL_ID,
                    context.getString(R.string.regular_transaction_notification_channel),
                    NotificationManager.IMPORTANCE_DEFAULT,
                ),
            )
        }

        private companion object {
            const val REGULAR_TRANSACTION_CHANNEL_ID = "regular_transactions"
        }
    }
