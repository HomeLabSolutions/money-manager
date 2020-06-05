package com.d9tilov.moneymanager.base.di.builder

import com.d9tilov.moneymanager.category.di.provider.CategoryFrgProvider
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.di.IncomeExpenseFrgProvider
import com.d9tilov.moneymanager.settings.di.provider.SettingsFrgProvider
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import com.d9tilov.moneymanager.splash.ui.provider.SplashFrgProvider
import com.d9tilov.moneymanager.statistics.di.provider.StatisticsFrgProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [SplashFrgProvider::class])
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(
        modules = [
            IncomeExpenseFrgProvider::class,
            SettingsFrgProvider::class,
            StatisticsFrgProvider::class,
            CategoryFrgProvider::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity
}
