package com.d9tilov.android.insights.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.d9tilov.android.designsystem.component.roundedBackComposable
import com.d9tilov.android.insights.presentation.InsightsRoute

const val INSIGHTS_NAVIGATION_ROUTE = "insights_route"

fun NavController.navigateToInsights() {
    navigate(INSIGHTS_NAVIGATION_ROUTE)
}

fun NavGraphBuilder.insightsScreen(onBackClick: () -> Unit) {
    roundedBackComposable(route = INSIGHTS_NAVIGATION_ROUTE) {
        InsightsRoute(onBackClick = onBackClick)
    }
}
