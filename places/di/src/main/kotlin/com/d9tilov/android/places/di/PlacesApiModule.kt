package com.d9tilov.android.places.di

import com.d9tilov.android.network.di.qualifier.GeoapifyNetworkApi
import com.d9tilov.android.places.data.impl.api.GeoapifyApi
import com.d9tilov.android.places.data.impl.api.KtorGeoapifyApi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient

@BindingContainer
@ContributesTo(AppScope::class)
object PlacesApiModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideGeoapifyApi(
        @GeoapifyNetworkApi client: HttpClient,
    ): GeoapifyApi = KtorGeoapifyApi(client)
}
