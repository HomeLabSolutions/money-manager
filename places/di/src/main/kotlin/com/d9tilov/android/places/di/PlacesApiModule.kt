package com.d9tilov.android.places.di

import com.d9tilov.android.network.di.qualifier.GeoapifyNetworkApi
import com.d9tilov.android.places.data.impl.api.GeoapifyApi
import com.d9tilov.android.places.data.impl.api.KtorGeoapifyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesApiModule {
    @Provides
    @Singleton
    fun provideGeoapifyApi(
        @GeoapifyNetworkApi client: HttpClient,
    ): GeoapifyApi = KtorGeoapifyApi(client)
}
