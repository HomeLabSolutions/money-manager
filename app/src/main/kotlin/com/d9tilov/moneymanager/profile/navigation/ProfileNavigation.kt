package com.d9tilov.moneymanager.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.moneymanager.currency.navigation.currencyNavigationRoute
import com.d9tilov.moneymanager.profile.ui.ProfileRoute

const val profileNavigationRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileNavigationRoute, navOptions)
}

fun NavController.navigateToCurrencyList(navOptions: NavOptions? = null) {
    this.navigate(currencyNavigationRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(
    navigateToCurrencyList: () -> Unit,
) {
    composable(route = profileNavigationRoute) { ProfileRoute(navigateToCurrencyList = navigateToCurrencyList) }
}
