package com.d9tilov.android.incomeexpense.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.incomeexpense.ui.IncomeExpenseRoute
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType

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

fun NavGraphBuilder.incomeExpenseScreen(
    onCurrencyClick: () -> Unit,
    onAllCategoryClick: (TransactionType) -> Unit,
) {
    composable(route = incomeExpenseNavigationRoute) {
        IncomeExpenseRoute(
            onCurrencyClicked = onCurrencyClick,
            onAllCategoryClicked = { type ->
                when (type) {
                    ScreenType.EXPENSE -> onAllCategoryClick.invoke(TransactionType.EXPENSE)
                    ScreenType.INCOME -> onAllCategoryClick.invoke(TransactionType.INCOME)
                }
            }
        )
    }
}
