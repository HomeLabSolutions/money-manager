package com.d9tilov.android.transaction.domain.impl

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionMutationMutex
    @Inject
    constructor() {
        private val mutex = Mutex()

        suspend fun <T> withLock(action: suspend () -> T): T = mutex.withLock { action() }
    }
