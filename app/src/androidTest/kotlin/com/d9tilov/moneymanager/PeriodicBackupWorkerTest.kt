package com.d9tilov.moneymanager

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.domain.model.BackupData
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import java.io.IOException

class PeriodicBackupWorkerTest {
    @Test
    fun successfulBackupCompletesWithoutForegroundService() =
        runTest {
            assertEquals(ListenableWorker.Result.success(), worker(ResultOf.Success(BackupData.EMPTY)).doWork())
        }

    @Test
    fun failedUploadIsRetried() =
        runTest {
            assertEquals(ListenableWorker.Result.retry(), worker(ResultOf.Failure(IOException())).doWork())
        }

    @Test
    fun retriesAreBounded() =
        runTest {
            assertEquals(
                ListenableWorker.Result.failure(),
                worker(ResultOf.Failure(IOException()), attempts = 3).doWork(),
            )
        }

    @Test
    fun missingAccountDoesNotRetry() =
        runTest {
            assertEquals(ListenableWorker.Result.failure(), worker(ResultOf.Failure(WrongUidException())).doWork())
        }

    @Test
    fun cancellationIsPropagated() =
        runTest {
            val cancellation = CancellationException("Worker stopped")
            val result = runCatching { worker(ResultOf.Failure(cancellation)).doWork() }
            assertSame(cancellation, result.exceptionOrNull())
        }

    private fun worker(
        result: ResultOf<BackupData>,
        attempts: Int = 0,
    ): PeriodicBackupWorker {
        val interactor =
            object : BackupInteractor {
                override fun getBackupData() = flowOf(BackupData.EMPTY)

                override suspend fun makeBackup() = result

                override suspend fun restoreBackup(): ResultOf<Long> = error("Unexpected restore")

                override suspend fun deleteBackup(): ResultOf<Any> = error("Unexpected delete")
            }
        val factory =
            object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters,
                ): ListenableWorker = PeriodicBackupWorker(appContext, workerParameters, interactor)
            }
        return TestListenableWorkerBuilder<PeriodicBackupWorker>(
            InstrumentationRegistry.getInstrumentation().targetContext,
        ).setWorkerFactory(factory)
            .setRunAttemptCount(attempts)
            .build()
    }
}
