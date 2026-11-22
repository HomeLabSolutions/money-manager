package com.d9tilov.android.transaction.data.impl.mapper

import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.Money
import com.d9tilov.android.core.model.toType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.database.entity.TransactionMinMaxDateDbModel
import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.Place
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel

fun TransactionDbModel.toDataModel(): TransactionDataModel =
    TransactionDataModel(
        id,
        clientId,
        type.toType(),
        categoryId,
        currency,
        Money(sum, checkNotNull(Currency.fromCode(currency))).toDecimal(),
        Decimal(usdSum).movePoint(-Currency.USD.precision).withoutTrailingFractionZeros(),
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        Place(location = LocationData(latitude, longitude), name = placeName, address = placeAddress),
        photoUri,
    )

fun TransactionDataModel.toDbModel(): TransactionDbModel {
    val currency = checkNotNull(Currency.fromCode(currencyCode)) { "Unsupported currency: $currencyCode" }
    val amount = sum.toMoney(currency)
    val usdPrecision = Currency.USD.precision
    val usdCents = usdSum.roundTo(usdPrecision).toLongExact(usdPrecision)
    return TransactionDbModel(
        id,
        clientId,
        type.value,
        categoryId,
        currencyCode,
        amount.value,
        usdCents,
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        place.location.latitude,
        place.location.longitude,
        photoUri,
        place.name,
        place.address,
    )
}

fun TransactionMinMaxDateDbModel.toDataModel(): TransactionMinMaxDateModel =
    TransactionMinMaxDateModel(minDate ?: currentDateTime(), maxDate ?: currentDateTime())
