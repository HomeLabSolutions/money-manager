package com.d9tilov.moneymanager.regular.di

import android.content.Context
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.regular.data.RegularTransactionDataRepo
import com.d9tilov.moneymanager.regular.data.local.RegularTransactionLocalSource
import com.d9tilov.moneymanager.regular.data.local.RegularTransactionSource
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractorImpl
import com.d9tilov.moneymanager.regular.domain.RegularTransactionRepo
import com.d9tilov.moneymanager.regular.ui.notification.TransactionNotificationBuilder
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RegularTransactionModule {

    @Provides
    fun provideRegularTransactionSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase
    ): RegularTransactionSource =
        RegularTransactionLocalSource(preferencesStore, appDatabase.regularTransactionDao())

    @Provides
    fun provideRegularTransactionRepo(
        regularTransactionSource: RegularTransactionSource,
    ): RegularTransactionRepo = RegularTransactionDataRepo(regularTransactionSource)

    @Provides
    fun provideRegularTransactionInteractor(
        regularTransactionRepo: RegularTransactionRepo,
        currencyInteractor: CurrencyInteractor,
        categoryInteractor: CategoryInteractor,
        userInteractor: UserInteractor,
    ): RegularTransactionInteractor = RegularTransactionInteractorImpl(
        regularTransactionRepo,
        currencyInteractor,
        categoryInteractor,
        userInteractor
    )

    @Provides
    fun provideNotificationBuilder(context: Context): TransactionNotificationBuilder =
        TransactionNotificationBuilder(context)
}
