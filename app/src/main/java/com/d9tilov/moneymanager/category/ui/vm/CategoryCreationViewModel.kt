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
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CategoryCreationViewModel @AssistedInject constructor(
    private val categoryInteractor: CategoryInteractor,
    @Assisted val savedStateHandle: SavedStateHandle,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<CategoryCreationNavigator>() {

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        navigator?.showError(exception)
    }

    fun save(category: Category) = viewModelScope.launch(saveCategoryExceptionHandler) {
        val receivedCategory = savedStateHandle.get<Category>("category")
        if (receivedCategory == null || receivedCategory.id == DEFAULT_DATA_ID) {
            categoryInteractor.create(category)
            navigator?.save()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param("create_category", "name: " + category.name)
            }
        } else {
            categoryInteractor.update(category)
            navigator?.save()
        }
    }
}
