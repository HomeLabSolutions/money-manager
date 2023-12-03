package com.d9tilov.android.goals.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.goals.ui.GoalsRoute

const val goalsNavigationRoute = "goals"

fun NavController.navigateToGoalsScreen(navOptions: NavOptions? = null) {
    this.navigate(goalsNavigationRoute, navOptions)
}

fun NavGraphBuilder.goalsScreen(clickBack: () -> Unit) {
    composable(route = goalsNavigationRoute) { GoalsRoute() }
}
