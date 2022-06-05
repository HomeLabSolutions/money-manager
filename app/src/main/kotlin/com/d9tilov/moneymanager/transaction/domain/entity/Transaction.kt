package com.d9tilov.moneymanager.transaction.domain.entity

import android.location.Location
import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.NO_ID
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.ITEM
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Parcelize
data class Transaction(
    val id: Long,
    val clientId: String,
    val type: TransactionType,
    val category: Category,
    val currencyCode: String,
    val sum: BigDecimal,
    val usdSum: BigDecimal,
    val date: @RawValue LocalDateTime,
    val description: String,
    val qrCode: String?,
    val isRegular: Boolean,
    val inStatistics: Boolean,
    val location: Location?,
    val photoUri: String?,
    override val headerPosition: Int
) : BaseTransaction, Parcelable {

    companion object {
        val EMPTY = Transaction(
            id = DataConstants.DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            type = TransactionType.EXPENSE,
            category = Category.EMPTY_EXPENSE,
            currencyCode = DataConstants.DEFAULT_CURRENCY_CODE,
            sum = BigDecimal.ZERO,
            usdSum = BigDecimal.ZERO,
            date = currentDateTime(),
            description = "",
            qrCode = null,
            isRegular = false,
            inStatistics = true,
            headerPosition = 0,
            location = null,
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
        if (location != other.location) return false
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
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (photoUri?.hashCode() ?: 0)
        return result
    }
}
