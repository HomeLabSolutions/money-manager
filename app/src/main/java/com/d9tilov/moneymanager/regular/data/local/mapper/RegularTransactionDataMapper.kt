package com.d9tilov.moneymanager.regular.data.local.mapper

import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.data.local.entity.RegularTransactionDbModel

fun RegularTransactionDbModel.toDataModel(): RegularTransactionData =
    RegularTransactionData(
        id,
        clientId,
        currency,
        type,
        sum,
        categoryId,
        createdDate,
        startDate,
        periodType,
        dayOfWeek,
        description,
        pushEnable
    )

fun RegularTransactionData.toDbModel(): RegularTransactionDbModel =
    RegularTransactionDbModel(
        id,
        clientId,
        currencyCode,
        type,
        sum,
        categoryId,
        createdDate,
        startDate,
        periodType,
        dayOfWeek,
        description,
        pushEnable
    )
