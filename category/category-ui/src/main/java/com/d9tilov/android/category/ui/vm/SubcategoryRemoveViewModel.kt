package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.ui.navigation.RemoveSubCategoryDialogNavigator
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SubcategoryRemoveViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<RemoveSubCategoryDialogNavigator>() {

    fun remove(subcategory: Category) = viewModelScope.launch(Dispatchers.IO) {
        awaitAll(
            async { regularTransactionInteractor.removeAllByCategory(subcategory) },
            async { transactionInteractor.removeAllByCategory(subcategory) }
        )
        val parentRemoved = categoryInteractor.deleteSubCategory(subcategory)
        withContext(Dispatchers.Main) {
            if (parentRemoved) navigator?.closeDialogAndGoToCategory()
            else navigator?.closeDialog()
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_subcategory", "name: " + subcategory.name)
        }
    }

    fun removeFromGroup(subcategory: Category) = viewModelScope.launch(Dispatchers.IO) {
        val parentRemoved = categoryInteractor.deleteFromGroup(subcategory)
        withContext(Dispatchers.Main) {
            if (parentRemoved) navigator?.closeDialogAndGoToCategory()
            else navigator?.closeDialog()
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_subcategory_from_group", "name: " + subcategory.name)
        }
    }
}
