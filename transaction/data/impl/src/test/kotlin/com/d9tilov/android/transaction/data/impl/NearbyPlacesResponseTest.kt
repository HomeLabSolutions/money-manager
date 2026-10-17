package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.data.impl.mapper.toDataModel
import com.d9tilov.android.transaction.data.impl.mapper.toDbModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class NearbyPlacesResponseTest {
    @Test
    fun `callable results are ordered by distance and deduplicated`() {
        val response =
            mapOf(
                "places" to
                    listOf(
                        mapOf("id" to "shop", "name" to "Shop", "distanceMeters" to 150),
                        mapOf("id" to "cafe", "name" to "Cafe", "distanceMeters" to 10.5),
                        mapOf("id" to "cafe", "name" to "Cafe", "distanceMeters" to 10.5),
                    ),
            )
        assertEquals(listOf("Cafe", "Shop"), parseNearbyPlaces(response).map { it.name })
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid distances are rejected rather than presented as nearest`() {
        parseNearbyPlaces(mapOf("places" to listOf(mapOf("id" to "shop", "name" to "Shop", "distanceMeters" to 151))))
    }

    @Test
    fun `custom names skipped names and undecided places survive database mapping`() {
        val transaction =
            TransactionDataModel(
                type = TransactionType.EXPENSE,
                categoryId = 1,
                currencyCode = "USD",
                sum = BigDecimal.ONE,
                usdSum = BigDecimal.ONE,
                isRegular = false,
                inStatistics = true,
                location = LocationData.EMPTY,
            )
        listOf(null, "", "My cafe").forEach { name ->
            val original = transaction.copy(placeName = name, placeAddress = "High Street")
            assertEquals(original, original.toDbModel().toDataModel())
        }
    }
}
