package com.d9tilov.android.backup.data.impl

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.common.android.worker.DelegatingWorker
import com.d9tilov.android.common.android.worker.SyncConstraints
import com.d9tilov.android.common.android.worker.delegatedData
import com.d9tilov.android.common.android.worker.syncForegroundInfo
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.network.exception.NetworkException
import com.google.firebase.FirebaseException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit

@HiltWorker
class PeriodicBackupWorker
    @AssistedInject
    constructor(
        @Assisted private val context: Context,
        @Assisted workerParameters: WorkerParameters,
        private val backupInteractor: BackupInteractor,
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

        override suspend fun doWork(): Result {
            Timber.tag(TAG).d("Do work...")
            return try {
                setForeground(context.syncForegroundInfo())
                backupInteractor.makeBackup()
                Timber.tag(TAG).d("Do work with success")
                Result.success()
            } catch (ex: NetworkException) {
                Timber.tag(TAG).d("Do work with network exception: $ex")
                Result.failure()
            } catch (ex: WrongUidException) {
                Timber.tag(TAG).d("Do work with wrong uid exception: $ex")
                Result.failure()
            } catch (ex: FileNotFoundException) {
                Timber.tag(TAG).d("Do work with file not found error: $ex")
                Result.failure()
            } catch (ex: FirebaseException) {
                Timber.tag(TAG).d("Do work with exception: $ex")
                Result.failure()
            }
        }

        companion object {
            private const val BACKUP_WORK_NAME = "backup_work_name"
            private const val LOGIN_WORK_TAG = "periodic_backup"
            private const val PERIOD_WORK_IN_HOURS = 24L

            fun startPeriodicJob(context: Context) {
                Timber.tag(TAG).d("Start backup periodic job")
                val recurringWork =
                    PeriodicWorkRequest
                        .Builder(
                            DelegatingWorker::class.java,
                            PERIOD_WORK_IN_HOURS,
                            TimeUnit.HOURS,
                        ).addTag(LOGIN_WORK_TAG)
                        .setConstraints(SyncConstraints)
                        .setInputData(PeriodicBackupWorker::class.delegatedData())
                        .build()

                val workManager = WorkManager.getInstance(context)
                workManager.enqueueUniquePeriodicWork(
                    BACKUP_WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    recurringWork,
                )
            }

            fun stopPeriodicJob(context: Context) {
                Timber.tag(TAG).d("Stop periodic job")
                WorkManager.getInstance(context).cancelAllWorkByTag(LOGIN_WORK_TAG)
            }
        }
    }
