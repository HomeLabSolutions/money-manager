package com.d9tilov.android.core.utils

import com.d9tilov.android.core.constants.CurrencyConstants.LONG_DECIMAL_LENGTH
import com.d9tilov.android.core.model.Decimal

fun Decimal?.divideBy(divider: Decimal): Decimal = this?.divide(divider, LONG_DECIMAL_LENGTH) ?: Decimal.ZERO
