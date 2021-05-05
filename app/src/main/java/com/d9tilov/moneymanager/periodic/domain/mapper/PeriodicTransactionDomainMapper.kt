package com.d9tilov.moneymanager.periodic.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.periodic.data.entity.PeriodicTransactionData
import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction
import javax.inject.Inject

class PeriodicTransactionDomainMapper @Inject constructor() {

    fun toData(periodicTransaction: PeriodicTransaction) =
        with(periodicTransaction) {
            PeriodicTransactionData(
                id,
                clientId,
                currencyCode,
                type,
                sum,
                category.id,
                createdDate,
                startDate,
                periodType,
                dayOfWeek,
                description,
                pushEnable
            )
        }

    fun toDomain(periodicTransactionData: PeriodicTransactionData, category: Category) =
        with(periodicTransactionData) {
            PeriodicTransaction(
                id,
                clientId,
                currencyCode,
                type,
                sum,
                category,
                createdDate,
                startDate,
                periodType,
                dayOfWeek,
                description,
                pushEnable
            )
        }
}
