package com.d9tilov.android.insights.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.model.WeeklyInsightNotification
import com.d9tilov.android.insights.domain.model.exception.DailyInsightLimitException
import com.d9tilov.android.insights.domain.model.exception.InsightServiceException
import com.d9tilov.android.insights.domain.model.exception.InsufficientInsightsDataException
import com.d9tilov.android.insights.presentation.notification.WeeklyInsightNotificationSender
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class WeeklyInsightWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val insightsInteractor: InsightsInteractor,
    private val notification: WeeklyInsightNotificationSender,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        if (!insightsInteractor.isConsentGranted()) return Result.success()
        return try {
            val insight = insightsInteractor.generate()
            notification.send(WeeklyInsightNotification(text = insight.text))
            Result.success()
        } catch (_: InsufficientInsightsDataException) {
            Result.failure()
        } catch (_: DailyInsightLimitException) {
            Result.failure()
        } catch (error: InsightServiceException) {
            Timber.w(error, "Unable to generate weekly insight")
            if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
        }
    }

    companion object {
        private const val WORK_NAME = "weekly_insight_after_consent"
        private const val INTERVAL_DAYS = 5L
        private const val MAX_RETRIES = 3
        private const val RETRY_DELAY_MINUTES = 15L

        fun startPeriodicJob(context: Context) {
            val recurringWork =
                PeriodicWorkRequest
                    .Builder(
                        DelegatingWorker::class.java,
                        INTERVAL_DAYS,
                        TimeUnit.DAYS,
                    ).addTag(WORK_NAME)
                    .setInitialDelay(INTERVAL_DAYS, TimeUnit.DAYS)
                    .setConstraints(SyncConstraints)
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, RETRY_DELAY_MINUTES, TimeUnit.MINUTES)
                    .setInputData(WeeklyInsightWorker::class.delegatedData())
                    .build()
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                recurringWork,
            )
        }

        fun stopPeriodicJob(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_NAME)
        }
    }
}
