package com.d9tilov.android.network.di

import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object NetworkLoggingModule {
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
}
