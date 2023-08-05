package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.transaction.domain.model.BaseTransaction.Companion.ITEM
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class Transaction(
    val id: Long,
    val clientId: String,
    val type: TransactionType,
    val category: Category,
    val currencyCode: String,
    val sum: BigDecimal,
    val usdSum: BigDecimal,
    val date: LocalDateTime,
    val description: String,
    val qrCode: String?,
    val isRegular: Boolean,
    val inStatistics: Boolean,
    val latitude: Double,
    val longitude: Double,
    val photoUri: String?,
    override val headerPosition: Int
) : BaseTransaction {

    companion object {
        val EMPTY = Transaction(
            id = DataConstants.DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            type = TransactionType.EXPENSE,
            category = Category.EMPTY_EXPENSE,
            currencyCode = DEFAULT_CURRENCY_CODE,
            sum = BigDecimal.ZERO,
            usdSum = BigDecimal.ZERO,
            date = currentDateTime(),
            description = "",
            qrCode = null,
            isRegular = false,
            inStatistics = true,
            headerPosition = 0,
            latitude = 0.0,
            longitude = 0.0,
            photoUri = null
        )
    }

    override val itemType: Int
        get() = ITEM

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Transaction) return false

        if (id != other.id) return false
        if (clientId != other.clientId) return false
        if (type != other.type) return false
        if (category.id != other.category.id) return false
        if (currencyCode != other.currencyCode) return false
        if (sum != other.sum) return false
        if (usdSum != other.usdSum) return false
        if (date != other.date) return false
        if (description != other.description) return false
        if (qrCode != other.qrCode) return false
        if (isRegular != other.isRegular) return false
        if (inStatistics != other.inStatistics) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (photoUri != other.photoUri) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + clientId.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + category.id.hashCode()
        result = 31 * result + currencyCode.hashCode()
        result = 31 * result + sum.hashCode()
        result = 31 * result + usdSum.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (qrCode?.hashCode() ?: 0)
        result = 31 * result + isRegular.hashCode()
        result = 31 * result + inStatistics.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + (photoUri?.hashCode() ?: 0)
        return result
    }
}
