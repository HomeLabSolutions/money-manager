package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
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
    var mainSum: StringBuilder = StringBuilder()
        private set

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
        // transactionInteractor.getTransactionById(1)
        //     .subscribeOn(ioScheduler)
        //     .observeOn(uiScheduler)
        //     .subscribe { Timber.tag(App.TAG).d("tr = $it") }
        //     .addTo(compositeDisposable)
    }

    fun updateNum(num: String) {
        mainSum = StringBuilder(num)
    }

    fun tapNum(num: String): String {
        mainSum.append(num)
        return mainSum.toString()
    }

    fun removeNum(): String {
        return if (mainSum.isNotEmpty() && mainSum.toString() != "0") {
            mainSum.deleteCharAt(mainSum.length - 1)
            if (mainSum.toString().isEmpty()) {
                "0"
            } else {
                mainSum.toString()
            }
        } else {
            "0"
        }
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
                .subscribe({}, { Timber.tag(App.TAG).d("error = ${it.message}") })
                .addTo(compositeDisposable)
        }
    }
}
