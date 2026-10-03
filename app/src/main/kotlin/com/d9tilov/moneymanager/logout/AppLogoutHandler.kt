package com.d9tilov.moneymanager.logout

import android.app.Application
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
    }
}
