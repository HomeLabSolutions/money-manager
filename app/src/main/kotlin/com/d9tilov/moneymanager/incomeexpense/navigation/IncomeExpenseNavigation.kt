package com.d9tilov.moneymanager.incomeexpense.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseRoute

const val incomeExpenseNavigationRoute = "income_expense"

fun NavController.navigateToIncomeExpense(navOptions: NavOptions? = null) {
    this.navigate(incomeExpenseNavigationRoute, navOptions)
}

fun NavGraphBuilder.incomeExpenseScreen() {
    composable(route = incomeExpenseNavigationRoute) { IncomeExpenseRoute() }
}
