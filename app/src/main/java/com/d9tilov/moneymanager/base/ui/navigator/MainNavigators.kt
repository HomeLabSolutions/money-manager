package com.d9tilov.moneymanager.base.ui.navigator

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

interface HomeNavigator : BaseNavigator

interface SplashNavigator : BaseNavigator {
    fun openHomeScreen()
    fun openAuthScreen()
}

interface IncomeNavigator : BaseNavigator

interface ExpenseNavigator : BaseNavigator {
    fun openCategoriesScreen()
    fun openRemoveConfirmationDialog(transaction: Transaction)
}

interface IncomeExpenseNavigator : BaseNavigator

interface SettingsNavigator : BaseNavigator

interface StatisticsNavigator : BaseNavigator

interface RemoveTransactionNavigator : BaseNavigator {
    fun remove()
    fun cancel()
}

interface CategoryNavigator : BaseNavigator {
    fun openSubCategoryScreen(category: Category)
    fun openCreateCategoryDialog()
}

interface EditTransactionNavigator : BaseNavigator {
    fun save()
    fun back()
    fun openCategories()
    fun openCalendar()
}

interface SubCategoryNavigator : BaseNavigator {
    fun openCreateCategoryDialog()
}
