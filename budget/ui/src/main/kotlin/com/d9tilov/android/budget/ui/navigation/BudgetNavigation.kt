package com.d9tilov.android.budget.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.d9tilov.android.budget.ui.BudgetRoute
import com.d9tilov.android.designsystem.component.roundedBackComposable

const val BUDGET_NAVIGATION_ROUTE = "budget_route"

fun NavController.navigateToBudgetScreen(navOptions: NavOptions? = null) {
    this.navigate(BUDGET_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.budgetScreen(
    route: String,
    clickBack: () -> Unit,
) {
    roundedBackComposable(route = route) { BudgetRoute(clickBack = clickBack) }
}
