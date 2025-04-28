package com.d9tilov.android.budget.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.budget.ui.BudgetRoute

const val BUDGET_NAVIGATION_ROUTE = "budget_route"

fun NavController.navigateToBudgetScreen(navOptions: NavOptions? = null) {
    this.navigate(BUDGET_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.budgetScreen(
    route: String,
    clickBack: () -> Unit,
) {
    composable(route = route) { BudgetRoute(clickBack = clickBack) }
}
