package com.d9tilov.android.profile.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.profile.ui.ProfileRoute

const val profileNavigationRoute = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileNavigationRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    navigateToCurrencyListScreen: () -> Unit,
    navigateToBudgetScreen: () -> Unit,
    navigateToRegularIncomeScreen: () -> Unit,
    navigateToRegularExpenseScreen: () -> Unit,
    navigateToGoalsScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    composable(route = profileNavigationRoute) {
        ProfileRoute(
            navigateToCurrencyListScreen = navigateToCurrencyListScreen,
            navigateToBudgetScreen = navigateToBudgetScreen,
            navigateToRegularIncomeScreen = navigateToRegularIncomeScreen,
            navigateToRegularExpenseScreen = navigateToRegularExpenseScreen,
            navigateToGoalsScreen = navigateToGoalsScreen,
            navigateToSettingsScreen = navigateToSettingsScreen,
        )
    }
}

interface LogoutDialogNavigator : BaseNavigator {
    fun logout()
}
interface ProfileNavigator : BaseNavigator
