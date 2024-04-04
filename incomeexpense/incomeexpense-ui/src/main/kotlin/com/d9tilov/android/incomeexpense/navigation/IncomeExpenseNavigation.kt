package com.d9tilov.android.incomeexpense.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.category.domain.model.CategoryArgs
import com.d9tilov.android.category.domain.model.CategoryDestination
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.CURRENCY_CODE_ARGS
import com.d9tilov.android.incomeexpense.ui.IncomeExpenseRoute
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType
import com.d9tilov.android.transaction.domain.model.Transaction

const val incomeExpenseNavigationRoute = "income_expense_screen"

fun NavController.navigateToIncomeExpense(navOptions: NavOptions? = null) {
    this.navigate(incomeExpenseNavigationRoute, navOptions)
}

fun NavGraphBuilder.incomeExpenseScreen(
    onCurrencyClick: (String) -> Unit,
    onAllCategoryClick: (TransactionType, CategoryDestination) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
) {
    composable(route = incomeExpenseNavigationRoute) { entry ->
        val viewModel: IncomeExpenseViewModel = hiltViewModel()
        val categoryId = entry.savedStateHandle.get<Long>(CategoryArgs.categoryIdArgs)
        categoryId?.let { id ->
            viewModel.addTransaction(id)
            entry.savedStateHandle.remove<Long>(CategoryArgs.categoryIdArgs)
        }
        val currencyCode =  entry.savedStateHandle.get<String>(CURRENCY_CODE_ARGS)
        currencyCode?.let { code ->
            viewModel.updateCurrencyCode(code)
            entry.savedStateHandle.remove<String>(CURRENCY_CODE_ARGS)
        }
        IncomeExpenseRoute(
            viewModel = viewModel,
            onCurrencyClicked = onCurrencyClick,
            onAllCategoryClicked = { type, destination ->
                when (type) {
                    ScreenType.EXPENSE -> onAllCategoryClick(TransactionType.EXPENSE, destination)
                    ScreenType.INCOME -> onAllCategoryClick(TransactionType.INCOME, destination)
                }
            },
            onTransactionClicked = onTransactionClick
        )
    }
}
