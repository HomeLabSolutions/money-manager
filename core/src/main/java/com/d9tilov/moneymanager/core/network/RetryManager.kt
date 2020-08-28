package com.d9tilov.moneymanager.core.network

import io.reactivex.subjects.PublishSubject

interface RetryManager {
    fun observeRetries(): PublishSubject<RetryEvent>
    fun retry()
}