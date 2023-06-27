package com.d9tilov.android.statistics.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common_android.ui.base.BaseNavigator
import com.d9tilov.android.statistics.ui.StatisticsRoute

const val statisticsNavigationRoute = "statistics"

interface StatisticsDetailsNavigator : StatisticsNavigator

fun NavController.navigateToStatistics(navOptions: NavOptions? = null) {
    this.navigate(statisticsNavigationRoute, navOptions)
}

fun NavGraphBuilder.statisticsScreen() {
    composable(route = statisticsNavigationRoute) { StatisticsRoute() }
}
