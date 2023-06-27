package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.ui.navigation.EditCategoryDialogNavigator
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
