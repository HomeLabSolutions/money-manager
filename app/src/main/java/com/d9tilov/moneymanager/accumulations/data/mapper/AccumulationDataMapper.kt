package com.d9tilov.moneymanager.accumulations.data.mapper

import com.d9tilov.moneymanager.accumulations.data.entity.Accumulation
import com.d9tilov.moneymanager.accumulations.data.local.entitiy.AccumulationDbModel
import com.d9tilov.moneymanager.core.mapper.Mapper
import javax.inject.Inject

class AccumulationDataMapper @Inject constructor() : Mapper<AccumulationDbModel, Accumulation> {

    override fun toDataModel(model: AccumulationDbModel): Accumulation =
        with(model) {
            Accumulation(
                id,
                clientId,
                currency,
                sum,
                date
            )
        }

    override fun toDbModel(model: Accumulation): AccumulationDbModel =
        with(model) {
            AccumulationDbModel(
                id,
                clientId,
                sum,
                currencyCode,
                date,
            )
        }
}
