package com.d9tilov.moneymanager.core.util

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
