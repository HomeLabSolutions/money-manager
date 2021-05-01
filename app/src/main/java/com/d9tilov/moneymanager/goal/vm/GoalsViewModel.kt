package com.d9tilov.moneymanager.goal.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.EmptyResultSetException
import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import io.reactivex.Single
import java.math.BigDecimal
import java.util.Date

class GoalsViewModel @ViewModelInject constructor(
    private val budgetInteractor: BudgetInteractor,
    goalInteractor: GoalInteractor
) : BaseViewModel<GoalsNavigator>() {

    private val amount = MutableLiveData<BigDecimal>()
    private val goals = MutableLiveData<List<Goal>>()

    init {
        budgetInteractor.get()
            .onErrorResumeNext {
                if (it is EmptyResultSetException) {
                    val budgetData = BudgetData(
                        sum = BigDecimal.ZERO,
                        fiscalDay = Date().getFirstDayOfMonth()
                    )
                    budgetInteractor.create(budgetData).toSingleDefault(budgetData)
                        .doOnSuccess { data ->
                            amount.postValue(data.sum)
                        }
                } else {
                    Single.error(it)
                }
            }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({ amount.value = it.sum }, {})
            .addTo(compositeDisposable)

        goalInteractor.getAll()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({ goals.value = it }, {})
            .addTo(compositeDisposable)
    }

    fun getAmount(): LiveData<BigDecimal> = amount
    fun getGoals(): LiveData<List<Goal>> = goals
}
