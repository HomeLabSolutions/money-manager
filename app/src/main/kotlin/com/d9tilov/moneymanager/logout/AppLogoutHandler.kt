package com.d9tilov.moneymanager.logout

import android.app.Application
import android.content.Intent
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.common.android.ui.logout.LogoutHandler
import com.d9tilov.android.transaction.regular.data.impl.worker.RegularTransactionSyncWorker
import com.d9tilov.moneymanager.insights.WeeklyInsightNotification
import javax.inject.Inject

class AppLogoutHandler @Inject constructor(
    private val application: Application,
    private val notification: WeeklyInsightNotification,
) : LogoutHandler {
    override fun onLogout() {
        PeriodicBackupWorker.stopPeriodicJob(application)
        RegularTransactionSyncWorker.stopPeriodicJob(application)
        notification.cancel()
        val intent = application.packageManager.getLaunchIntentForPackage(application.packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            application.startActivity(intent)
        }
    }
}
