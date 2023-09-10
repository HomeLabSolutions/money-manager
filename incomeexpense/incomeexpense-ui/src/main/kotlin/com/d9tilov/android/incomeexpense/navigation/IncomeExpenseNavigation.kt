package com.d9tilov.android.incomeexpense.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.incomeexpense.ui.IncomeExpenseRoute

const val incomeExpenseNavigationRoute = "income_expense"

interface BaseIncomeExpenseNavigator : BaseNavigator {
    fun openCategoriesScreen()
    fun showEmptySumError()
}

interface IncomeExpenseNavigator : BaseNavigator

interface IncomeNavigator : BaseIncomeExpenseNavigator

interface ExpenseNavigator : BaseIncomeExpenseNavigator

fun NavController.navigateToIncomeExpense(navOptions: NavOptions? = null) {
    this.navigate(incomeExpenseNavigationRoute, navOptions)
}

fun NavGraphBuilder.incomeExpenseScreen() {
    composable(route = incomeExpenseNavigationRoute) { IncomeExpenseRoute() }
}
