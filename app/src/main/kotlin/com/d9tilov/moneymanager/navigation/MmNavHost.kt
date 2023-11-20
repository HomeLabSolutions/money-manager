package com.d9tilov.moneymanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.d9tilov.android.category.ui.navigation.categoryCreationScreen
import com.d9tilov.android.category.ui.navigation.categoryIconGridScreen
import com.d9tilov.android.category.ui.navigation.categoryIconListScreen
import com.d9tilov.android.category.ui.navigation.categoryListScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryCreationScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryIconGridScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryIconListScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryListScreen
import com.d9tilov.android.currency.domain.model.CurrencyArgs.currencyCodeArgs
import com.d9tilov.android.currency.ui.navigation.currencyScreen
import com.d9tilov.android.currency.ui.navigation.navigateToCurrencyListScreen
import com.d9tilov.android.incomeexpense.navigation.incomeExpenseNavigationRoute
import com.d9tilov.android.incomeexpense.navigation.incomeExpenseScreen
import com.d9tilov.android.profile.ui.navigation.budgetScreen
import com.d9tilov.android.profile.ui.navigation.navigateToBudgetScreen
import com.d9tilov.android.profile.ui.navigation.navigateToSettingsScreen
import com.d9tilov.android.profile.ui.navigation.profileScreen
import com.d9tilov.android.profile.ui.navigation.settingsScreen
import com.d9tilov.android.statistics.ui.navigation.statisticsScreen
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
        startDestination = HOME_DESTINATION,
        modifier = modifier,
    ) {
        navigation(startDestination = incomeExpenseNavigationRoute, route = HOME_DESTINATION) {
            incomeExpenseScreen(
                onCurrencyClick = navController::navigateToCurrencyListScreen,
                onAllCategoryClick = navController::navigateToCategoryListScreen
            )
            categoryListScreen(
                clickBack = navController::popBackStack,
                openCategory = navController::navigateToCategoryCreationScreen
            )

            categoryCreationScreen(
                navController = navController,
                clickBack = navController::popBackStack,
                openCategoryGroupIconList = navController::navigateToCategoryIconListScreen,
                openCategoryIconGrid = { navController.navigateToCategoryIconGridScreen(-1) }
            )
            categoryIconListScreen(
                clickBack = navController::popBackStack,
                onItemClick = navController::navigateToCategoryIconGridScreen
            )
            categoryIconGridScreen(
                navController = navController,
                clickBack = navController::popBackStack,
                onIconClick = { isPremium ->
                    val destination = navController.previousBackStackEntry?.destination?.id
                    destination?.let { id -> navController.popBackStack(id, inclusive = isPremium) }
                }
            )
            statisticsScreen()
            profileScreen(
                navigateToCurrencyListScreen = { navController.navigateToCurrencyListScreen(true) },
                navigateToBudgetScreen = navController::navigateToBudgetScreen,
                navigateToSettingsScreen = navController::navigateToSettingsScreen,
                navigateToGoalsScreen = { /* no-op */ }
            )
            currencyScreen(
                clickBack = navController::popBackStack,
                onChooseCurrency = { currencyCode ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(currencyCodeArgs, currencyCode)
                    navController.popBackStack()
                })
            budgetScreen { navController.popBackStack() }
            settingsScreen(clickBack = navController::popBackStack, onShowSnackBar = onShowSnackBar)
        }
    }
}

private const val HOME_DESTINATION = "home"
