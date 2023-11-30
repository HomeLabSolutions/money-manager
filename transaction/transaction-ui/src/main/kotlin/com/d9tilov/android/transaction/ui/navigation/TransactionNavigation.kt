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
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs
import com.d9tilov.android.transaction.ui.TransactionCreationRoute
import com.d9tilov.android.transaction.ui.vm.TransactionCreationViewModel

interface RemoveTransactionDialogNavigator : BaseNavigator {
    fun remove()
}

interface EditTransactionNavigator : BaseNavigator {
    fun save()
}

const val transactionNavigationRoute = "transaction_screen"
const val transactionIdArg = "transaction_id"

internal sealed class TransactionArgs {
    class TransactionCreationArgs(val transactionId: Long) {
        constructor(savedStateHandle: SavedStateHandle) :
                this((checkNotNull(savedStateHandle[transactionIdArg]) as Long))
    }
}
fun NavController.navigateToTransactionScreen(transactionId: Long, navOptions: NavOptions? = null) {
    this.navigate("$transactionNavigationRoute/$transactionId", navOptions)
}

fun NavGraphBuilder.transactionCreationScreen(
    clickBack: () -> Unit,
    onCategoryClick: (TransactionType, CategoryDestination) -> Unit,
    onCurrencyClick: (String) -> Unit
) {
    val route = "$transactionNavigationRoute/{$transactionIdArg}"
    composable(
        route = route,
        arguments = listOf(navArgument(transactionIdArg) { type = NavType.LongType })
    ) { entry ->
        val viewModel: TransactionCreationViewModel = hiltViewModel()
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
        TransactionCreationRoute(
            viewModel = viewModel,
            clickBack = clickBack,
            clickCurrency = onCurrencyClick,
            clickCategory = onCategoryClick
        )
    }
}
