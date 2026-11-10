package com.d9tilov.android.common.android.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import kotlin.reflect.KClass

private const val WORKER_CLASS_NAME = "RouterWorkerDelegateClassName2"

/**
 * Adds metadata to a WorkRequest to identify what [CoroutineWorker] the [DelegatingWorker] should
 * delegate to
 */
fun KClass<out CoroutineWorker>.delegatedData() = Data.Builder().putString(WORKER_CLASS_NAME, qualifiedName).build()

/**
 * Keeps persisted work requests compatible while resolving their concrete worker through Koin.
 */
class DelegatingWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    private val workerClassName = workerParams.inputData.getString(WORKER_CLASS_NAME) ?: ""

    private val delegateWorker by lazy {
        KoinWorkerFactory()
            .createWorker(appContext, workerClassName, workerParams) as? CoroutineWorker
            ?: throw IllegalArgumentException("Unable to find appropriate worker")
    }

    override suspend fun getForegroundInfo(): ForegroundInfo = delegateWorker.getForegroundInfo()

    override suspend fun doWork(): Result = delegateWorker.doWork()
}
