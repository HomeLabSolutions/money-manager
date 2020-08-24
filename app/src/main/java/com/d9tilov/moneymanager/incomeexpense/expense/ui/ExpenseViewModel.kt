package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.AsyncTask
import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.PagedList
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import com.d9tilov.moneymanager.transaction.domain.paging.ListDataSource
import com.d9tilov.moneymanager.transaction.domain.paging.PagingConfig
import com.d9tilov.moneymanager.transaction.domain.paging.UiThreadExecutor
import io.reactivex.Observable
import timber.log.Timber
import java.math.BigDecimal
import java.util.Date

class ExpenseViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseIncomeExpenseViewModel<ExpenseNavigator>() {

    init {
        categoryInteractor.getGroupedCategoriesByType(TransactionType.EXPENSE)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { categories.value = it }
            .addTo(compositeDisposable)

        transactionInteractor.getTransactionsByType(TransactionType.EXPENSE)
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

    fun generateData() {
        val numbers = IntArray(6) { it + 1 }.toList()
        Observable.fromIterable(numbers)
            .flatMapCompletable {
                categoryInteractor.getCategoryById((3..8).random().toLong())
                    .flatMapCompletable { category ->
                        transactionInteractor.addTransaction(
                            createTransaction(
                                category,
                                it
                            )
                        )
                    }
            }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {}
            .addTo(compositeDisposable)
    }

    fun clearData() {
        transactionInteractor.clearAll()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun createTransaction(category: Category, number: Int): Transaction {
        val d = Date()
        val rand = (number..number + 1).random()
        val dateBefore = Date(d.time - rand * 24 * 3600 * 1000)
        return Transaction(
            type = TransactionType.EXPENSE,
            sum = number.toBigDecimal(),
            date = dateBefore,
            category = category
        )
    }

    override fun saveTransaction(category: Category, sum: BigDecimal) {
        if (sum.signum() > 0) {
            transactionInteractor.addTransaction(
                Transaction(
                    type = TransactionType.EXPENSE,
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
}
