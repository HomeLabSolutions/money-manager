package com.d9tilov.android.profile.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.profile.ui.ProfileRoute

const val PROFILE_NAVIGATION_ROUTE = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(PROFILE_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.profileScreen(
    route: String,
    navigateToCurrencyListScreen: () -> Unit,
    navigateToBudgetScreen: () -> Unit,
    navigateToRegularIncomeScreen: () -> Unit,
    navigateToRegularExpenseScreen: () -> Unit,
    navigateToGoalsScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    composable(route = route) {
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
