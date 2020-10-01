package com.d9tilov.moneymanager.core.util

import android.os.Looper
import androidx.annotation.RestrictTo
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val networkScheduler: Scheduler = Schedulers.from(Executors.newFixedThreadPool(1))
val uiScheduler: Scheduler = AndroidSchedulers.mainThread()
val ioScheduler: Scheduler = Schedulers.io()
val newThreadScheduler: Scheduler = Schedulers.newThread()
fun getSchedulerFrom(executor: Executor) = Schedulers.from(executor)

@get:RestrictTo(RestrictTo.Scope.LIBRARY)
internal inline val isMainThread: Boolean
    get() = Looper.myLooper() == Looper.getMainLooper()

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun checkIsMainThread() = check(isMainThread)
