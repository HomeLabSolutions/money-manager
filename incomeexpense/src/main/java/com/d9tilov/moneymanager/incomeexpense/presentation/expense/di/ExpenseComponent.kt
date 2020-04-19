package com.d9tilov.moneymanager.incomeexpense.presentation.expense.di

import com.d9tilov.moneymanager.base.di.AppComponent
import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.incomeexpense.presentation.expense.ui.ExpenseFragment
import dagger.Component

@Component(
    modules = [ExpenseModule::class],
    dependencies = [AppComponent::class]
)
@FeatureScope
interface ExpenseComponent : BaseFragmentComponent<ExpenseFragment> {

    @Component.Builder
    interface Builder {

        fun build(): ExpenseComponent
        fun appComponent(component: AppComponent): Builder
        fun expenseModule(module: ExpenseModule): Builder
    }
}