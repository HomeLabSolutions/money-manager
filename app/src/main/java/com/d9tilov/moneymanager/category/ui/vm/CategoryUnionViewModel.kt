package com.d9tilov.moneymanager.category.ui.vm

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CategoryUnionDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CategoryUnionViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val firebaseAnalytics: FirebaseAnalytics,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<CategoryUnionDialogNavigator>() {

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        navigator?.showError(exception)
    }

    fun addToGroup(categoryItem: Category, parentCategory: Category) =
        viewModelScope.launch(saveCategoryExceptionHandler) {
            categoryInteractor.update(categoryItem.copy(parent = parentCategory))
            navigator?.accept()
        }

    fun createGroup(categoryItem1: Category, categoryItem2: Category, groupedCategory: Category) =
        viewModelScope.launch(saveCategoryExceptionHandler) {
            Log.d("moggot", "launch")
            val parentId = categoryInteractor.create(groupedCategory)
            Log.d("moggot", "parentId: " + parentId)
            val category = categoryInteractor.getCategoryById(parentId)
            Log.d("moggot", "category: " + category)
            categoryInteractor.update(categoryItem1.copy(parent = category))
            Log.d("moggot", "update1")
            categoryInteractor.update(categoryItem2.copy(parent = category))
            Log.d("moggot", "update2")
            navigator?.accept()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param("create_category", "name: " + groupedCategory.name)
            }
        }

    fun cancel() {
        navigator?.cancel()
    }
}
