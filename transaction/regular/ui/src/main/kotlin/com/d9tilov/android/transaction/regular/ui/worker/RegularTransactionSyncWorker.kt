package com.d9tilov.android.transaction.regular.ui.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.common.android.worker.syncForegroundInfo
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class RegularTransactionSyncWorker
@AssistedInject
constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val transactionInteractor: TransactionInteractor,
    private val billingInteractor: BillingInteractor,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

    override suspend fun doWork(): Result {
        billingInteractor.billingConnectionReady
            .combine(
                billingInteractor
                    .isPremium()
            ) { isReady, isPremium ->
                isReady && isPremium
            }.collect { readyForPremium ->
                if (readyForPremium) {
                    transactionInteractor.executeRegularIfNeeded(TransactionType.INCOME)
                    transactionInteractor.executeRegularIfNeeded(TransactionType.EXPENSE)
                }
            }
        return Result.success()
    }

    companion object {
        private const val REGULAR_TRANSACTION_SYNC_WORK_NAME = "regular_transaction_work_name"
        private const val LOGIN_WORK_TAG = "periodic_backup"
        private const val PERIOD_WORK_IN_HOURS = 24L

        fun startPeriodicJob(context: Context) {
            Timber.tag(DataConstants.TAG).d("Start backup periodic job")
            val recurringWork =
                PeriodicWorkRequest
                    .Builder(
                        DelegatingWorker::class.java,
                        PERIOD_WORK_IN_HOURS,
                        TimeUnit.HOURS,
                    ).addTag(LOGIN_WORK_TAG)
                    .setConstraints(SyncConstraints)
                    .setInputData(RegularTransactionSyncWorker::class.delegatedData())
                    .build()

            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniquePeriodicWork(
                REGULAR_TRANSACTION_SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                recurringWork,
            )
        }

        fun stopPeriodicJob(context: Context) {
            Timber.tag(DataConstants.TAG).d("Stop periodic job")
            WorkManager.getInstance(context).cancelAllWorkByTag(LOGIN_WORK_TAG)
        }
    }
}