package com.d9tilov.moneymanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.d9tilov.android.budget.ui.navigation.BUDGET_NAVIGATION_ROUTE
import com.d9tilov.android.budget.ui.navigation.budgetScreen
import com.d9tilov.android.budget.ui.navigation.navigateToBudgetScreen
import com.d9tilov.android.category.domain.model.CategoryArgs.CATEGORY_ID_ARGS
import com.d9tilov.android.category.ui.navigation.CATEGORY_CREATION_NAVIGATION_ROUTE
import com.d9tilov.android.category.ui.navigation.CATEGORY_DESTINATION_ARG
import com.d9tilov.android.category.ui.navigation.CATEGORY_GROUP_ARG
import com.d9tilov.android.category.ui.navigation.CATEGORY_ICON_GRID_NAVIGATION_ROUTE
import com.d9tilov.android.category.ui.navigation.CATEGORY_ICON_LIST_NAVIGATION_ROUTE
import com.d9tilov.android.category.ui.navigation.CATEGORY_ID_ARG
import com.d9tilov.android.category.ui.navigation.CATEGORY_NAVIGATION_ROUTE
import com.d9tilov.android.category.ui.navigation.categoryCreationScreen
import com.d9tilov.android.category.ui.navigation.categoryIconGridScreen
import com.d9tilov.android.category.ui.navigation.categoryIconListScreen
import com.d9tilov.android.category.ui.navigation.categoryListScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryCreationScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryIconGridScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryIconListScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryListScreen
import com.d9tilov.android.core.constants.NavigationConstants
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.model.CurrencyArgs.CURRENCY_CODE_ARGS
import com.d9tilov.android.currency.ui.navigation.CURRENCY_LIST_NAVIGATION_ROUTE
import com.d9tilov.android.currency.ui.navigation.currencyScreen
import com.d9tilov.android.currency.ui.navigation.navigateToCurrencyListScreen
import com.d9tilov.android.incomeexpense.navigation.INCOME_EXPENSE_NAVIGATION_ROUTE
import com.d9tilov.android.incomeexpense.navigation.incomeExpenseScreen
import com.d9tilov.android.profile.ui.navigation.PROFILE_NAVIGATION_ROUTE
import com.d9tilov.android.profile.ui.navigation.profileScreen
import com.d9tilov.android.regular.transaction.ui.navigator.REGULAR_TRANSACTION_CREATION_NAVIGATION_ROUTE
import com.d9tilov.android.regular.transaction.ui.navigator.REGULAR_TRANSACTION_ID_ARGS
import com.d9tilov.android.regular.transaction.ui.navigator.REGULAR_TRANSACTION_LIST_NAVIGATION_ROUTE
import com.d9tilov.android.regular.transaction.ui.navigator.navigateToRegularTransactionCreationScreen
import com.d9tilov.android.regular.transaction.ui.navigator.navigateToRegularTransactionListScreen
import com.d9tilov.android.regular.transaction.ui.navigator.regularTransactionCreationScreen
import com.d9tilov.android.regular.transaction.ui.navigator.regularTransactionListScreen
import com.d9tilov.android.settings.ui.navigation.SETTINGS_NAVIGATION_ROUTE
import com.d9tilov.android.settings.ui.navigation.navigateToSettingsScreen
import com.d9tilov.android.settings.ui.navigation.settingsScreen
import com.d9tilov.android.statistics.ui.navigation.STATISTICS_DETAILS_NAVIGATION_ROUTE
import com.d9tilov.android.statistics.ui.navigation.STATISTICS_NAVIGATION_ROUTE
import com.d9tilov.android.statistics.ui.navigation.TRANSACTION_DETAILS_CATEGORY_ID_ARGS
import com.d9tilov.android.statistics.ui.navigation.TRANSACTION_DETAILS_DATE_FROM_ARGS
import com.d9tilov.android.statistics.ui.navigation.TRANSACTION_DETAILS_DATE_TO_ARGS
import com.d9tilov.android.statistics.ui.navigation.TRANSACTION_DETAILS_IN_STATISTICS_ARGS
import com.d9tilov.android.statistics.ui.navigation.navigateToStatisticsDetailsTransactionScreen
import com.d9tilov.android.statistics.ui.navigation.statisticsDetailsScreen
import com.d9tilov.android.statistics.ui.navigation.statisticsScreen
import com.d9tilov.android.transaction.ui.navigation.TRANSACTION_ID_ARG
import com.d9tilov.android.transaction.ui.navigation.TRANSACTION_NAVIGATION_ROUTE
import com.d9tilov.android.transaction.ui.navigation.navigateToTransactionScreen
import com.d9tilov.android.transaction.ui.navigation.transactionCreationScreen
import com.d9tilov.moneymanager.ui.MmAppState

