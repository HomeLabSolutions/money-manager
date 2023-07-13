package com.d9tilov.android.currency.data.impl.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.data.impl.sync.initializers.SyncConstraints
import com.d9tilov.android.currency.data.impl.sync.initializers.syncForegroundInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class CurrencySyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val currencyInteractor: CurrencyInteractor
) : CoroutineWorker(context, workerParameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        context.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Timber.tag(DataConstants.TAG).d("CurrencySyncWorker doWork")
        // First sync the repositories in parallel
        val syncedSuccessfully = awaitAll(async { currencyInteractor.updateCurrencyRates() })
            .all { it }

        if (syncedSuccessfully) Result.success()
        else Result.retry()
    }

    companion object {
        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(CurrencySyncWorker::class.delegatedData())
            .build()
    }
}
