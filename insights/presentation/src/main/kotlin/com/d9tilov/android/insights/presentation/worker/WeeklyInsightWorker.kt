package com.d9tilov.android.insights.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.common.android.worker.syncForegroundInfo
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.insights.domain.DailyInsightLimitException
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import com.d9tilov.android.insights.domain.InsightsRepository
import com.d9tilov.android.insights.domain.InsufficientInsightsDataException
import com.d9tilov.android.insights.domain.NoNewInsightException
import com.d9tilov.android.insights.domain.WeeklyInsightNotification
import com.d9tilov.android.insights.presentation.notification.WeeklyInsightNotificationSender
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.Locale
import java.util.concurrent.TimeUnit

@HiltWorker
class WeeklyInsightWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val preferencesStore: PreferencesStore,
    private val consentRepository: InsightsConsentRepository,
    private val insightsRepository: InsightsRepository,
    private val notification: WeeklyInsightNotificationSender,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result {
        val uid = preferencesStore.uid.first()
        if (uid == null || FirebaseAuth.getInstance().currentUser?.uid != uid || !consentRepository.isGranted.first()) {
            return Result.success()
        }
        return try {
            val insight = insightsRepository.generate(Locale.getDefault().toLanguageTag())
            notification.send(WeeklyInsightNotification(text = insight))
            Result.success()
        } catch (error: CancellationException) {
            throw error
        } catch (_: InsufficientInsightsDataException) {
            Result.success()
        } catch (_: DailyInsightLimitException) {
            Result.success()
        } catch (_: NoNewInsightException) {
            Result.success()
        } catch (error: Exception) {
            Timber.w(error, "Unable to generate weekly insight")
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "weekly_insight_after_consent"
        private const val INTERVAL_DAYS = 7L

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
}
