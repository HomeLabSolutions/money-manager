package com.d9tilov.moneymanager.goal.data.local.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.data.local.entity.GoalDbModel
import javax.inject.Inject

class GoalDataMapper @Inject constructor() :
    Mapper<GoalDbModel, GoalData> {
    override fun toDataModel(model: GoalDbModel): GoalData =
        with(model) {
            GoalData(
                id,
                clientId,
                currency,
                name,
                targetSum,
                createdDate,
                description
            )
        }

    override fun toDbModel(model: GoalData): GoalDbModel =
        with(model) {
            GoalDbModel(
                id,
                clientId,
                currencyCode,
                name,
                targetSum,
                createdDate,
                description
            )
        }
}
