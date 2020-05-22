package com.d9tilov.moneymanager.base.di

import android.content.Context
import com.d9tilov.moneymanager.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: App): Context = application
}
