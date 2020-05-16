package com.d9tilov.moneymanager.base.di

import com.d9tilov.moneymanager.data.di.DatabaseModule
import com.d9tilov.moneymanager.data.di.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }
}
