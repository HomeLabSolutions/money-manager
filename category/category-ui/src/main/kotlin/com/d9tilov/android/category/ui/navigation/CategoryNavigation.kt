package com.d9tilov.android.category.ui.navigation

import androidx.annotation.DrawableRes
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.CategoryGroupItem
import com.d9tilov.android.category.ui.CategoryListRoute
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.toType

interface CategoryCreationNavigator : BaseNavigator {
    fun save()
    fun showError(error: Throwable)
}

interface CategorySetNavigator : BaseNavigator {
    fun save(@DrawableRes icon: Int)
}

interface SubCategoryNavigator : BaseNavigator {
    fun backToEditTransactionScreen(category: Category)
    fun backToEditRegularTransactionScreen(category: Category)
    fun backToMainScreen(category: Category)
    fun openCreateCategoryScreen(category: Category)
    fun openRemoveDialog(subCategory: Category)
}

interface RemoveCategoryDialogNavigator : BaseNavigator {
    fun closeDialog()
}

interface CategoryUnionDialogNavigator : BaseNavigator {
    fun accept()
    fun showError(error: Throwable)
    fun cancel()
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

interface CategoryNavigator : BaseNavigator {
    fun openSubCategoryScreen(category: Category)
    fun openCreateCategoryScreen(category: Category)
    fun openRemoveDialog(category: Category)
    fun backToEditTransactionScreen(category: Category)
    fun backToEditRegularTransactionScreen(category: Category)
    fun backToMainScreen(category: Category)
}

interface CategoryGroupSetNavigator : BaseNavigator {
    fun openCategoryGroup(item: CategoryGroupItem)
}

const val categoryNavigationRoute = "category"
internal const val transactionTypeArg = "transactionType"

internal class CategoryArgs(val transactionType: TransactionType) {
    constructor(savedStateHandle: SavedStateHandle) :
            this((checkNotNull(savedStateHandle[transactionTypeArg]) as Int).toType())
}

fun NavController.navigateToCategoryListScreen(
    transactionType: TransactionType,
    navOptions: NavOptions? = null,
) {
    this.navigate("$categoryNavigationRoute${transactionType.value}", navOptions)
}

fun NavGraphBuilder.categoryListScreen() {
    composable(
        route = "$categoryNavigationRoute{$transactionTypeArg}",
        arguments = listOf(
            navArgument(transactionTypeArg) { type = NavType.IntType },
        )
    ) { CategoryListRoute() }
}
