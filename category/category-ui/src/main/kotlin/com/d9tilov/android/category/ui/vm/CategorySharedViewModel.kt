package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.ViewModel
import com.d9tilov.android.core.constants.DataConstants.NO_RES_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CategorySharedViewModel : ViewModel() {
    private val _categoryIconId = MutableStateFlow(NO_RES_ID)
    val categoryIconId: StateFlow<Int> = _categoryIconId

    fun setId(id: Int) {
        _categoryIconId.update { id }
    }
}
