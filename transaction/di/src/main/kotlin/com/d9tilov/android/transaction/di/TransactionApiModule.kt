package com.d9tilov.android.transaction.di

import com.d9tilov.android.network.GeoapifyApi
import com.d9tilov.android.network.di.qualifier.GeoapifyNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionApiModule {
    @Provides
    @Singleton
    fun provideGeoapifyApi(
        @GeoapifyNetworkApi retrofit: Retrofit,
    ): GeoapifyApi = retrofit.create(GeoapifyApi::class.java)
}
