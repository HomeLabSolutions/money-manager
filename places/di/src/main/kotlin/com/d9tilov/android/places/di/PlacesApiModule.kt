package com.d9tilov.android.places.di

import com.d9tilov.android.network.di.qualifier.GeoapifyNetworkApi
import com.d9tilov.android.places.data.impl.api.GeoapifyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesApiModule {
    @Provides
    @Singleton
    fun provideGeoapifyApi(
        @GeoapifyNetworkApi retrofit: Retrofit,
    ): GeoapifyApi = retrofit.create(GeoapifyApi::class.java)
}
