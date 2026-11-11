package com.d9tilov.android.common.android.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

fun interface ChildWorkerFactory {
    fun create(
        context: Context,
        workerParameters: WorkerParameters,
    ): ListenableWorker
}
