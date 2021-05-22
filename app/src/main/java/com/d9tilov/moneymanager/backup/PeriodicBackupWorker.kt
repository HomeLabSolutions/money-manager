package com.d9tilov.moneymanager.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.data.Status
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class PeriodicBackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val userInteractor: UserInteractor
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Timber.tag(App.TAG).d("Do work...")
        val result = userInteractor.backup()
        return if (result.status == Status.SUCCESS) {
            Timber.tag(App.TAG).d("Do work with success")
            Result.success()
        } else {
            Timber.tag(App.TAG).d("Do work with error ${result.message}")
            Result.failure()
        }
    }

    companion object {
        private const val LOGIN_WORK_TAG = "periodic_backup"
        private const val PERIOD_WORK_IN_HOURS = 20L

        fun startPeriodicJob(context: Context) {
            Timber.tag(App.TAG).d("Start periodic job")
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val recurringWork = PeriodicWorkRequest
                .Builder(
                    PeriodicBackupWorker::class.java,
                    PERIOD_WORK_IN_HOURS, TimeUnit.HOURS
                )
                .setConstraints(constraints)
                .addTag(LOGIN_WORK_TAG)
                .build()

            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniquePeriodicWork(
                PeriodicBackupWorker::class.java.name,
                ExistingPeriodicWorkPolicy.KEEP,
                recurringWork
            )
        }

        fun stopPeriodicJob(context: Context) {
            Timber.tag(App.TAG).d("Stop periodic job")
            WorkManager.getInstance(context).cancelAllWorkByTag(LOGIN_WORK_TAG)
        }
    }
}
