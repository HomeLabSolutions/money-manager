package com.d9tilov.android.budget.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.budget.ui.BudgetRoute

const val budgetNavigationRoute = "budget_route"


fun NavController.navigateToBudgetScreen(navOptions: NavOptions? = null) {
    this.navigate(budgetNavigationRoute, navOptions)
}

fun NavGraphBuilder.budgetScreen(clickBack: () -> Unit) {
    composable(route = budgetNavigationRoute) { BudgetRoute(clickBack = clickBack) }
}
