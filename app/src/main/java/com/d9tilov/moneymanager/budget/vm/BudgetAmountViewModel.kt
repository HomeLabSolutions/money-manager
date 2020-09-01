package com.d9tilov.moneymanager.budget.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import java.math.BigDecimal

class BudgetAmountViewModel @ViewModelInject constructor(private val budgetInteractor: BudgetInteractor) :
    BaseViewModel<BudgetAmountNavigator>() {

    var amount = MutableLiveData<BigDecimal>()
    private lateinit var budget: BudgetData

    init {
        budgetInteractor.get()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { budget ->
                amount.value = budget.sum
                this.budget = budget
            }
            .addTo(compositeDisposable)
    }

    fun saveBudgetAmount(sum: BigDecimal) {
        budgetInteractor.update(budget.copy(sum = sum))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { this.amount.value = sum }
            .addTo(compositeDisposable)
    }
}
