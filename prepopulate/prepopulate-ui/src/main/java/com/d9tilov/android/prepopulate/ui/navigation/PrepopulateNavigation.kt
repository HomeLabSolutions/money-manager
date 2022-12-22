package com.d9tilov.android.prepopulate.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object MoneyManagerDestinations {
    const val PREPOPULATE = "prepopulate"
}

class MoneyManagerNavigationActions(navController: NavHostController) {
    val navigateToPrepopulate: () -> Unit = {
        navController.navigate(MoneyManagerDestinations.PREPOPULATE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
