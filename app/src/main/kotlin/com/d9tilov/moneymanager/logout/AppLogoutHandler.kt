package com.d9tilov.moneymanager.logout

import android.app.Application
import android.content.Intent
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.common.android.ui.logout.LogoutHandler
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.transaction.regular.data.impl.worker.RegularTransactionSyncWorker
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.moneymanager.insights.WeeklyInsightNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class AppLogoutHandler @Inject constructor(
    private val application: Application,
    private val notification: WeeklyInsightNotification,
    private val userInfoInteractor: UserInteractor,
    private val preferencesStore: PreferencesStore,
    private val analyticsSender: AnalyticsSender,
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
) : LogoutHandler {
    override suspend fun onLogout() {
        withContext(ioDispatcher) {
            userInfoInteractor.deleteUser()
            preferencesStore.clearAllData()
            analyticsSender.send(
                AnalyticsEvent.Client.Auth,
                mapOf(AnalyticsParams.Auth.Action to "logout"),
            )
            PeriodicBackupWorker.stopPeriodicJob(application)
            RegularTransactionSyncWorker.stopPeriodicJob(application)
            notification.cancel()
        }
        withContext(Dispatchers.Main) {
            val intent = application.packageManager.getLaunchIntentForPackage(application.packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                application.startActivity(intent)
            }
        }
    }
}
