package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.contract.CurrencySource
import com.d9tilov.android.currency.data.impl.CurrencyDataRepo
import com.d9tilov.android.currency.data.impl.CurrencyLocalSource
import com.d9tilov.android.currency.data.impl.GeocodeDataRepo
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.GeocodeRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyDataModule {
    @Binds
    fun provideCurrencySource(impl: CurrencyLocalSource): CurrencySource

    @Binds
    fun provideCurrencyRepo(impl: CurrencyDataRepo): CurrencyRepo

    @Binds
    fun provideGeoRepo(impl: GeocodeDataRepo): GeocodeRepo
}
