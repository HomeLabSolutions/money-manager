package com.d9tilov.android.transaction.di

import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.impl.ObserveSpendingSummaryUseCaseImpl
import com.d9tilov.android.transaction.domain.impl.TransactionInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface TransactionDomainModule {
    @Binds
    val ObserveSpendingSummaryUseCaseImpl.provideObserveSpendingSummaryUseCase: ObserveSpendingSummaryUseCase

    @Binds
    val TransactionInteractorImpl.provideTransactionInteractor: TransactionInteractor
}
