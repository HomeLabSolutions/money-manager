package com.d9tilov.moneymanager.goal.domain.mapper

import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import java.math.BigDecimal
import javax.inject.Inject

class GoalDomainMapper @Inject constructor() {

    fun toData(goal: Goal) = with(goal) {
        GoalData(
            id,
            clientId,
            currencyCode,
            name,
            targetSum,
            createdDate,
            description
        )
    }

    fun toDomain(goalData: GoalData, currentSum: BigDecimal) = with(goalData) {
        Goal(
            id,
            clientId,
            currencyCode,
            name,
            targetSum,
            currentSum,
            createdDate,
            description
        )
    }
}
