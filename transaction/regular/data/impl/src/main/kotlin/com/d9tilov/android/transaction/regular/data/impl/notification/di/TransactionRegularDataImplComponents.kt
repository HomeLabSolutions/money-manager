package com.d9tilov.android.transaction.regular.data.impl.notification.di

import com.d9tilov.android.transaction.regular.data.impl.notification.TransactionNotificationSender
import com.d9tilov.android.transaction.regular.data.impl.worker.RegularTransactionSyncWorker
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.worker

val transactionRegularDataImplComponents =
    module {
        factory<TransactionNotificationSender>()
        worker<RegularTransactionSyncWorker>()
    }
