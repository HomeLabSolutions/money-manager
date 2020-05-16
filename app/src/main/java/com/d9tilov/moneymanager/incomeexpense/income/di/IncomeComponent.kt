package com.d9tilov.moneymanager.incomeexpense.income.di

import com.d9tilov.moneymanager.base.di.AppComponent
import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeFragment
import dagger.Component

@Component(
    modules = [IncomeModule::class],
    dependencies = [AppComponent::class]
)
@FeatureScope
interface IncomeComponent : BaseFragmentComponent<IncomeFragment> {

    @Component.Builder
    interface Builder {

        fun build(): IncomeComponent
        fun appComponent(component: AppComponent): Builder
        fun incomeModule(module: IncomeModule): Builder
    }
}
