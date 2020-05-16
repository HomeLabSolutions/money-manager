package com.d9tilov.moneymanager.statistics.di

import com.d9tilov.moneymanager.base.di.AppComponent
import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.statistics.ui.StatisticsFragment
import dagger.Component

@Component(
    modules = [StatisticsModule::class],
    dependencies = [AppComponent::class]
)
@FeatureScope
interface StatisticsComponent : BaseFragmentComponent<StatisticsFragment> {

    @Component.Builder
    interface Builder {

        fun build(): StatisticsComponent
        fun appComponent(component: AppComponent): Builder
        fun statisticsModule(module: StatisticsModule): Builder
    }
}
