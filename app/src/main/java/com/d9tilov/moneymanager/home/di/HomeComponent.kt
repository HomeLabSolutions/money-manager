package com.d9tilov.moneymanager.home.di

import com.d9tilov.moneymanager.base.di.AppComponent
import com.d9tilov.moneymanager.core.di.BaseActivityComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.home.HomeActivity
import dagger.Component

@Component(
    dependencies = [AppComponent::class]
)
@FeatureScope
interface HomeComponent : BaseActivityComponent<HomeActivity> {

    @Component.Builder
    interface Builder {

        fun build(): HomeComponent
        fun appComponent(component: AppComponent): Builder
    }
}