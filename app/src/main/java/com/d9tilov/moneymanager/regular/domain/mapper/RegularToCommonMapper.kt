package com.d9tilov.moneymanager.regular.domain.mapper

import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

fun RegularTransaction.toCommon(): Transaction = Transaction(
    type = type,
    sum = sum,
    category = category,
    currencyCode = currencyCode,
    description = description,
    isRegular = true
)
