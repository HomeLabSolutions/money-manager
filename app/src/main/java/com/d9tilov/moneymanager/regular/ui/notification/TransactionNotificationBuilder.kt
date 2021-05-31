package com.d9tilov.moneymanager.regular.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.util.toModInt
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import kotlin.random.Random

class TransactionNotificationBuilder(private val context: Context) {

    fun fireNotification(transaction: Transaction, id: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(transaction)
        }
        var channelId = ""
        val title: String
        val notificationAction: NotificationAction
        when (transaction.type) {
            TransactionType.INCOME -> {
                channelId = CHANNEL_INCOME_ID
                title = context.getString(R.string.notification_income_title)
                notificationAction = NotificationAction(
                    ACTION_INCOME,
                    context.getString(R.string.notification_action_income_title)
                )
            }
            TransactionType.EXPENSE -> {
                channelId = CHANNEL_EXPENSE_ID
                title = context.getString(R.string.notification_expense_title)
                notificationAction = NotificationAction(
                    ACTION_EXPENSE,
                    context.getString(R.string.notification_action_expense_title)
                )
            }
        }
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
        notificationBuilder
            .setContentTitle(title)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentText(transaction.category.name + " " + transaction.sum)

        val actionClickIntent = Intent(context, TransactionActionReceiver::class.java)
            .putExtra(EXTRA_ACTION_TYPE, ActionType.BUTTON)
            .putExtra(EXTRA_ACTION_TRANSACTION, transaction)
            .putExtra(EXTRA_ACTION_ID, id)
            .putExtra(notificationAction.action, notificationAction.title)
            .setAction(notificationAction.action)
        val actionPendingIntent = PendingIntent.getBroadcast(
            context,
            Random.nextInt(0, Integer.MAX_VALUE),
            actionClickIntent,
            0
        )
        val clickAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            notificationAction.title, actionPendingIntent
        ).build()
        notificationBuilder.addAction(clickAction)

        val intent = Intent(context, TransactionActionReceiver::class.java)
            .putExtra(EXTRA_ACTION_TYPE, ActionType.DEEP_LINK)
            .putExtra(EXTRA_ACTION_TRANSACTION, transaction)
            .putExtra(EXTRA_ACTION_ID, id)
        val contentIntent = PendingIntent.getBroadcast(
            context,
            Random.nextInt(0, Integer.MAX_VALUE),
            intent,
            0
        )
        notificationBuilder.setContentIntent(contentIntent)
        val notification = notificationBuilder
            .setPriority(PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id.toModInt(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(transaction: Transaction) {
        val channelId = when (transaction.type) {
            TransactionType.INCOME -> CHANNEL_INCOME_ID
            TransactionType.EXPENSE -> CHANNEL_EXPENSE_ID
        }
        val channelName = when (transaction.type) {
            TransactionType.INCOME -> context.getString(R.string.notification_channel_income_name)
            TransactionType.EXPENSE -> context.getString(R.string.notification_channel_expense_name)
        }
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val ACTION_INCOME = "action_income"
        const val ACTION_EXPENSE = "action_expense"
        const val EXTRA_ACTION_TYPE = "action_type"
        const val EXTRA_ACTION_ID = "action_id"
        const val EXTRA_ACTION_TRANSACTION = "action_transaction"
        private const val CHANNEL_EXPENSE_ID = "id_expense"
        private const val CHANNEL_INCOME_ID = "id_income"
    }
}
