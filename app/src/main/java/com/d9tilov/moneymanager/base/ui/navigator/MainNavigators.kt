package com.d9tilov.moneymanager.base.ui.navigator

interface SplashNavigator:BaseNavigator {
    fun openHomeScreen()
    fun openAuthScreen()
}

interface IncomeNavigator : BaseNavigator {}

interface ExpenseNavigator : BaseNavigator {}