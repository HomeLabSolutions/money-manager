package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.EditCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryGroupEditViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor
) : BaseViewModel<EditCategoryDialogNavigator>() {

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) { navigator?.showError(exception) }
    }

    fun save(category: Category) {
        viewModelScope.launch(Dispatchers.IO + saveCategoryExceptionHandler) {
            categoryInteractor.update(category)
        }
        navigator?.save()
    }
}
