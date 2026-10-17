package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.network.model.GeoapifyPlaceFeature
import com.d9tilov.android.network.model.GeoapifyPlaceProperties
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import com.d9tilov.android.transaction.data.impl.mapper.toNearbyPlaces
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeoapifyPlaceMapperTest {
    private val location = LocationData(51.5, -0.1)

    @Test
    fun `results retain the nearest duplicate and sort using distance before rounding`() {
        val response =
            GeoapifyPlacesResponse(
                listOf(
                    feature("shop", "Shop", 150.0),
                    feature("cafe", " Cafe ", 1.9),
                    feature("closer", "Closer", 1.1),
                    feature("cafe", "Cafe duplicate", 20.0),
                ),
            )
        val places = response.toNearbyPlaces(location)
        assertEquals(listOf("closer", "cafe", "shop"), places.map { it.id })
        assertEquals(listOf(1, 1, 150), places.map { it.distanceMeters })
        assertEquals("Cafe", places[1].name)
        assertEquals("High Street", places[1].address)
    }

    @Test
    fun `invalid unnamed and out of radius features are omitted`() {
        val response =
            GeoapifyPlacesResponse(
                listOf(
                    null,
                    GeoapifyPlaceFeature(),
                    feature("", "Cafe", 0.0),
                    feature("blank", " ", 1.0),
                    feature("outside", "Shop", 150.001),
                    feature("negative", "Shop", -1.0),
                    feature("nan", "Shop", Double.NaN),
                    feature("infinite", "Shop", Double.POSITIVE_INFINITY),
                ),
            )
        assertTrue(response.toNearbyPlaces(location).isEmpty())
    }

    @Test
    fun `missing distance uses coordinates and excludes distant or invalid positions`() {
        val response =
            GeoapifyPlacesResponse(
                listOf(
                    point("same", 51.5, -0.1),
                    point("near", 51.5001, -0.1),
                    point("outside", 51.51, -0.1),
                    point("invalid", 91.0, -0.1),
                ),
            )
        val places = response.toNearbyPlaces(location)
        assertEquals(listOf("same", "near"), places.map { it.id })
        assertEquals(0, places.first().distanceMeters)
        assertTrue(places.last().distanceMeters in 10..12)
    }

    @Test
    fun `results are capped at twenty and empty responses are valid`() {
        val response = GeoapifyPlacesResponse((0..24).map { feature(it.toString(), "Shop", it.toDouble()) })
        assertEquals((0..19).map(Int::toString), response.toNearbyPlaces(location).map { it.id })
        assertTrue(GeoapifyPlacesResponse(emptyList()).toNearbyPlaces(location).isEmpty())
    }

    private fun feature(
        id: String,
        name: String,
        distance: Double,
    ) = GeoapifyPlaceFeature(
        GeoapifyPlaceProperties(id = id, name = name, address = "High Street", distance = distance),
    )

    private fun point(
        id: String,
        latitude: Double,
        longitude: Double,
    ) = GeoapifyPlaceFeature(
        GeoapifyPlaceProperties(id = id, name = "Cafe", latitude = latitude, longitude = longitude),
    )
}
