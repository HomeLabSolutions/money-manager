package com.d9tilov.android.transaction.ui.model

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.model.BaseTransaction.Companion.ITEM
import kotlinx.datetime.LocalDateTime

data class TransactionUiModel(
    val id: Long,
    val clientId: String,
    val type: TransactionType,
    val category: Category,
    val currencyCode: String,
    val sum: Decimal,
    val usdSum: Decimal?,
    override val date: LocalDateTime,
    val description: String,
    val showTime: Boolean,
    val isRegular: Boolean,
    val inStatistics: Boolean,
    val hasPlace: Boolean,
) : BaseTransaction {
    override val itemType: Int = ITEM

    companion object {
        val EMPTY =
            TransactionUiModel(
                id = DataConstants.DEFAULT_DATA_ID,
                clientId = NO_ID.toString(),
                type = TransactionType.EXPENSE,
                category =
                    Category.EMPTY_EXPENSE.copy(
                        color = android.R.color.holo_blue_light,
                        name = "Category1",
                        icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe,
                    ),
                currencyCode = DEFAULT_CURRENCY_CODE,
                sum = Decimal.ZERO,
                usdSum = Decimal.ZERO,
                date = currentDateTime(),
                description = "",
                showTime = true,
                isRegular = false,
                inStatistics = true,
                hasPlace = false,
            )
    }
}

fun Transaction.toUiModel(isStatisticsScreen: Boolean = false) =
    TransactionUiModel(
        id = id,
        clientId = clientId,
        type = type,
        category = category,
        currencyCode = sum.currency.name,
        sum = sum.toDecimal(),
        usdSum = if (sum.currency.name == DEFAULT_CURRENCY_CODE) null else usdSum,
        date = date,
        description = if (!isStatisticsScreen) description else "",
        showTime = isStatisticsScreen,
        isRegular = isRegular,
        inStatistics = inStatistics,
        hasPlace = !place.name.isNullOrBlank(),
    )
