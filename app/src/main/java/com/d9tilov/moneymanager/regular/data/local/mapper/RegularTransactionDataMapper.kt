package com.d9tilov.moneymanager.regular.data.local.mapper

import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.regular.data.local.entity.RegularTransactionDbModel

fun RegularTransactionDbModel.toDataModel(): RegularTransactionData =
    RegularTransactionData(
        id,
        clientId,
        type,
        sum,
        usdSum,
        categoryId,
        currency,
        createdDate,
        startDate,
        periodType,
        dayOfWeek,
        description,
        pushEnable,
        autoAdd
    )

fun RegularTransactionData.toDbModel(): RegularTransactionDbModel =
    RegularTransactionDbModel(
        id,
        clientId,
        type,
        sum,
        usdSum,
        categoryId,
        currencyCode,
        createdDate,
        startDate,
        periodType,
        dayOfWeek,
        description,
        pushEnable,
        autoAdd
    )
