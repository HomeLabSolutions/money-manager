package com.d9tilov.moneymanager.regular.domain.mapper

import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import java.math.BigDecimal

fun RegularTransaction.toCommon(sumInUsd: BigDecimal): Transaction = Transaction.EMPTY.copy(
    type = type,
    sum = sum,
    usdSum = sumInUsd,
    category = category,
    currencyCode = currencyCode,
    description = description,
    isRegular = true
)
