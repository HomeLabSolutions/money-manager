package com.d9tilov.android.common.android.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.reflect.KClass

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class MetroWorkerFactory(
    private val workerFactories: Map<KClass<out ListenableWorker>, () -> ChildWorkerFactory>,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? =
        workerFactories.entries
            .firstOrNull { (workerClass, _) -> workerClass.java.name == workerClassName }
            ?.value
            ?.invoke()
            ?.create(appContext, workerParameters)
}