@Composable
fun MmNavHost(
    appState: MmAppState,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = INCOME_EXPENSE_ROOT_DESTINATION,
        modifier = modifier,
    ) {
        navigation(
            startDestination = categoryNavigationRoute(),
            route = CATEGORY_ROOT_ROUTE,
        ) {
            categoryListScreen(
                route = categoryNavigationRoute(),
                clickBack = navController::popBackStack,
                openCategory = navController::navigateToCategoryCreationScreen,
                onCategoryClickAndBack = { category ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(CATEGORY_ID_ARGS, category.id)
                    navController.popBackStack()
                },
            )
            categoryCreationScreen(
                route =
                    "$CATEGORY_CREATION_NAVIGATION_ROUTE/" +
                        "{$CATEGORY_ID_ARG}/" +
                        "{${NavigationConstants.TRANSACTION_TYPE_ARG}}",
                navController = navController,
                clickBack = navController::popBackStack,
                openCategoryGroupIconList = navController::navigateToCategoryIconListScreen,
                openCategoryIconGrid = { navController.navigateToCategoryIconGridScreen(-1) },
            )
            categoryIconListScreen(
                route = CATEGORY_ICON_LIST_NAVIGATION_ROUTE,
                clickBack = navController::popBackStack,
                onItemClick = navController::navigateToCategoryIconGridScreen,
            )
            categoryIconGridScreen(
                route = "$CATEGORY_ICON_GRID_NAVIGATION_ROUTE/{$CATEGORY_GROUP_ARG}",
                navController = navController,
                clickBack = navController::popBackStack,
                onIconClick = { isPremium ->
                    val destination = navController.previousBackStackEntry?.destination?.id
                    destination?.let { id -> navController.popBackStack(id, inclusive = isPremium) }
                },
            )
            currencyScreen(
                route = "$CURRENCY_LIST_NAVIGATION_ROUTE?{${CURRENCY_CODE_ARGS}}",
                clickBack = navController::popBackStack,
                onChooseCurrency = { currencyCode ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(CURRENCY_CODE_ARGS, currencyCode)
                    navController.popBackStack()
                },
            )
        }
        navigation(
            startDestination = INCOME_EXPENSE_NAVIGATION_ROUTE,
            route = INCOME_EXPENSE_ROOT_DESTINATION,
        ) {
            incomeExpenseScreen(
                route = INCOME_EXPENSE_NAVIGATION_ROUTE,
                onCurrencyClick = navController::navigateToCurrencyListScreen,
                onAllCategoryClick = navController::navigateToCategoryListScreen,
                onTransactionClick = { navController.navigateToTransactionScreen(transactionId = it.id) },
            )
            transactionCreationScreen(
                route = "$TRANSACTION_NAVIGATION_ROUTE/{$TRANSACTION_ID_ARG}",
                clickBack = navController::popBackStack,
                onCategoryClick = navController::navigateToCategoryListScreen,
                onCurrencyClick = navController::navigateToCurrencyListScreen,
            )
        }
        navigation(
            startDestination = STATISTICS_NAVIGATION_ROUTE,
            route = STATISTICS_ROOT_DESTINATION,
        ) {
            statisticsScreen(
                STATISTICS_NAVIGATION_ROUTE,
                onTransactionClick = { navController.navigateToStatisticsDetailsTransactionScreen(it) },
            )
            statisticsDetailsScreen(
                route =
                    "$STATISTICS_DETAILS_NAVIGATION_ROUTE/" +
                        "{$TRANSACTION_DETAILS_CATEGORY_ID_ARGS}/" +
                        "{$TRANSACTION_DETAILS_DATE_FROM_ARGS}/" +
                        "{$TRANSACTION_DETAILS_DATE_TO_ARGS}/" +
                        "{$TRANSACTION_DETAILS_IN_STATISTICS_ARGS}",
            )
        }
        navigation(startDestination = PROFILE_NAVIGATION_ROUTE, route = PROFILE_ROOT_DESTINATION) {
            profileScreen(
                route = PROFILE_NAVIGATION_ROUTE,
                navigateToCurrencyListScreen = navController::navigateToCurrencyListScreen,
                navigateToBudgetScreen = navController::navigateToBudgetScreen,
                navigateToRegularIncomeScreen = {
                    navController.navigateToRegularTransactionListScreen(
                        transactionType = TransactionType.INCOME,
                    )
                },
                navigateToRegularExpenseScreen = {
                    navController.navigateToRegularTransactionListScreen(
                        transactionType = TransactionType.EXPENSE,
                    )
                },
                navigateToGoalsScreen = { /* no-op */ },
                navigateToSettingsScreen = navController::navigateToSettingsScreen,
            )
            budgetScreen(BUDGET_NAVIGATION_ROUTE) { navController.popBackStack() }
            regularTransactionListScreen(
                route =
                    "$REGULAR_TRANSACTION_LIST_NAVIGATION_ROUTE/" +
                        "{${NavigationConstants.TRANSACTION_TYPE_ARG}}",
                openCreationTransaction = navController::navigateToRegularTransactionCreationScreen,
                clickBack = navController::popBackStack,
            )
            regularTransactionCreationScreen(
                route =
                    "$REGULAR_TRANSACTION_CREATION_NAVIGATION_ROUTE/" +
                        "{${NavigationConstants.TRANSACTION_TYPE_ARG}}/" +
                        "{$REGULAR_TRANSACTION_ID_ARGS}",
                clickBack = navController::popBackStack,
                onCategoryClick = navController::navigateToCategoryListScreen,
                onCurrencyClick = navController::navigateToCurrencyListScreen,
                onSaveClick = navController::popBackStack,
            )
            settingsScreen(
                route = SETTINGS_NAVIGATION_ROUTE,
                clickBack = navController::popBackStack,
                onShowSnackBar = onShowSnackBar,
            )
        }
    }
}

private fun categoryNavigationRoute() =
    "$CATEGORY_NAVIGATION_ROUTE/" +
        "{${NavigationConstants.TRANSACTION_TYPE_ARG}}/" +
        "{$CATEGORY_DESTINATION_ARG}"

private const val INCOME_EXPENSE_ROOT_DESTINATION = "income_expense_root_destination"
private const val STATISTICS_ROOT_DESTINATION = "statistics_root_destination"
private const val PROFILE_ROOT_DESTINATION = "profile_root_destination"
private const val CATEGORY_ROOT_ROUTE = "category_root_route"
