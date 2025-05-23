package com.d9tilov.android.transaction.regular.data.impl.mapper

import com.d9tilov.android.database.entity.RegularTransactionDbModel
import com.d9tilov.android.transaction.regular.domain.model.RegularTransactionData

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
        autoAdd,
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
        autoAdd,
    )
