package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.os.AsyncTask
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.events.SingleLiveEvent
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import com.d9tilov.moneymanager.transaction.domain.paging.ListDataSource
import com.d9tilov.moneymanager.transaction.domain.paging.PagingConfig
import com.d9tilov.moneymanager.transaction.domain.paging.UiThreadExecutor
import timber.log.Timber
import java.math.BigDecimal

class IncomeViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseViewModel<IncomeNavigator>() {

    val addTransactionEvent = SingleLiveEvent<Any>()
    val categories = MutableLiveData<List<Category>>()
    val transactions = MutableLiveData<PagedList<BaseTransaction>>()

    init {
        categoryInteractor.getGroupedCategoriesByType(TransactionType.INCOME)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { categories.value = it }
            .addTo(compositeDisposable)

        transactionInteractor.getTransactionsByType(TransactionType.INCOME)
            .map {
                val newList = mutableListOf<BaseTransaction>()
                var itemPosition = -1
                var itemHeaderPosition = itemPosition
                for (item in it) {
                    if (item is TransactionHeader) {
                        itemPosition++
                        itemHeaderPosition = itemPosition
                        newList.add(item.copy(headerPosition = itemHeaderPosition))
                    }
                    if (item is Transaction) {
                        itemPosition++
                        newList.add(item.copy(headerPosition = itemHeaderPosition))
                    }
                }
                convert(newList)
            }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe(
                { transactions.value = it },
                { Timber.tag(App.TAG).d("Error while getting transaction list: $it") }
            )
            .addTo(compositeDisposable)
    }

    private fun convert(list: List<BaseTransaction>): PagedList<BaseTransaction> {
        return PagedList.Builder(ListDataSource(list), PagingConfig.createConfig())
            .setNotifyExecutor(UiThreadExecutor())
            .setFetchExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            .build()
    }

    fun openAllCategories() = navigator?.openCategoriesScreen()

    fun saveTransaction(category: Category, sum: BigDecimal) {
        if (sum.signum() > 0) {
            transactionInteractor.addTransaction(
                Transaction(
                    type = TransactionType.INCOME,
                    sum = sum,
                    category = category
                )
            )
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(
                    { addTransactionEvent.call() },
                    { Timber.tag(App.TAG).d("error = ${it.message}") }
                )
                .addTo(compositeDisposable)
        } else {
            navigator?.showEmptySumError()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        navigator?.openRemoveConfirmationDialog(transaction)
    }
}
