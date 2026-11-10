package com.d9tilov.android.transaction.di

import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.impl.ObserveSpendingSummaryUseCaseImpl
import com.d9tilov.android.transaction.domain.impl.TransactionInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val transactionDomainModule =
    module {
        factory<ObserveSpendingSummaryUseCaseImpl>() bind ObserveSpendingSummaryUseCase::class
        factory<TransactionInteractorImpl>() bind TransactionInteractor::class
    }
