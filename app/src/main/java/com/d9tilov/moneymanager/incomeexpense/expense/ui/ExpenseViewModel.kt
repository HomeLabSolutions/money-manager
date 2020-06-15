package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.events.SingleLiveEvent
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import timber.log.Timber
import java.math.BigDecimal

class ExpenseViewModel(
    categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) :
    BaseViewModel<ExpenseNavigator>() {

    val categories = MutableLiveData<List<Category>>()
    val transactions = MutableLiveData<List<Transaction>>()
    val addTransactionEvent = SingleLiveEvent<Any>()

    init {
        categoryInteractor.getExpenseCategories()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { categories.value = it }
            .addTo(compositeDisposable)
        transactionInteractor.getTransactionsByType(TransactionType.EXPENSE)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { transactions.value = it }
            .addTo(compositeDisposable)
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
