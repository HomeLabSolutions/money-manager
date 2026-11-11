package com.d9tilov.android.insights.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.android.common.android.worker.ChildWorkerFactory
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.WorkerKey
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.common.android.worker.syncForegroundInfo
import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.model.WeeklyInsightNotification
import com.d9tilov.android.insights.domain.model.exception.DailyInsightLimitException
import com.d9tilov.android.insights.domain.model.exception.InsufficientInsightsDataException
import com.d9tilov.android.insights.presentation.notification.WeeklyInsightNotificationSender
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AssistedInject
class WeeklyInsightWorker(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val insightsInteractor: InsightsInteractor,
    private val notification: WeeklyInsightNotificationSender,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result {
        if (!insightsInteractor.isConsentGranted()) return Result.success()
        return try {
            val insight = insightsInteractor.generate()
            notification.send(WeeklyInsightNotification(text = insight.text))
            Result.success()
        } catch (error: CancellationException) {
            throw error
        } catch (_: InsufficientInsightsDataException) {
            Result.failure()
        } catch (_: DailyInsightLimitException) {
            Result.failure()
        } catch (error: Exception) {
            Timber.w(error, "Unable to generate weekly insight")
            Result.failure()
        }
    }

    companion object {
        private const val WORK_NAME = "weekly_insight_after_consent"
        private const val INTERVAL_DAYS = 5L

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

    @AssistedFactory
    @WorkerKey(WeeklyInsightWorker::class)
    @ContributesIntoMap(AppScope::class, binding<ChildWorkerFactory>())
    fun interface Factory : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters,
        ): WeeklyInsightWorker
    }
}
