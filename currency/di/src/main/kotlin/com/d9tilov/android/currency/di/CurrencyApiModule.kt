package com.d9tilov.android.currency.di

import com.d9tilov.android.currency.data.impl.api.CurrencyApi
import com.d9tilov.android.currency.data.impl.api.GeocodeApi
import com.d9tilov.android.currency.data.impl.api.KtorCurrencyApi
import com.d9tilov.android.currency.data.impl.api.KtorGeocodeApi
import com.d9tilov.android.network.di.qualifier.CurrencyNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoNetworkApi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient

@BindingContainer
@ContributesTo(AppScope::class)
object CurrencyApiModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideCurrencyApi(
        @CurrencyNetworkApi client: HttpClient,
    ): CurrencyApi = KtorCurrencyApi(client)

    @Provides
    @SingleIn(AppScope::class)
    fun provideGeoApi(
        @GeoNetworkApi client: HttpClient,
    ): GeocodeApi = KtorGeocodeApi(client)
}
