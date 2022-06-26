package com.d9tilov.moneymanager.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.google.firebase.FirebaseException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltWorker
class PeriodicBackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var backupInteractor: BackupInteractor

    override suspend fun doWork(): Result {
        Timber.tag(App.TAG).d("Do work...")
        return try {
            backupInteractor.makeBackup()
            Timber.tag(App.TAG).d("Do work with success")
            Result.success()
        } catch (ex: NetworkException) {
            Timber.tag(App.TAG).d("Do work with network exception: $ex")
            Result.failure()
        } catch (ex: WrongUidException) {
            Timber.tag(App.TAG).d("Do work with wrong uid exception: $ex")
            Result.failure()
        } catch (ex: FileNotFoundException) {
            Timber.tag(App.TAG).d("Do work with file not found error: $ex")
            Result.failure()
        } catch (ex: FirebaseException) {
            Timber.tag(App.TAG).d("Do work with exception: $ex")
            Result.failure()
        }
    }

    companion object {
        private const val LOGIN_WORK_TAG = "periodic_backup"
        private const val PERIOD_WORK_IN_HOURS = 24L

        fun startPeriodicJob(context: Context) {
            Timber.tag(App.TAG).d("Start backup periodic job")
            val recurringWork = PeriodicWorkRequest
                .Builder(
                    PeriodicBackupWorker::class.java,
                    PERIOD_WORK_IN_HOURS,
                    TimeUnit.HOURS
                )
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
