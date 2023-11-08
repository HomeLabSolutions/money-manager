package com.d9tilov.moneymanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.d9tilov.android.category.ui.navigation.categoryCreationScreen
import com.d9tilov.android.category.ui.navigation.categoryIconGridScreen
import com.d9tilov.android.category.ui.navigation.categoryIconListScreen
import com.d9tilov.android.category.ui.navigation.categoryListScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryCreationScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryIconGridScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryIconListScreen
import com.d9tilov.android.category.ui.navigation.navigateToCategoryListScreen
import com.d9tilov.android.incomeexpense.navigation.incomeExpenseNavigationRoute
import com.d9tilov.android.incomeexpense.navigation.incomeExpenseScreen
import com.d9tilov.android.profile.ui.navigation.budgetScreen
import com.d9tilov.android.profile.ui.navigation.currencyScreen
import com.d9tilov.android.profile.ui.navigation.navigateToBudgetScreen
import com.d9tilov.android.profile.ui.navigation.navigateToCurrencyListScreen
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
    startDestination: String = incomeExpenseNavigationRoute,
) {

    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        incomeExpenseScreen(
            onCurrencyClick = navController::navigateToCurrencyListScreen,
            onAllCategoryClick = navController::navigateToCategoryListScreen
        )
        categoryListScreen(
            clickBack = navController::popBackStack,
            openCategory = navController::navigateToCategoryCreationScreen
        )
        categoryCreationScreen(
            clickBack = navController::popBackStack,
            clickOnCategoryIcon = navController::navigateToCategoryIconListScreen,
            clickSave = navController::popBackStack
        )
        categoryIconListScreen(
            clickBack = navController::popBackStack,
            onItemClick = navController::navigateToCategoryIconGridScreen
        )
        categoryIconGridScreen(
            clickBack = navController::popBackStack,
            onIconClick = {}
        )
        statisticsScreen()
        profileScreen(
            navigateToCurrencyListScreen = navController::navigateToCurrencyListScreen,
            navigateToBudgetScreen = navController::navigateToBudgetScreen,
            navigateToSettingsScreen = navController::navigateToSettingsScreen,
            navigateToGoalsScreen = { /* no-op */ }
        )
        currencyScreen { navController.popBackStack() }
        budgetScreen { navController.popBackStack() }
        settingsScreen(clickBack = navController::popBackStack, onShowSnackBar = onShowSnackBar)
    }
}
