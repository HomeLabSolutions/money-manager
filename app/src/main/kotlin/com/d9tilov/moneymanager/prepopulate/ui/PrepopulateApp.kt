package com.d9tilov.moneymanager.prepopulate.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.d9tilov.moneymanager.theme.MoneyManagerTheme

@Composable
fun PrepopulateApp() {
    MoneyManagerTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            MoneyManagerNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: MoneyManagerDestinations.PREPOPULATE
        PrepopulateNavGraph()
    }
}