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
import com.d9tilov.android.category.ui.CategoryCreationRoute
import com.d9tilov.android.category.ui.CategoryGroupIconListRoute
import com.d9tilov.android.category.ui.CategoryListRoute
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.toType

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
const val categoryCreationNavigationRoute = "category_creation"
const val categoryIconListNavigationRoute = "category_icon_list"
internal const val transactionTypeArg = "transaction_type"
internal const val categoryIdArg = "category_id"
internal const val categoryGroupId = "category_group_id"

internal sealed class CategoryArgs {
    class CategoryListArgs(val transactionType: TransactionType) {
        constructor(savedStateHandle: SavedStateHandle) :
                this((checkNotNull(savedStateHandle[transactionTypeArg]) as Int).toType())
    }

    class CategoryCreationArgs(val categoryId: Long, val transactionType: TransactionType) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(
                    checkNotNull(savedStateHandle[categoryIdArg]) as Long,
                    (checkNotNull(savedStateHandle[transactionTypeArg]) as Int).toType()
                )
    }
}

fun NavController.navigateToCategoryListScreen(
    transactionType: TransactionType,
    navOptions: NavOptions? = null,
) {
    this.navigate("$categoryNavigationRoute/${transactionType.value}", navOptions)
}

fun NavGraphBuilder.categoryListScreen(clickBack: () -> Unit, openCategory: (Long, TransactionType) -> Unit) {
    composable(
        route = "$categoryNavigationRoute/{$transactionTypeArg}",
        arguments = listOf(navArgument(transactionTypeArg) { type = NavType.IntType })
    ) { CategoryListRoute(openCategory = openCategory, clickBack = clickBack) }
}

fun NavController.navigateToCategoryCreationScreen(
    categoryId: Long,
    transactionType: TransactionType,
    navOptions: NavOptions? = null,
) {
    this.navigate("$categoryCreationNavigationRoute/${categoryId}/${transactionType.value}", navOptions)
}

fun NavGraphBuilder.categoryCreationScreen(
    clickBack: () -> Unit,
    clickOnCategoryIcon: () -> Unit,
    clickSave: () -> Unit,
) {
    composable(
        route = "$categoryCreationNavigationRoute/{$categoryIdArg}/{${transactionTypeArg}}",
        arguments = listOf(
            navArgument(categoryIdArg) { type = NavType.LongType },
            navArgument(transactionTypeArg) { type = NavType.IntType },
        )
    ) {
        CategoryCreationRoute(
            clickSave = clickSave,
            clickBack = clickBack,
            clickOnCategoryIcon = clickOnCategoryIcon
        )
    }
}

fun NavController.navigateToCategoryIconListScreen(navOptions: NavOptions? = null) {
    this.navigate(categoryIconListNavigationRoute, navOptions)
}

fun NavGraphBuilder.categoryIconListScreen(
    clickBack: () -> Unit,
    onItemClick: (CategoryGroupItem) -> Unit,
) {
    composable(route = categoryIconListNavigationRoute) {
        CategoryGroupIconListRoute(
            onItemClick = onItemClick,
            clickBack = clickBack
        )
    }
}
