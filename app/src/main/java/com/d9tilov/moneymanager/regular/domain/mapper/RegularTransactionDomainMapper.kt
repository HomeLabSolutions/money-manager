package com.d9tilov.moneymanager.regular.domain.mapper

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction

fun RegularTransaction.toData(): RegularTransactionData = RegularTransactionData(
    id,
    clientId,
    type,
    sum,
    usdSum,
    category.id,
    currencyCode,
    createdDate,
    startDate,
    periodType,
    dayOfWeek,
    description,
    pushEnable,
    autoAdd
)

fun RegularTransactionData.toDomain(category: Category): RegularTransaction =
    RegularTransaction(
        id,
        clientId,
        currencyCode,
        type,
        sum,
        usdSum,
        category,
        createdDate,
        startDate,
        periodType,
        dayOfWeek,
        description,
        pushEnable,
        autoAdd
    )
