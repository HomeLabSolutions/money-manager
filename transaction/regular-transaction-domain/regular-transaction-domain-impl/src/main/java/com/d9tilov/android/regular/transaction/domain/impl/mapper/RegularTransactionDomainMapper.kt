package com.d9tilov.android.regular.transaction.domain.impl

import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.regular.transaction.data.model.RegularTransactionData
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction

fun com.d9tilov.android.regular.transaction.domain.model.RegularTransaction.toData(): com.d9tilov.android.regular.transaction.data.model.RegularTransactionData =
    com.d9tilov.android.regular.transaction.data.model.RegularTransactionData(
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
        autoAdd
    )

fun com.d9tilov.android.regular.transaction.data.model.RegularTransactionData.toDomain(category: Category): com.d9tilov.android.regular.transaction.domain.model.RegularTransaction =
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
        autoAdd
    )
