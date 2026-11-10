package com.d9tilov.android.places.di

import com.d9tilov.android.places.data.impl.api.GeoapifyApi
import com.d9tilov.android.places.data.impl.api.KtorGeoapifyApi
import io.ktor.client.HttpClient
import org.koin.core.annotation.Named
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

fun provideGeoapifyApi(
    @Named("GeoapifyNetworkApi") client: HttpClient,
): GeoapifyApi = KtorGeoapifyApi(client)

val placesApiModule =
    module {
        single { create(::provideGeoapifyApi) }
    }
