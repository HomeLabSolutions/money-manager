package com.d9tilov.moneymanager.base.di

import com.d9tilov.moneymanager.data.di.DataBaseModule
import com.d9tilov.moneymanager.data.di.NetworkModule
import com.d9tilov.moneymanager.data.di.UserDataModule
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.di.UserDomainModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DataBaseModule::class,
        UserDataModule::class,
        UserDomainModule::class]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }

    fun provideUserInfoInteractor(): UserInfoInteractor
}