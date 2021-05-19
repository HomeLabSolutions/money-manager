package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RemoveCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryRemoveViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<RemoveCategoryDialogNavigator>() {

    fun remove(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionInteractor.removeAllByCategory(category)
            category.children.forEach { transactionInteractor.removeAllByCategory(it) }
            categoryInteractor.deleteCategory(category)
        }
        navigator?.closeDialog()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_category", "name: " + category.name)
        }
    }
}
