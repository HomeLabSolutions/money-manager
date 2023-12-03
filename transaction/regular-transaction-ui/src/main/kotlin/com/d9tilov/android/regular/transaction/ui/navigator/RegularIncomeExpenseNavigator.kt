package com.d9tilov.android.regular.transaction.ui.navigator

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.core.constants.NavigationConstants
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.toType
import com.d9tilov.android.regular.transaction.ui.RegularTransactionListRoute

interface BaseRegularIncomeExpenseNavigator : BaseNavigator

interface RegularExpenseNavigator : BaseRegularIncomeExpenseNavigator
interface RegularIncomeNavigator : BaseRegularIncomeExpenseNavigator

interface RegularTransactionCreatedNavigator : BaseNavigator {
    fun back()
}

const val regularIncomeListNavigationRoute = "regular_income_list_route"
const val regularExpenseListNavigationRoute = "regular_expense_list_route"

interface DayOfMonthDialogNavigator : BaseNavigator

internal sealed class RegularTransactionArgs {
    class RegularTransactionListArgs(
        val transactionType: TransactionType,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(
                    (checkNotNull(savedStateHandle[NavigationConstants.transactionTypeArg]) as Int).toType(),
                )
    }
}


fun NavController.navigateToRegularIncomeListScreen(navOptions: NavOptions? = null) {
    this.navigate(regularIncomeListNavigationRoute, navOptions)
}
fun NavGraphBuilder.regularIncomeScreen(clickBack: () -> Unit) {
    composable(route = regularIncomeListNavigationRoute) { RegularTransactionListRoute(clickBack = clickBack) }
}

fun NavController.navigateToRegularExpenseListScreen(navOptions: NavOptions? = null) {
    this.navigate(regularExpenseListNavigationRoute, navOptions)
}

fun NavGraphBuilder.regularExpenseScreen(clickBack: () -> Unit) {
    composable(route = regularExpenseListNavigationRoute) { RegularTransactionListRoute(clickBack = clickBack) }
}
