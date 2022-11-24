package com.d9tilov.moneymanager.statistics.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.moneymanager.statistics.ui.StatisticsRoute

const val statisticsNavigationRoute = "statistics"

fun NavController.navigateToStatistics(navOptions: NavOptions? = null) {
    this.navigate(statisticsNavigationRoute, navOptions)
}

fun NavGraphBuilder.statisticsScreen() {
    composable(route = statisticsNavigationRoute) { StatisticsRoute() }
}
