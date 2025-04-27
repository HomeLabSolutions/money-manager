package com.d9tilov.android.incomeexpense.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.category.domain.entity.CategoryArgs
import com.d9tilov.android.category.domain.entity.CategoryDestination
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.CURRENCY_CODE_ARGS
import com.d9tilov.android.incomeexpense.ui.IncomeExpenseRoute
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType
import com.d9tilov.android.transaction.ui.model.TransactionUiModel

const val INCOME_EXPENSE_NAVIGATION_ROUTE = "income_expense_route"

fun NavController.navigateToIncomeExpense(navOptions: NavOptions? = null) {
    this.navigate(INCOME_EXPENSE_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.incomeExpenseScreen(
    route: String,
    onCurrencyClick: (String) -> Unit,
    onAllCategoryClick: (TransactionType, CategoryDestination) -> Unit,
    onTransactionClick: (TransactionUiModel) -> Unit,
) {
    composable(route = route) { entry ->
        val viewModel: IncomeExpenseViewModel = hiltViewModel()
        val categoryId = entry.savedStateHandle.get<Long>(CategoryArgs.CATEGORY_ID_ARGS)
        categoryId?.let { id ->
            viewModel.addTransaction(id)
            entry.savedStateHandle.remove<Long>(CategoryArgs.CATEGORY_ID_ARGS)
        }
        val currencyCode = entry.savedStateHandle.get<String>(CURRENCY_CODE_ARGS)
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
            onTransactionClicked = onTransactionClick,
        )
    }
}
