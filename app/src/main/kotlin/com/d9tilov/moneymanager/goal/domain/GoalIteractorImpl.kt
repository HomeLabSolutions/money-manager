package com.d9tilov.moneymanager.goal.domain

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import com.d9tilov.moneymanager.goal.domain.mapper.GoalDomainMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GoalIteractorImpl(
    private val goalRepo: GoalRepo,
    private val currencyInteractor: CurrencyInteractor,
    private val budgetInteractor: BudgetInteractor,
    private val goalDomainMapper: GoalDomainMapper
) : GoalInteractor {

    override suspend fun insert(goal: Goal) {
        val currency = currencyInteractor.getMainCurrency()
        goalRepo.insert(goalDomainMapper.toData(goal.copy(currencyCode = currency.code)))
    }

    override fun getAll(): Flow<List<Goal>> {
        return goalRepo.getAll().flatMapLatest { goals ->
            budgetInteractor.get()
                .map { budget ->
                    val goalList = mutableListOf<Goal>()
                    for (goal in goals) {
                        var currentSum = budget.sum
                        if (budget.sum >= goal.targetSum) {
                            currentSum = goal.targetSum
                        }
                        val newGoal = goalDomainMapper.toDomain(goal, currentSum)
                        goalList.add(newGoal)
                    }
                    goalList
                }
        }
    }

    override suspend fun update(goal: Goal) {
        goalRepo.update(goalDomainMapper.toData(goal))
    }

    override suspend fun delete(goal: Goal) {
        goalRepo.delete(goalDomainMapper.toData(goal))
    }
}
