package com.d9tilov.moneymanager.core.di

import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Component(modules = [CoreNetworkModule::class])
@Singleton
interface CoreComponent {

    @Component.Builder interface Builder {
        fun build(): CoreComponent
    }

    fun provideOkHttpClient(): OkHttpClient
    fun provideGson(): Gson
    fun provideGsonConverterFactory(): GsonConverterFactory
}
