package com.d9tilov.android.transaction.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.d9tilov.android.category.domain.model.CategoryArgs
import com.d9tilov.android.category.domain.model.CategoryDestination
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.CURRENCY_CODE_ARGS
import com.d9tilov.android.transaction.ui.TransactionCreationRoute
import com.d9tilov.android.transaction.ui.vm.TransactionCreationViewModel

const val TRANSACTION_NAVIGATION_ROUTE = "transaction_route"
const val TRANSACTION_ID_ARG = "transaction_id"

internal sealed class TransactionArgs {
    class TransactionCreationArgs(
        val transactionId: Long,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this((checkNotNull(savedStateHandle[TRANSACTION_ID_ARG]) as Long))
    }
}

fun NavController.navigateToTransactionScreen(
    transactionId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$TRANSACTION_NAVIGATION_ROUTE/$transactionId", navOptions)
}

fun NavGraphBuilder.transactionCreationScreen(
    route: String,
    clickBack: () -> Unit,
    onCategoryClick: (TransactionType, CategoryDestination) -> Unit,
    onCurrencyClick: (String) -> Unit,
) {
    composable(
        route = route,
        arguments = listOf(navArgument(TRANSACTION_ID_ARG) { type = NavType.LongType }),
    ) { entry ->
        val viewModel: TransactionCreationViewModel = hiltViewModel()
        val categoryId = entry.savedStateHandle.get<Long>(CategoryArgs.CATEGORY_ID_ARGS)
        categoryId?.let { id ->
            viewModel.updateCategory(id)
            entry.savedStateHandle.remove<Long>(CategoryArgs.CATEGORY_ID_ARGS)
        }
        val currencyCode = entry.savedStateHandle.get<String>(CURRENCY_CODE_ARGS)
        currencyCode?.let { code ->
            viewModel.updateCurrencyCode(code)
            entry.savedStateHandle.remove<String>(CURRENCY_CODE_ARGS)
        }
        TransactionCreationRoute(
            viewModel = viewModel,
            clickBack = clickBack,
            clickCurrency = onCurrencyClick,
            clickCategory = onCategoryClick,
        )
    }
}
