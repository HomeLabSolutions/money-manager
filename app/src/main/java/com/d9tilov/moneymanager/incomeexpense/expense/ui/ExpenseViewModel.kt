package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.CategoryType
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
import io.reactivex.Observable
import timber.log.Timber
import java.math.BigDecimal
import java.util.Date

class ExpenseViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseViewModel<ExpenseNavigator>() {

    val categories = MutableLiveData<List<Category>>()
    val transactions = MutableLiveData<PagedList<BaseTransaction>>()
    val addTransactionEvent = SingleLiveEvent<Any>()

    init {
        categoryInteractor.getGroupedCategoriesByType(CategoryType.EXPENSE)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { categories.value = it }
            .addTo(compositeDisposable)
        transactionInteractor.getTransactionsByType(TransactionType.EXPENSE)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                transactions.value = it }
            .addTo(compositeDisposable)
    }

    fun generateData() {
        val numbers = IntArray(50) { it + 1 }.toList()
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
            .subscribe {
                Log.d("moggot", "generateData: subscribe")
            }
            .addTo(compositeDisposable)
    }

    fun clearData() {
        transactionInteractor.getTransactionsByType2(TransactionType.EXPENSE)
            .flatMapCompletable {
                Observable.fromIterable(it)
                    .flatMapCompletable {
                        transactionInteractor.removeTransaction(it) }
            }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun createTransaction(category: Category, number: Int): Transaction {
        val d = Date()
        val rand = (number..number + 3).random()
        val dateBefore = Date(d.time - rand * 24 * 3600 * 1000)
        return Transaction(
            type = TransactionType.EXPENSE,
            sum = number.toBigDecimal(),
            date = dateBefore,
            category = category
        )
    }

    fun openAllCategories() = navigator?.openCategoriesScreen()

    fun saveTransaction(category: Category, sum: BigDecimal) {
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
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        transactionInteractor.removeTransaction(transaction)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }
}
