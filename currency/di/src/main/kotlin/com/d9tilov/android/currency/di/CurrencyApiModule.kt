package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.impl.api.CurrencyApi
import com.d9tilov.android.currency.data.impl.api.GeocodeApi
import com.d9tilov.android.currency.data.impl.api.KtorCurrencyApi
import com.d9tilov.android.currency.data.impl.api.KtorGeocodeApi
import io.ktor.client.HttpClient
import org.koin.core.annotation.Named
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

fun provideCurrencyApi(
    @Named("CurrencyNetworkApi") client: HttpClient,
): CurrencyApi = KtorCurrencyApi(client)

fun provideGeoApi(
    @Named("GeoNetworkApi") client: HttpClient,
): GeocodeApi = KtorGeocodeApi(client)

val currencyApiModule =
    module {
        single { create(::provideCurrencyApi) }
        single { create(::provideGeoApi) }
    }
