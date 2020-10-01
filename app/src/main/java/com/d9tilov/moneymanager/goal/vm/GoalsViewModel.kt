package com.d9tilov.moneymanager.goal.vm

import androidx.hilt.lifecycle.ViewModelInject
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
    private val goalInteractor: GoalInteractor
) :
    BaseViewModel<GoalsNavigator>() {

    val amount by lazy { MutableLiveData<BigDecimal>() }
    val goals by lazy { MutableLiveData<List<Goal>>() }

    init {
        budgetInteractor.get()
            .onErrorResumeNext {
                if (it is EmptyResultSetException) {
                    val budgetData = BudgetData(
                        sum = BigDecimal.ZERO,
                        fiscalDay = Date().getFirstDayOfMonth()
                    )
                    budgetInteractor.create(budgetData).toSingleDefault(budgetData)
                        .doOnSuccess { budgetData ->
                            amount.postValue(budgetData.sum)
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
}
