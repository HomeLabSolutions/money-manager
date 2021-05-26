package com.d9tilov.moneymanager.goal.data.local.mapper

import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.data.local.entity.GoalDbModel

fun GoalDbModel.toDataModel(): GoalData =

    GoalData(
        id,
        clientId,
        currency,
        name,
        targetSum,
        createdDate,
        description
    )

fun GoalData.toDbModel(): GoalDbModel =
    GoalDbModel(
        id,
        clientId,
        currencyCode,
        name,
        targetSum,
        createdDate,
        description
    )
