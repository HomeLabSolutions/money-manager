package com.d9tilov.android.network.di

import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.network.BuildConfig
import com.d9tilov.android.network.di.qualifier.CurrencyNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoNetworkApi
import com.d9tilov.android.network.di.qualifier.GeoapifyNetworkApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val READ_TIMEOUT = 15L
    private const val WRITE_TIMEOUT = 15L
    private const val BASE_CURRENCY_URL = "https://v6.exchangerate-api.com/"
    private const val BASE_GEOCODE_URL = "https://api.opencagedata.com/"
    private const val BASE_GEOAPIFY_URL = "https://api.geoapify.com/"
    private const val JSON_CONTENT_TYPE = "application/json"

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message -> Timber.tag(TAG).d("HTTP REQUEST: $message") }.apply {
            redactQueryParams("apiKey")
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }

    @CurrencyNetworkApi
    @Provides
    fun provideCurrencyRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = JSON_CONTENT_TYPE.toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit
            .Builder()
            .baseUrl(BASE_CURRENCY_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @GeoNetworkApi
    @Provides
    fun provideGeoRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = JSON_CONTENT_TYPE.toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit
            .Builder()
            .baseUrl(BASE_GEOCODE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @GeoapifyNetworkApi
    @Provides
    fun provideGeoapifyRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = JSON_CONTENT_TYPE.toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit
            .Builder()
            .baseUrl(BASE_GEOAPIFY_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }
}
