package com.d9tilov.moneymanager.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.moneymanager.profile.ui.ProfileRoute

const val profileNavigationRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileNavigationRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    navigateToCurrencyListScreen: () -> Unit,
    navigateToBudgetScreen: () -> Unit
) {
    composable(route = profileNavigationRoute) {
        ProfileRoute(
            navigateToCurrencyListScreen = navigateToCurrencyListScreen,
            navigateToBudgetScreen = navigateToBudgetScreen
        )
    }
}
