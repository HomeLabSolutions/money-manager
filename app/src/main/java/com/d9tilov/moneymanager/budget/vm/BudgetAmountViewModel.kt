package com.d9tilov.moneymanager.budget.vm

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.data.local.exceptions.EmptyDbDataException
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BudgetAmountViewModel @Inject constructor(private val budgetInteractor: BudgetInteractor) :
    BaseViewModel<BudgetAmountNavigator>() {

    val budgetData = budgetInteractor.get().catch {
        if (it is EmptyDbDataException) {
            budgetInteractor.create(
                BudgetData(
                    sum = BigDecimal.ZERO,
                    createdDate = Date(),
                    fiscalDay = getFirstDayOfMonth()
                )
            )
        }
    }.asLiveData()

    fun saveBudgetAmount(sum: BigDecimal) = viewModelScope.launch(Dispatchers.IO) {
        budgetInteractor.get().map { budgetInteractor.update(it.copy(sum = sum)) }.collect()
    }
}
