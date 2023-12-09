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
import com.d9tilov.android.currency.domain.model.CurrencyArgs
import com.d9tilov.android.regular.transaction.ui.RegularTransactionCreationRoute
import com.d9tilov.android.regular.transaction.ui.RegularTransactionListRoute
import com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationViewModel

interface BaseRegularIncomeExpenseNavigator : BaseNavigator

interface RegularExpenseNavigator : BaseRegularIncomeExpenseNavigator
interface RegularIncomeNavigator : BaseRegularIncomeExpenseNavigator

const val regularTransactionIdArgs = "regular_transaction_id"
const val regularTransactionListNavigationRoute = "regular_transaction_list_route"
const val regularTransactionCreationNavigationRoute = "regular_transaction_creation_route"

interface DayOfMonthDialogNavigator : BaseNavigator

internal sealed class RegularTransactionArgs {
    class RegularTransactionListArgs(
        val transactionType: TransactionType,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
                this((checkNotNull(savedStateHandle[NavigationConstants.transactionTypeArg]) as Int).toType())
    }

    class RegularTransactionCreationArgs(
        val transactionType: TransactionType,
        val transactionId: Long,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(
                    (checkNotNull(savedStateHandle[NavigationConstants.transactionTypeArg]) as Int).toType(),
                    (checkNotNull(savedStateHandle[regularTransactionIdArgs]) as Long)
                )
    }
}


fun NavController.navigateToRegularTransactionListScreen(
    navOptions: NavOptions? = null,
    transactionType: TransactionType,
) {
    this.navigate(
        "$regularTransactionListNavigationRoute/${transactionType.value}",
        navOptions
    )
}

fun NavGraphBuilder.regularTransactionListScreen(
    clickBack: () -> Unit,
    openCreationTransaction: (TransactionType, Long) -> Unit,
) {
    composable(
        route = "$regularTransactionListNavigationRoute/{${NavigationConstants.transactionTypeArg}}",
        arguments = listOf(
            navArgument(NavigationConstants.transactionTypeArg) { type = NavType.IntType },
        )
    ) {
        RegularTransactionListRoute(
            clickBack = clickBack,
            onAddClicked = openCreationTransaction,
            onItemClicked = { tr -> openCreationTransaction.invoke(tr.type, tr.id) }
        )
    }
}

fun NavController.navigateToRegularTransactionCreationScreen(
    transactionType: TransactionType,
    transactionId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        "$regularTransactionCreationNavigationRoute/${transactionType.value}/${transactionId}",
        navOptions
    )
}

fun NavGraphBuilder.regularTransactionCreationScreen(
    onCategoryClick: (TransactionType, CategoryDestination) -> Unit,
    onCurrencyClick: (String) -> Unit,
    onSaveClick: () -> Unit,
    clickBack: () -> Unit,
) {
    composable(
        route = "$regularTransactionCreationNavigationRoute/{${NavigationConstants.transactionTypeArg}}/{$regularTransactionIdArgs}",
        arguments = listOf(
            navArgument(NavigationConstants.transactionTypeArg) { type = NavType.IntType },
            navArgument(regularTransactionIdArgs) { type = NavType.LongType }
        )
    ) { entry ->
        val viewModel: RegularTransactionCreationViewModel = hiltViewModel()
        val categoryId = entry.savedStateHandle.get<Long>(CategoryArgs.categoryIdArgs)
        categoryId?.let { id ->
            viewModel.updateCategory(id)
            entry.savedStateHandle.remove<Long>(CategoryArgs.categoryIdArgs)
        }
        val currencyCode = entry.savedStateHandle.get<String>(CurrencyArgs.currencyCodeArgs)
        currencyCode?.let { code ->
            viewModel.updateCurrencyCode(code)
            entry.savedStateHandle.remove<String>(CurrencyArgs.currencyCodeArgs)
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