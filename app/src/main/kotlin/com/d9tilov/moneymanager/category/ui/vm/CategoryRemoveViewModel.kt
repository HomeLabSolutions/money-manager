package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RemoveCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryRemoveViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<RemoveCategoryDialogNavigator>() {

    fun remove(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        awaitAll(
            async { transactionInteractor.removeAllByCategory(category) },
            async { regularTransactionInteractor.removeAllByCategory(category) }
        )
        category.children.forEach { child ->
            awaitAll(
                async { transactionInteractor.removeAllByCategory(child) },
                async { regularTransactionInteractor.removeAllByCategory(child) }
            )
        }
        categoryInteractor.deleteCategory(category)
        withContext(Dispatchers.Main) { navigator?.closeDialog() }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_category", "name: " + category.name)
        }
    }
}
