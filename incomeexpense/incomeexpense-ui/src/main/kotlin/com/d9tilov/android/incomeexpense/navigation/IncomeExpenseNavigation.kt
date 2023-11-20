package com.d9tilov.android.incomeexpense.navigation

import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.currencyCodeArgs
import com.d9tilov.android.incomeexpense.ui.IncomeExpenseRoute
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType

const val incomeExpenseNavigationRoute = "income_expense"

fun NavController.navigateToIncomeExpense(navOptions: NavOptions? = null) {
    this.navigate(incomeExpenseNavigationRoute, navOptions)
}

fun NavGraphBuilder.incomeExpenseScreen(
    onCurrencyClick: () -> Unit,
    onAllCategoryClick: (TransactionType) -> Unit,
) {
    composable(route = incomeExpenseNavigationRoute) { entry ->
        IncomeExpenseRoute(
            currencyCode =  entry.savedStateHandle.get<String>(currencyCodeArgs),
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
