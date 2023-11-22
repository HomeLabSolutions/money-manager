package com.d9tilov.android.incomeexpense.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.currencyCodeArgs
import com.d9tilov.android.incomeexpense.ui.IncomeExpenseRoute
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType
import com.d9tilov.android.transaction.domain.model.Transaction

const val incomeExpenseNavigationRoute = "income_expense_screen"

fun NavController.navigateToIncomeExpense(navOptions: NavOptions? = null) {
    this.navigate(incomeExpenseNavigationRoute, navOptions)
}

fun NavGraphBuilder.incomeExpenseScreen(
    onCurrencyClick: () -> Unit,
    onAllCategoryClick: (TransactionType) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
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
            },
            onTransactionClicked = onTransactionClick
        )
    }
}
