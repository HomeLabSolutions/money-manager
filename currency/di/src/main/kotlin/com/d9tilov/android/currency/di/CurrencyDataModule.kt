package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.contract.CurrencySource
import com.d9tilov.android.currency.data.impl.CurrencyDataRepo
import com.d9tilov.android.currency.data.impl.CurrencyLocalSource
import com.d9tilov.android.currency.data.impl.GeocodeDataRepo
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val currencyDataModule =
    module {
        factory<CurrencyLocalSource>() bind CurrencySource::class
        factory<CurrencyDataRepo>() bind CurrencyRepo::class
        factory<GeocodeDataRepo>() bind GeocodeRepo::class
    }
