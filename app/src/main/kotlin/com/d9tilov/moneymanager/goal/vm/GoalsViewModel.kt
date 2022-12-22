package com.d9tilov.moneymanager.goal.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.budget.data.model.BudgetData
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor,
    transactionInteractor: TransactionInteractor,
    goalInteractor: GoalInteractor
) : BaseViewModel<GoalsNavigator>() {

    val budget = budgetInteractor.get()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BudgetData.EMPTY)
    val goals = goalInteractor.getAll()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val accumulated =
        transactionInteractor.ableToSpendInFiscalPeriod()
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)

    fun savePrepopulateStatusAndSavedSum(sum: BigDecimal) = viewModelScope.launch {
        launch { userInteractor.prepopulateCompleted() }
        launch {
            val budget = budgetInteractor.get().first()
            budgetInteractor.update(budget.copy(saveSum = sum))
        }
    }

    fun insertSavedSum(sum: BigDecimal) {
        viewModelScope.launch(Dispatchers.IO) {
            val budget = budgetInteractor.get().first()
            budgetInteractor.update(budget.copy(saveSum = sum))
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
