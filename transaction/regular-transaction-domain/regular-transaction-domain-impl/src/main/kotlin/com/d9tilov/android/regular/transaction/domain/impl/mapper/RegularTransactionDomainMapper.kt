package com.d9tilov.android.regular.transaction.domain.impl.mapper

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.domain.model.RegularTransactionData

fun RegularTransaction.toData(): RegularTransactionData =
    RegularTransactionData(
        id,
        clientId,
        type,
        sum,
        category.id,
        currencyCode,
        createdDate,
        executionPeriod,
        description,
        pushEnabled,
        autoAdd,
    )

fun RegularTransactionData.toDomain(category: Category): RegularTransaction =
    RegularTransaction(
        id,
        clientId,
        currencyCode,
        type,
        sum,
        category,
        createdDate,
        executionPeriod,
        description,
        pushEnable,
        autoAdd,
    )
