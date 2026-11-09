package com.d9tilov.android.widget.di

import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import com.d9tilov.android.widget.domain.contract.SpendingWidgetStateObserver
import com.d9tilov.android.widget.domain.impl.SpendingWidgetInteractorImpl
import com.d9tilov.android.widget.ui.SpendingWidgetStateObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface WidgetDomainModule {
    @Binds
    fun provideSpendingWidgetInteractor(impl: SpendingWidgetInteractorImpl): SpendingWidgetInteractor

    @Binds
    fun provideSpendingWidgetStateObserver(impl: SpendingWidgetStateObserverImpl): SpendingWidgetStateObserver
}
