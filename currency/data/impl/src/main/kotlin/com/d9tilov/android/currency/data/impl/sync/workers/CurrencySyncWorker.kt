package com.d9tilov.android.currency.data.impl.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.common.android.worker.syncForegroundInfo
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class CurrencySyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val currencyInteractor: CurrencyInteractor,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            Timber.tag(DataConstants.TAG).d("CurrencySyncWorker doWork")
            // First sync the repositories in parallel
            val res =
                currencyInteractor
                    .updateCurrencyRates()
                    .onSuccess { Timber.tag(DataConstants.TAG).d("CurrencySyncWorker doWork success") }
                    .onFailure { Timber.tag(DataConstants.TAG).d("CurrencySyncWorker doWork failure: $it") }
            if (res.isSuccess) {
                Result.success()
            } else {
                val error = res.exceptionOrNull()
                if (error is CancellationException) throw error
                if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
            }
        }

    companion object {
        private const val MAX_RETRIES = 3
        private const val RETRY_DELAY_MINUTES = 15L

        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() =
            OneTimeWorkRequestBuilder<DelegatingWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(SyncConstraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, RETRY_DELAY_MINUTES, TimeUnit.MINUTES)
                .setInputData(CurrencySyncWorker::class.delegatedData())
                .build()
    }
}
