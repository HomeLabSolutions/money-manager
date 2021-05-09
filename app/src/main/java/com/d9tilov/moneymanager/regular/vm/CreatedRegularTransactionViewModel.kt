package com.d9tilov.moneymanager.regular.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RegularTransactionCreatedNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class CreatedRegularTransactionViewModel @ViewModelInject constructor(
    private val regularTransactionInteractor: RegularTransactionInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) :
    BaseViewModel<RegularTransactionCreatedNavigator>() {

    var sum = MutableLiveData<BigDecimal>()
    var category = MutableLiveData<Category?>()
    var periodType = MutableLiveData<PeriodType>()
    var startDate = MutableLiveData<Date>()
    var description = MutableLiveData<String>()
    var pushEnabled = MutableLiveData<Boolean>()
    var weekDaysSelected = MutableLiveData<Int>()

    fun save() = viewModelScope.launch {
        val transactionType =
            requireNotNull(savedStateHandle.get<TransactionType>("transactionType"))
        val regularTransaction = savedStateHandle.get<RegularTransaction>("regular_transaction")
        if (regularTransaction == null) {
            regularTransactionInteractor.insert(
                RegularTransaction(
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
        } else {
            regularTransactionInteractor.update(
                regularTransaction.copy(
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
        }
        navigator?.back()
    }
}
