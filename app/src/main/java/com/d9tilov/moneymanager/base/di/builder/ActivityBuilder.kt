package com.d9tilov.moneymanager.base.di.builder

import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.base.ui.ViewModelFactory
import com.d9tilov.moneymanager.category.di.CategoryFrgProvider
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.di.IncomeExpenseFrgProvider
import com.d9tilov.moneymanager.settings.di.provider.SettingsFrgProvider
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import com.d9tilov.moneymanager.statistics.di.provider.StatisticsFrgProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilder {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(
        modules = [
            IncomeExpenseFrgProvider::class,
            SettingsFrgProvider::class,
            StatisticsFrgProvider::class,
            CategoryFrgProvider::class]
    )
    fun bindMainActivity(): MainActivity
}
