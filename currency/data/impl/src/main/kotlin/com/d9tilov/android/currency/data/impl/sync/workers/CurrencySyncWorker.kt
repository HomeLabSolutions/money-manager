package com.d9tilov.android.currency.data.impl.sync.workers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.d9tilov.android.common.android.worker.ChildWorkerFactory
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.WorkerKey
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.common.android.worker.syncForegroundInfo
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AssistedInject
class CurrencySyncWorker(
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

    @AssistedFactory
    @WorkerKey(CurrencySyncWorker::class)
    @ContributesIntoMap(AppScope::class, binding<ChildWorkerFactory>())
    fun interface Factory : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters,
        ): CurrencySyncWorker
    }
}
