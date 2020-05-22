package com.d9tilov.moneymanager.base.ui.navigator

interface HomeNavigator : BaseNavigator

interface SplashNavigator : BaseNavigator {
    fun openHomeScreen()
    fun openAuthScreen()
}

interface IncomeNavigator : BaseNavigator

interface ExpenseNavigator : BaseNavigator

interface IncomeExpenseNavigator : BaseNavigator

interface SettingsNavigator : BaseNavigator

interface StatisticsNavigator : BaseNavigator
