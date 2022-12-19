package com.d9tilov.moneymanager.regular.di

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.regular.data.RegularTransactionDataRepo
import com.d9tilov.moneymanager.regular.data.local.RegularTransactionLocalSource
import com.d9tilov.moneymanager.regular.data.local.RegularTransactionSource
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractorImpl
import com.d9tilov.moneymanager.regular.domain.RegularTransactionRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RegularTransactionModule {

    @Provides
    fun provideRegularTransactionSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase
    ): RegularTransactionSource =
        RegularTransactionLocalSource(preferencesStore, appDatabase.regularTransactionDao())

    @Provides
    fun provideRegularTransactionRepo(
        regularTransactionSource: RegularTransactionSource
    ): RegularTransactionRepo = RegularTransactionDataRepo(regularTransactionSource)

    @Provides
    fun provideRegularTransactionInteractor(
        currencyInteractor: CurrencyInteractor,
        regularTransactionRepo: RegularTransactionRepo,
        categoryInteractor: CategoryInteractor
    ): RegularTransactionInteractor = RegularTransactionInteractorImpl(
        currencyInteractor,
        regularTransactionRepo,
        categoryInteractor
    )
}
