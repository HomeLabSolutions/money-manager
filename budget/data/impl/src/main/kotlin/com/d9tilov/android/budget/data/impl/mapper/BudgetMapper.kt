package com.d9tilov.android.budget.data.impl.mapper

import com.d9tilov.android.budget.domain.model.BudgetData
import com.d9tilov.android.database.entity.BudgetDbModel

fun BudgetDbModel.toDataModel(): BudgetData =
    BudgetData(
        id,
        clientId,
        currency,
        sum,
        saveSum,
        createdDate,
    )

fun BudgetData.toDbModel(): BudgetDbModel =
    BudgetDbModel(
        id,
        clientId,
        currencyCode,
        sum,
        saveSum,
        createdDate,
    )
