package com.d9tilov.moneymanager.regular.ui.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.core.util.toModInt
import com.d9tilov.moneymanager.regular.ui.notification.TransactionNotificationBuilder.Companion.EXTRA_ACTION_ID
import com.d9tilov.moneymanager.regular.ui.notification.TransactionNotificationBuilder.Companion.EXTRA_ACTION_TRANSACTION
import com.d9tilov.moneymanager.regular.ui.notification.TransactionNotificationBuilder.Companion.EXTRA_ACTION_TYPE
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class TransactionActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var transactionInteractor: TransactionInteractor

    override fun onReceive(context: Context, intent: Intent?) {
        intent?.run {
            val actionType =
                requireNotNull(
                    extras?.getParcelable<ActionType>(EXTRA_ACTION_TYPE),
                    { "Action type must't be null" }
                )
            val action = if (actionType == ActionType.BUTTON) {
                action!!
            } else {
                actionType.name
            }
            Timber.tag(App.TAG).d("Handle action: $actionType")
            val transaction =
                extras?.getParcelable<Transaction>(EXTRA_ACTION_TRANSACTION)!!
            val id = extras?.getLong(EXTRA_ACTION_ID)!!
            Timber.tag(App.TAG).d("Handle regular transaction: $transaction")
            val actionTitle = extras?.getString(action)
            Timber.tag(App.TAG).d("Handle actionTitle: $actionTitle")
            when (actionType) {
                ActionType.BUTTON -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        transactionInteractor.addTransaction(transaction)
                    }
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(id.toModInt())
                }
                ActionType.DEEP_LINK -> {
                    val clickIntent = generateIntent(context)
                    val contentIntent =
                        PendingIntent.getActivity(
                            context,
                            Random.nextInt(0, Integer.MAX_VALUE),
                            clickIntent,
                            0
                        )
                    contentIntent.send()
                }
            }
        }
    }

    private fun generateIntent(context: Context): Intent? {
        val packageName = context.packageName
        return context.packageManager.getLaunchIntentForPackage(packageName)
    }
}
