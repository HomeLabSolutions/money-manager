package com.d9tilov.android.backup.data.impl

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class PeriodicBackupWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted workerParameters: WorkerParameters,
        private val backupInteractor: BackupInteractor,
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result {
            Timber.tag(TAG).d("Do work...")
            return when (val result = backupInteractor.makeBackup()) {
                is ResultOf.Failure -> {
                    val error = result.throwable
                    if (error is CancellationException) throw error
                    Timber.tag(TAG).w(error, "Automatic backup failed; attempt %s", runAttemptCount + 1)
                    if (error is WrongUidException || runAttemptCount >= MAX_RETRIES) {
                        Result.failure()
                    } else {
                        Result.retry()
                    }
                }
                is ResultOf.Loading -> {
                    if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
                }
                is ResultOf.Success -> {
                    Result.success()
                }
            }
        }

        companion object {
            private const val BACKUP_WORK_NAME = "backup_work_name"
            private const val LOGIN_WORK_TAG = "periodic_backup"
            private const val PERIOD_WORK_IN_HOURS = 24L
            private const val MAX_RETRIES = 3
            private const val RETRY_DELAY_MINUTES = 15L

            fun startPeriodicJob(context: Context) {
                Timber.tag(TAG).d("Start backup periodic job")
                val recurringWork =
                    PeriodicWorkRequest
                        .Builder(
                            DelegatingWorker::class.java,
                            PERIOD_WORK_IN_HOURS,
                            TimeUnit.HOURS,
                        ).addTag(LOGIN_WORK_TAG)
                        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, RETRY_DELAY_MINUTES, TimeUnit.MINUTES)
                        .setConstraints(SyncConstraints)
                        .setInputData(PeriodicBackupWorker::class.delegatedData())
                        .build()

                val workManager = WorkManager.getInstance(context)
                workManager.enqueueUniquePeriodicWork(
                    BACKUP_WORK_NAME,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    recurringWork,
                )
            }

            fun stopPeriodicJob(context: Context) {
                Timber.tag(TAG).d("Stop periodic job")
                WorkManager.getInstance(context).cancelAllWorkByTag(LOGIN_WORK_TAG)
            }
        }
    }
