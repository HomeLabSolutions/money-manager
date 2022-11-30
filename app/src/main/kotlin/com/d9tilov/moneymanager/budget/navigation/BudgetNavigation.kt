package com.d9tilov.moneymanager.budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.moneymanager.currency.ui.CurrencyListRoute
import com.d9tilov.moneymanager.prepopulate.ui.BudgetRoute

const val budgetNavigationRoute = "budget"

fun NavController.navigateToBudgetScreen(navOptions: NavOptions? = null) {
    this.navigate(budgetNavigationRoute, navOptions)
}

fun NavGraphBuilder.budgetScreen(clickBack: () -> Unit) {
    composable(route = budgetNavigationRoute) { BudgetRoute(clickBack = clickBack) }
}
