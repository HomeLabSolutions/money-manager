package com.d9tilov.android.common.android.worker

import androidx.work.WorkerFactory

interface WorkerFactoryOwner {
    val workerFactory: WorkerFactory
}
