package com.d9tilov.android.places.data.impl

import com.d9tilov.android.network.model.GeoapifyPlaceFeature
import com.d9tilov.android.network.model.GeoapifyPlaceProperties
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.data.impl.mapper.toNearbyPlaces
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeoapifyPlaceMapperTest {
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
        val places = response.toNearbyPlaces()
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
                    GeoapifyPlaceFeature(GeoapifyPlaceProperties()),
                    feature("", "Cafe", 0.0),
                    feature("blank", " ", 1.0),
                    feature("outside", "Shop", 150.001),
                    feature("negative", "Shop", -1.0),
                    feature("nan", "Shop", Double.NaN),
                    feature("infinite", "Shop", Double.POSITIVE_INFINITY),
                ),
            )
        assertTrue(response.toNearbyPlaces().isEmpty())
    }

    @Test
    fun `places without api distance are omitted even when coordinates are present`() {
        val response =
            GeoapifyPlacesResponse(
                listOf(
                    point("same", 51.5, -0.1),
                    point("near", 51.5001, -0.1),
                    point("outside", 51.51, -0.1),
                    point("invalid", 91.0, -0.1),
                ),
            )
        assertTrue(response.toNearbyPlaces().isEmpty())
    }

    @Test
    fun `results are capped at twenty and empty responses are valid`() {
        val response = GeoapifyPlacesResponse((0..24).map { feature(it.toString(), "Shop", it.toDouble()) })
        assertEquals((0..19).map(Int::toString), response.toNearbyPlaces().map { it.id })
        assertTrue(GeoapifyPlacesResponse(emptyList()).toNearbyPlaces().isEmpty())
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
