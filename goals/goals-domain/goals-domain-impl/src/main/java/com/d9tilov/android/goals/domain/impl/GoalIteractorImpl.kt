package com.d9tilov.android.goals.domain.impl

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.goals.domain.contract.GoalRepo
import com.d9tilov.android.goals.domain.contract.GoalInteractor
import com.d9tilov.android.goals.domain.impl.mapper.toData
import com.d9tilov.android.goals.domain.model.Goal

class GoalIteractorImpl(
    private val goalRepo: GoalRepo,
    private val currencyInteractor: CurrencyInteractor
) : GoalInteractor {

    override suspend fun insert(goal: Goal) {
        val currency = currencyInteractor.getMainCurrency()
        goalRepo.insert(goal.copy(currencyCode = currency.code).toData())
    }

    override suspend fun update(goal: Goal) {
        goalRepo.update(goal.toData())
    }

    override suspend fun delete(goal: Goal) {
        goalRepo.delete(goal.toData())
    }
}
