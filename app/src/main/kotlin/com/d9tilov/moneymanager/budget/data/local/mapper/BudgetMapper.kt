package com.d9tilov.moneymanager.budget.data.local.mapper

import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.entity.BudgetDbModel

fun BudgetDbModel.toDataModel(): BudgetData = BudgetData(
    id,
    clientId,
    currency,
    sum,
    saveSum,
    createdDate
)

fun BudgetData.toDbModel(): BudgetDbModel =
    BudgetDbModel(
        id,
        clientId,
        currencyCode,
        sum,
        saveSum,
        createdDate
    )
