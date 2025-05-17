package com.d9tilov.android.transaction.regular.di

import com.d9tilov.android.transaction.regular.data.contract.RegularTransactionSource
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionDataRepo
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionLocalSource
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface RegularTransactionDataModule {
    @Binds
    fun provideRegularTransactionSource(impl: RegularTransactionLocalSource): RegularTransactionSource

    @Binds
    fun provideRegularTransactionRepo(impl: RegularTransactionDataRepo): RegularTransactionRepo
}
