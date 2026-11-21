package com.d9tilov.android.transaction.data.impl.mapper

import com.d9tilov.android.core.model.toType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.toMoney
import com.d9tilov.android.core.utils.withoutTrailingZeros
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.database.entity.TransactionMinMaxDateDbModel
import com.d9tilov.android.places.domain.model.LocationData
import com.d9tilov.android.places.domain.model.Place
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import java.math.BigDecimal

fun TransactionDbModel.toDataModel(): TransactionDataModel =
    TransactionDataModel(
        id,
        clientId,
        type.toType(),
        categoryId,
        currency,
        BigDecimal.valueOf(checkNotNull(sum), checkNotNull(sumPrecision)).withoutTrailingZeros(),
        usdSum,
        date,
        description,
        qrCode,
        isRegular,
        inStatistics,
        Place(location = LocationData(latitude, longitude), name = placeName, address = placeAddress),
        photoUri,
    )

fun TransactionDataModel.toDbModel(): TransactionDbModel {
    val amount = sum.toMoney(currencyCode)
    return TransactionDbModel(
        id,
        clientId,
        type.value,
        categoryId,
        currencyCode,
        amount.value,
        amount.currency.precision,
        usdSum,
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
