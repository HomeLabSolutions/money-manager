package com.d9tilov.moneymanager.budget.data.local.mapper

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.entity.BudgetDbModel
import com.d9tilov.moneymanager.core.mapper.Mapper
import javax.inject.Inject

class BudgetMapper @Inject constructor() : Mapper<BudgetDbModel, BudgetData> {

    override fun toDataModel(model: BudgetDbModel): BudgetData =
        with(model) {
            BudgetData(
                id,
                clientId,
                currency,
                sum,
                saveSum,
                createdDate
            )
        }

    override fun toDbModel(model: BudgetData): BudgetDbModel =
        with(model) {
            BudgetDbModel(
                id,
                clientId,
                currencyCode,
                sum,
                saveSum,
                createdDate
            )
        }
}
