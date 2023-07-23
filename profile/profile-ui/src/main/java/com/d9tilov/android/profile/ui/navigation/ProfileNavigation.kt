package com.d9tilov.android.profile.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.budget.ui.BudgetRoute
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.currency.ui.CurrencyListRoute
import com.d9tilov.android.profile.ui.ProfileRoute
import com.d9tilov.android.settings.ui.SettingsRoute

const val profileNavigationRoute = "profile"
const val currencyNavigationRoute = "currency_list"
const val budgetNavigationRoute = "budget"
const val settingsNavigationRoute = "settings"

fun NavController.navigateToBudgetScreen(navOptions: NavOptions? = null) {
    this.navigate(budgetNavigationRoute, navOptions)
}

fun NavGraphBuilder.budgetScreen(clickBack: () -> Unit) {
    composable(route = budgetNavigationRoute) { BudgetRoute(clickBack = clickBack) }
}
fun NavController.navigateToCurrencyListScreen(navOptions: NavOptions? = null) {
    this.navigate(currencyNavigationRoute, navOptions)
}

fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(settingsNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(clickBack: () -> Unit) {
    composable(route = settingsNavigationRoute) { SettingsRoute(clickBack = clickBack) }
}

fun NavGraphBuilder.currencyScreen(clickBack: () -> Unit) {
    composable(route = currencyNavigationRoute) { CurrencyListRoute(clickBack = clickBack) }
}
fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileNavigationRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    navigateToCurrencyListScreen: () -> Unit,
    navigateToBudgetScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToGoalsScreen: () -> Unit
) {
    composable(route = profileNavigationRoute) {
        ProfileRoute(
            navigateToCurrencyListScreen = navigateToCurrencyListScreen,
            navigateToBudgetScreen = navigateToBudgetScreen,
            navigateToSettingsScreen = navigateToSettingsScreen,
            navigateToGoalsScreen = navigateToGoalsScreen
        )
    }
}

interface LogoutDialogNavigator : BaseNavigator {
    fun logout()
}
interface ProfileNavigator : BaseNavigator
