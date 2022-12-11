package com.d9tilov.moneymanager.regular.data.local.mapper

import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.android.database.entity.RegularTransactionDbModel

fun RegularTransactionDbModel.toDataModel(): RegularTransactionData =
    RegularTransactionData(
        id,
        clientId,
        type,
        sum,
        categoryId,
        currency,
        createdDate,
        executionPeriod,
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
        categoryId,
        currencyCode,
        createdDate,
        executionPeriod,
        description,
        pushEnable,
        autoAdd
    )
