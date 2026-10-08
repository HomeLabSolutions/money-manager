package com.d9tilov.android.transaction.ui.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.d9tilov.android.category.domain.entity.CategoryArgs
import com.d9tilov.android.category.domain.entity.CategoryDestination
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.CURRENCY_CODE_ARGS
import com.d9tilov.android.transaction.ui.TransactionInfoRoute
import com.d9tilov.android.transaction.ui.model.TransactionInfoMode
import com.d9tilov.android.transaction.ui.vm.TransactionInfoViewModel

const val TRANSACTION_NAVIGATION_ROUTE = "transaction_route"
const val TRANSACTION_ID_ARG = "transaction_id"
const val TRANSACTION_MODE_ARG = "transaction_mode"

internal sealed class TransactionArgs {
    data class TransactionInfoArgs(
        val transactionId: Long,
        val mode: TransactionInfoMode,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[TRANSACTION_ID_ARG]).toString().toLong(),
                TransactionInfoMode.valueOf(checkNotNull(savedStateHandle[TRANSACTION_MODE_ARG])),
            )
    }
}

fun NavController.navigateToTransactionScreen(
    transactionId: Long,
    mode: TransactionInfoMode,
    navOptions: NavOptions? = null,
) {
    this.navigate("$TRANSACTION_NAVIGATION_ROUTE/$transactionId/${mode.name}", navOptions)
}

fun NavGraphBuilder.transactionInfoScreen(
    route: String,
    clickBack: () -> Unit,
    onCategoryClick: (TransactionType, CategoryDestination) -> Unit,
    onCurrencyClick: (String) -> Unit,
) {
    composable(
        route = route,
        arguments =
            listOf(
                navArgument(TRANSACTION_ID_ARG) { type = NavType.LongType },
                navArgument(TRANSACTION_MODE_ARG) { type = NavType.StringType },
            ),
    ) { entry ->
        val viewModel: TransactionInfoViewModel = hiltViewModel()
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
        TransactionInfoRoute(
            viewModel = viewModel,
            clickBack = clickBack,
            clickCurrency = onCurrencyClick,
            clickCategory = onCategoryClick,
        )
    }
}
