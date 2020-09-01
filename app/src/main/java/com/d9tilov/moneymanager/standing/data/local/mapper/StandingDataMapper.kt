package com.d9tilov.moneymanager.standing.data.local.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.standing.data.entity.StandingData
import com.d9tilov.moneymanager.standing.data.local.entity.StandingDbModel
import javax.inject.Inject

class StandingDataMapper @Inject constructor() : Mapper<StandingDbModel, StandingData> {

    override fun toDataModel(model: StandingDbModel): StandingData =
        with(model) {
            StandingData(
                id,
                clientId,
                currency,
                type,
                sum,
                categoryId,
                createdDate,
                expireDate,
                description,
                pushEnable,
                autoAdded
            )
        }

    override fun toDbModel(model: StandingData): StandingDbModel =
        with(model) {
            StandingDbModel(
                id,
                clientId,
                currencyCode,
                type,
                sum,
                categoryId,
                createdDate,
                expireDate,
                description,
                pushEnable,
                autoAdded
            )
        }
}
