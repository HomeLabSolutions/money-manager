package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.Money
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.places.domain.model.Place
import kotlinx.datetime.LocalDateTime

data class Transaction(
    val id: Long,
    val clientId: String,
    val type: TransactionType,
    val category: Category,
    val sum: Money,
    val usdSum: Decimal,
    val date: LocalDateTime,
    val description: String,
    val qrCode: String?,
    val isRegular: Boolean,
    val inStatistics: Boolean,
    val place: Place,
    val photoUri: String?,
) {
    companion object {
        val EMPTY =
            Transaction(
                id = DataConstants.DEFAULT_DATA_ID,
                clientId = NO_ID.toString(),
                type = TransactionType.EXPENSE,
                category = Category.EMPTY_EXPENSE,
                sum = Money(0, Currency.USD),
                usdSum = Decimal.ZERO,
                date = currentDateTime(),
                description = "",
                qrCode = null,
                isRegular = false,
                inStatistics = true,
                place = Place.EMPTY,
                photoUri = null,
            )
    }
}
