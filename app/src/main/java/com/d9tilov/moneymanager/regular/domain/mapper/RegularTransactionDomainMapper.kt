package com.d9tilov.moneymanager.regular.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import javax.inject.Inject

class RegularTransactionDomainMapper @Inject constructor() {

    fun toData(regularTransaction: RegularTransaction) =
        with(regularTransaction) {
            RegularTransactionData(
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

    fun toDomain(regularTransactionData: RegularTransactionData, category: Category) =
        with(regularTransactionData) {
            RegularTransaction(
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
