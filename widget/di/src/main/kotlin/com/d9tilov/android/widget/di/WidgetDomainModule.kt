package com.d9tilov.android.widget.di

import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import com.d9tilov.android.widget.domain.contract.SpendingWidgetStateObserver
import com.d9tilov.android.widget.domain.impl.SpendingWidgetInteractorImpl
import com.d9tilov.android.widget.ui.SpendingWidgetStateObserverImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface WidgetDomainModule {
    @Binds
    val SpendingWidgetInteractorImpl.provideSpendingWidgetInteractor: SpendingWidgetInteractor

    @Binds
    val SpendingWidgetStateObserverImpl.provideSpendingWidgetStateObserver: SpendingWidgetStateObserver
}
