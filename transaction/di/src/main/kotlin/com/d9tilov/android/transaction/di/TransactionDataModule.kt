package com.d9tilov.android.transaction.di

import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.data.impl.TransactionDataRepo
import com.d9tilov.android.transaction.data.impl.TransactionLocalSource
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface TransactionDataModule {
    @Binds
    fun provideTransactionLocalSource(impl: TransactionLocalSource): TransactionSource

    @Binds
    fun provideTransactionRepo(impl: TransactionDataRepo): TransactionRepo
}
