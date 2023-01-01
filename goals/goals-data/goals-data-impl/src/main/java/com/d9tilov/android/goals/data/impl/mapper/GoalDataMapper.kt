package com.d9tilov.android.goals.data.impl.mapper

import com.d9tilov.android.database.entity.GoalDbModel
import com.d9tilov.android.goals.data.model.GoalData

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
