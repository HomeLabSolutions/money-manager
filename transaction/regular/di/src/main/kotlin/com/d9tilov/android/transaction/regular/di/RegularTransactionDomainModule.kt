package com.d9tilov.android.transaction.regular.di

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import com.d9tilov.android.transaction.regular.domain.impl.RegularTransactionInteractorImpl
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.factory

fun provideRegularTransactionInteractor(
    regularTransactionRepo: RegularTransactionRepo,
    currencyInteractor: CurrencyInteractor,
    categoryInteractor: CategoryInteractor,
): RegularTransactionInteractor =
    RegularTransactionInteractorImpl(
        currencyInteractor,
        regularTransactionRepo,
        categoryInteractor,
    )

val regularTransactionDomainModule =
    module {
        factory { create(::provideRegularTransactionInteractor) }
    }
