package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.EditCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CategoryGroupEditViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor
) :
    BaseViewModel<EditCategoryDialogNavigator>() {

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        navigator?.showError(exception)
    }

    fun save(category: Category) = viewModelScope.launch(saveCategoryExceptionHandler) {
        categoryInteractor.update(category)
        navigator?.save()
    }
}
