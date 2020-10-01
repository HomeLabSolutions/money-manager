package com.d9tilov.moneymanager.fixed.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import javax.inject.Inject

class FixedTransactionDomainMapper @Inject constructor() {

    fun toData(fixedTransaction: FixedTransaction) =
        with(fixedTransaction) {
            FixedTransactionData(
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

    fun toDomain(fixedTransactionData: FixedTransactionData, category: Category) =
        with(fixedTransactionData) {
            FixedTransaction(
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
