package com.d9tilov.android.goals.domain.impl.mapper

import com.d9tilov.android.goals.data.model.GoalData
import com.d9tilov.android.goals.domain.model.Goal
import java.math.BigDecimal

fun Goal.toData() = with(this) {
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

fun GoalData.toDomain(currentSum: BigDecimal) = with(this) {
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
