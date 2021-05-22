package com.d9tilov.moneymanager.base.ui.navigator

import androidx.annotation.DrawableRes
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.transaction.TransactionType

interface HomeNavigator : BaseNavigator

interface SplashNavigator : BaseNavigator {
    fun openHomeScreen()
    fun openPrepopulate()
    fun openAuthScreen()
}

interface CurrencyNavigator : BaseNavigator {
    fun skip()
    fun showError()
}

interface BudgetAmountNavigator : BaseNavigator {
    fun goToRegularIncomeScreen()
    fun showError()
}

interface RegularTransactionCreatedNavigator : BaseNavigator {
    fun back()
}

interface BaseRegularIncomeExpenseNavigator : BaseNavigator

interface RegularExpenseNavigator : BaseRegularIncomeExpenseNavigator
interface RegularIncomeNavigator : BaseRegularIncomeExpenseNavigator

interface GoalsNavigator : BaseNavigator
interface CreatedGoalNavigator : BaseNavigator {
    fun back()
}

interface RemoveGoalNavigator : BaseNavigator {
    fun closeDialog()
}

interface IncomeNavigator : BaseIncomeExpenseNavigator

interface ExpenseNavigator : BaseIncomeExpenseNavigator

interface BaseIncomeExpenseNavigator : BaseNavigator {
    fun openCategoriesScreen()
    fun showEmptySumError()
}

interface IncomeExpenseNavigator : BaseNavigator

interface ProfileNavigator : BaseNavigator

interface StatisticsNavigator : BaseNavigator

interface RemoveTransactionDialogNavigator : BaseNavigator {
    fun remove()
}

interface CategoryUnionDialogNavigator : BaseNavigator {
    fun accept()
    fun showError(error: Throwable)
    fun cancel()
}

interface RemoveCategoryDialogNavigator : BaseNavigator {
    fun closeDialog()
}

interface EditCategoryDialogNavigator : BaseNavigator {
    fun showError(error: Throwable)
    fun save()
    fun closeDialog()
}

interface RemoveSubCategoryDialogNavigator : BaseNavigator {
    fun closeDialog()
    fun closeDialogAndGoToCategory()
}

interface LogoutDialogNavigator : BaseNavigator {
    fun logout()
}

interface CategoryNavigator : BaseNavigator {
    fun openSubCategoryScreen(category: Category)
    fun openCreateCategoryScreen(category: Category? = null)
    fun openRemoveDialog(category: Category)
    fun backToEditTransactionScreen(category: Category)
    fun backToRegularTransactionCreationScreen(category: Category)
    fun backToMainScreen(transactionType: TransactionType)
}

interface CategoryCreationNavigator : BaseNavigator {
    fun save()
    fun showError(error: Throwable)
}

interface CategorySetNavigator : BaseNavigator {
    fun save(@DrawableRes icon: Int)
}

interface EditTransactionNavigator : BaseNavigator {
    fun save()
}

interface SubCategoryNavigator : BaseNavigator {
    fun backToEditTransactionScreen(category: Category)
    fun backToMainScreen(transactionType: TransactionType)
    fun openCreateCategoryScreen(category: Category? = null)
    fun backToRegularTransactionCreationScreen(category: Category)
    fun openRemoveDialog(subCategory: Category)
}

interface SettingsNavigator : BaseNavigator
