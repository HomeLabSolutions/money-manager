package com.d9tilov.android.incomeexpense

import android.location.Location
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.model.TransactionType
import com.google.gson.annotations.SerializedName
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class Transaction2(
    @SerializedName("id") val id: Long,
    @SerializedName("clientId") val clientId: String,
    @SerializedName("type") val type: TransactionType,
    @SerializedName("category") val category: Category,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("sum") val sum: BigDecimal,
    @SerializedName("usdSum") val usdSum: BigDecimal,
    @SerializedName("date") val date: LocalDateTime,
    @SerializedName("description") val description: String,
    @SerializedName("qrCode") val qrCode: String?,
    @SerializedName("isRegular") val isRegular: Boolean,
    @SerializedName("inStatistics") val inStatistics: Boolean,
    @SerializedName("location") val location: Location?,
    @SerializedName("photoUri") val photoUri: String?,
    @SerializedName("headerPosition") val headerPosition: Int,
)
