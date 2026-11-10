package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.GeocodingInteractor
import com.d9tilov.android.currency.domain.impl.CurrencyInteractorImpl
import com.d9tilov.android.currency.domain.impl.GeocodingInteractorImpl
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.currency.observer.impl.CurrencyUpdateObserverImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val currencyDomainModule =
    module {
        factory<CurrencyInteractorImpl>() bind CurrencyInteractor::class
        factory<GeocodingInteractorImpl>() bind GeocodingInteractor::class
        factory<CurrencyUpdateObserverImpl>() bind CurrencyUpdateObserver::class
    }
