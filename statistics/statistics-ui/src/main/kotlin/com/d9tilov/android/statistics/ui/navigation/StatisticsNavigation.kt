package com.d9tilov.android.statistics.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.statistics.ui.StatisticsRoute

const val STATISTICS_NAVIGATION_ROUTE = "statistics_route"

interface StatisticsNavigator : BaseNavigator

interface StatisticsDetailsNavigator : StatisticsNavigator

fun NavController.navigateToStatistics(navOptions: NavOptions? = null) {
    this.navigate(STATISTICS_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.statisticsScreen(route: String) {
    composable(route = route) { StatisticsRoute() }
}
