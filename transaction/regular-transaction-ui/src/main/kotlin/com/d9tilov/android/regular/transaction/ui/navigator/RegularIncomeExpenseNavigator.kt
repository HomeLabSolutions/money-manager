package com.d9tilov.android.regular.transaction.ui.navigator

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
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.core.constants.NavigationConstants
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.toType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.CURRENCY_CODE_ARGS
import com.d9tilov.android.regular.transaction.ui.RegularTransactionCreationRoute
import com.d9tilov.android.regular.transaction.ui.RegularTransactionListRoute
import com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationViewModel

interface BaseRegularIncomeExpenseNavigator : BaseNavigator

interface RegularExpenseNavigator : BaseRegularIncomeExpenseNavigator

interface RegularIncomeNavigator : BaseRegularIncomeExpenseNavigator

const val REGULAR_TRANSACTION_ID_ARGS = "regular_transaction_id"
const val REGULAR_TRANSACTION_LIST_NAVIGATION_ROUTE = "regular_transaction_list_route"
const val REGULAR_TRANSACTION_CREATION_NAVIGATION_ROUTE = "regular_transaction_creation_route"

interface DayOfMonthDialogNavigator : BaseNavigator

internal sealed class RegularTransactionArgs {
    class RegularTransactionListArgs(
        val transactionType: TransactionType,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this(checkNotNull(savedStateHandle[NavigationConstants.TRANSACTION_TYPE_ARG]).toString().toInt().toType())
    }

    class RegularTransactionCreationArgs(
        val transactionType: TransactionType,
        val transactionId: Long,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[NavigationConstants.TRANSACTION_TYPE_ARG]).toString().toInt().toType(),
                checkNotNull(savedStateHandle[REGULAR_TRANSACTION_ID_ARGS]).toString().toLong(),
            )
    }
}

fun NavController.navigateToRegularTransactionListScreen(
    navOptions: NavOptions? = null,
    transactionType: TransactionType,
) {
    this.navigate(
        "$REGULAR_TRANSACTION_LIST_NAVIGATION_ROUTE/${transactionType.value}",
        navOptions,
    )
}

fun NavGraphBuilder.regularTransactionListScreen(
    route: String,
    clickBack: () -> Unit,
    openCreationTransaction: (TransactionType, Long) -> Unit,
) {
    composable(
        route = route,
        arguments =
            listOf(
                navArgument(NavigationConstants.TRANSACTION_TYPE_ARG) { type = NavType.IntType },
            ),
    ) {
        RegularTransactionListRoute(
            clickBack = clickBack,
            onAddClicked = openCreationTransaction,
            onItemClicked = { tr -> openCreationTransaction(tr.type, tr.id) },
        )
    }
}

fun NavController.navigateToRegularTransactionCreationScreen(
    transactionType: TransactionType,
    transactionId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        "$REGULAR_TRANSACTION_CREATION_NAVIGATION_ROUTE/${transactionType.value}/$transactionId",
        navOptions,
    )
}

fun NavGraphBuilder.regularTransactionCreationScreen(
    route: String,
    onCategoryClick: (TransactionType, CategoryDestination) -> Unit,
    onCurrencyClick: (String) -> Unit,
    onSaveClick: () -> Unit,
    clickBack: () -> Unit,
) {
    composable(
        route = route,
        arguments =
            listOf(
                navArgument(NavigationConstants.TRANSACTION_TYPE_ARG) { type = NavType.IntType },
                navArgument(REGULAR_TRANSACTION_ID_ARGS) { type = NavType.LongType },
            ),
    ) { entry ->
        val viewModel: RegularTransactionCreationViewModel = hiltViewModel()
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
        RegularTransactionCreationRoute(
            viewModel = viewModel,
            clickBack = clickBack,
            onCurrencyClicked = onCurrencyClick,
            clickCategory = onCategoryClick,
            onSaveClicked = onSaveClick,
        )
    }
}
