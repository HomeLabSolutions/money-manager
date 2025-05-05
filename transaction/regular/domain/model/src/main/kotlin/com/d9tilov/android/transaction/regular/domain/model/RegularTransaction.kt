package com.d9tilov.android.transaction.regular.domain.model

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class RegularTransaction(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val type: TransactionType,
    val sum: BigDecimal,
    val category: Category,
    val createdDate: LocalDateTime,
    val executionPeriod: ExecutionPeriod,
    val description: String,
    val pushEnabled: Boolean,
    val autoAdd: Boolean,
) {
    companion object {
        val EMPTY =
            RegularTransaction(
                id = DEFAULT_DATA_ID,
                clientId = DataConstants.NO_ID.toString(),
                currencyCode = DEFAULT_CURRENCY_CODE,
                type = TransactionType.EXPENSE,
                sum = BigDecimal.ZERO,
                category = Category.EMPTY_EXPENSE,
                createdDate = currentDateTime(),
                executionPeriod =
                    ExecutionPeriod.EveryMonth(
                        currentDate().dayOfMonth,
                        currentDateTime(),
                    ),
                description = "",
                pushEnabled = true,
                autoAdd = true,
            )
    }

    fun isValid(): Boolean = id != DEFAULT_DATA_ID
}
