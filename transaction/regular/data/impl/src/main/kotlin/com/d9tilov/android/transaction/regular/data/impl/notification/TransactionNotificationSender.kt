package com.d9tilov.android.transaction.regular.data.impl.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.notification.MoneyManagerNotificationManager
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.impl.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TransactionNotificationSender
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val notificationManager: MoneyManagerNotificationManager,
    ) {
        fun notifyAboutRegularTransactions(transactions: List<RegularTransaction>) {
            val notifiableTransactions = transactions.filter { it.pushEnabled }
            if (notifiableTransactions.isEmpty()) return

            notificationManager.createChannel(
                REGULAR_TRANSACTION_CHANNEL_ID,
                context.getString(R.string.regular_transaction_notification_channel),
            )
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
                val notificationContent =
                    RemoteViews(context.packageName, R.layout.regular_transaction_notification).apply {
                        setTextViewText(R.id.notification_title, notificationTitle)
                        setImageViewResource(R.id.category_icon, transaction.category.icon)
                        setTextViewText(R.id.notification_text, styledNotificationText)
                    }
                notificationManager.notify(
                    transactionId,
                    notificationManager
                        .createBuilder(REGULAR_TRANSACTION_CHANNEL_ID)
                        .setContentTitle(notificationTitle)
                        .setContentText(styledNotificationText)
                        .setCustomContentView(notificationContent)
                        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                        .setContentIntent(contentIntent),
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

        private companion object {
            const val REGULAR_TRANSACTION_CHANNEL_ID = "regular_transactions"
        }
    }
