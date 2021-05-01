package com.d9tilov.moneymanager.goal.domain

import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import com.d9tilov.moneymanager.goal.domain.mapper.GoalDomainMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import java.math.BigDecimal

class GoalIteractorImpl(
    private val goalRepo: GoalRepo,
    private val budgetInteractor: BudgetInteractor,
    private val goalDomainMapper: GoalDomainMapper
) : GoalInteractor {
    override fun insert(goal: Goal): Completable = goalRepo.insert(goalDomainMapper.toData(goal))

    override fun getAll(): Flowable<List<Goal>> {
        return Flowable.combineLatest(
            budgetInteractor.get().toFlowable(),
            goalRepo.getAll(),
            { budget, list ->
                val goalList = mutableListOf<Goal>()
                for (goal in list) {

                    var currentSum = budget.sum
                    if (budget.sum >= goal.targetSum) {
                        currentSum = goal.targetSum
                    }
                    val newGoal = goalDomainMapper.toDomain(goal, currentSum)
                    goalList.add(newGoal)
                }
                goalList
            }
        )
    }

    override fun update(goal: Goal): Completable = goalRepo.update(goalDomainMapper.toData(goal))

    override fun delete(goal: Goal): Completable = goalRepo.delete(goalDomainMapper.toData(goal))
}
