package com.d9tilov.moneymanager.sync.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.google.samples.apps.nowinandroid.sync.initializers.SyncConstraints
import com.google.samples.apps.nowinandroid.sync.initializers.syncForegroundInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val currencyInteractor: CurrencyInteractor
) : CoroutineWorker(context, workerParameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        context.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        // First sync the repositories in parallel
        val syncedSuccessfully = awaitAll(
            async { currencyInteractor.updateCurrencyRates() }
        ).all { it }

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
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
