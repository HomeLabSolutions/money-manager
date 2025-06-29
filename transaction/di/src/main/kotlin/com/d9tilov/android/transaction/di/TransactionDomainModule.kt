package com.d9tilov.android.transaction.di

import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.impl.TransactionInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface TransactionDomainModule {
    @Binds
    fun provideTransactionInteractor(impl: TransactionInteractorImpl): TransactionInteractor
}
