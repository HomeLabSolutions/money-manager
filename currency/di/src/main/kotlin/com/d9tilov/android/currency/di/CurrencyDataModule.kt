package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.contract.CurrencySource
import com.d9tilov.android.currency.data.impl.CurrencyDataRepo
import com.d9tilov.android.currency.data.impl.CurrencyLocalSource
import com.d9tilov.android.currency.data.impl.GeocodeDataRepo
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface CurrencyDataModule {
    @Binds
    val CurrencyLocalSource.provideCurrencySource: CurrencySource

    @Binds
    val CurrencyDataRepo.provideCurrencyRepo: CurrencyRepo

    @Binds
    val GeocodeDataRepo.provideGeoRepo: GeocodeRepo
}
