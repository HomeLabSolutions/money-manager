package com.d9tilov.android.common.android.worker

import androidx.work.ListenableWorker
import dev.zacsweers.metro.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(
    val value: KClass<out ListenableWorker>,
)
