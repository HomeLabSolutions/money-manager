package com.d9tilov.android.goals.domain.impl.mapper

import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.goals.domain.model.GoalData
import com.d9tilov.android.goals.domain.model.Goal
import java.math.BigDecimal

fun Goal.toData() = with(this) {
    GoalData(
        id,
        clientId,
        currencyCode,
        name,
        targetSum,
        createdDate.toLocalDateTime(),
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
        createdDate.toMillis(),
        description
    )
}
