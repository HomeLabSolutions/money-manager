package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.GeocodingInteractor
import com.d9tilov.android.currency.domain.impl.CurrencyInteractorImpl
import com.d9tilov.android.currency.domain.impl.GeocodingInteractorImpl
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.currency.observer.impl.CurrencyUpdateObserverImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface CurrencyDomainModule {
    @Binds
    val CurrencyInteractorImpl.provideCurrencyInteractor: CurrencyInteractor

    @Binds
    val GeocodingInteractorImpl.provideGeocodingInteractor: GeocodingInteractor

    @Binds
    val CurrencyUpdateObserverImpl.provideCurrencyObserver: CurrencyUpdateObserver
}
