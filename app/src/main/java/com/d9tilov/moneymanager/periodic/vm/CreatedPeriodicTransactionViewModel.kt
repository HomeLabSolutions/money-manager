package com.d9tilov.moneymanager.periodic.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.FixedCreatedNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.periodic.domain.PeriodicTransactionInteractor
import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import java.math.BigDecimal
import java.util.Date

class CreatedPeriodicTransactionViewModel @ViewModelInject constructor(
    private val fixedTransactionInteractor: PeriodicTransactionInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) :
    BaseViewModel<FixedCreatedNavigator>() {

    var sum = MutableLiveData<BigDecimal>()
    var category = MutableLiveData<Category?>()
    var periodType = MutableLiveData<PeriodType>()
    var startDate = MutableLiveData<Date>()
    var description = MutableLiveData<String>()
    var pushEnabled = MutableLiveData<Boolean>()
    var weekDaysSelected = MutableLiveData<Int>()

    fun save() {
        val transactionType =
            requireNotNull(savedStateHandle.get<TransactionType>("transactionType"))
        val fixedTransaction = savedStateHandle.get<PeriodicTransaction>("fixed_transaction")
        if (fixedTransaction == null) {
            fixedTransactionInteractor.insert(
                PeriodicTransaction(
                    type = transactionType,
                    sum = sum.value!!,
                    category = category.value!!,
                    startDate = startDate.value!!,
                    periodType = periodType.value!!,
                    description = description.value!!,
                    pushEnable = pushEnabled.value!!,
                    dayOfWeek = if (periodType.value != PeriodType.WEEK) 0 else weekDaysSelected.value!!
                )
            )
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { navigator?.back() }
                .addTo(compositeDisposable)
        } else {
            fixedTransactionInteractor.update(
                fixedTransaction.copy(
                    type = transactionType,
                    sum = sum.value!!,
                    category = category.value!!,
                    startDate = startDate.value!!,
                    periodType = periodType.value!!,
                    description = description.value!!,
                    pushEnable = pushEnabled.value!!,
                    dayOfWeek = if (periodType.value != PeriodType.WEEK) 0 else weekDaysSelected.value!!
                )
            )
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { navigator?.back() }
                .addTo(compositeDisposable)
        }
    }
}
