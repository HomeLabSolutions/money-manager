package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CategoryCreationNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryCreationViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<CategoryCreationNavigator>() {

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) { navigator?.showError(exception) }
    }

    fun save(category: Category) {
        val receivedCategory = savedStateHandle.get<Category>("category")
        viewModelScope.launch(Dispatchers.IO + saveCategoryExceptionHandler) {
            if (receivedCategory == null || receivedCategory.id == DEFAULT_DATA_ID) {
                categoryInteractor.create(category)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                    param("create_category", "name: " + category.name)
                }
            } else {
                categoryInteractor.update(category)
            }
            navigator?.save()
        }
    }
}
