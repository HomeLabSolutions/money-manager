package com.d9tilov.android.network

import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class GeoapifyPlacesResponseTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `geojson fields deserialize to the place name address coordinates and distance`() {
        val response =
            json.decodeFromString<GeoapifyPlacesResponse>(
                """
                {
                  "type": "FeatureCollection",
                  "features": [{
                    "type": "Feature",
                    "properties": {
                      "place_id": "cafe", "name": "Cafe", "formatted": "High Street",
                      "lat": 51.5, "lon": -0.1, "distance": 10.5, "categories": ["catering.cafe"]
                    }
                  }]
                }
                """.trimIndent(),
            )
        val properties = checkNotNull(response.features.single()?.properties)
        assertEquals("cafe", properties.id)
        assertEquals("Cafe", properties.name)
        assertEquals("High Street", properties.address)
        assertEquals(51.5, checkNotNull(properties.latitude), 0.0)
        assertEquals(-0.1, checkNotNull(properties.longitude), 0.0)
        assertEquals(10.5, checkNotNull(properties.distance), 0.0)
    }

    @Test
    fun `missing optional properties can be filtered by the mapper`() {
        val response = json.decodeFromString<GeoapifyPlacesResponse>("""{"features": [null, {}, {"properties": {}}]}""")
        assertNull(response.features.first())
        assertNull(response.features[1]?.properties)
        assertNull(response.features[2]?.properties?.id)
    }

    @Test
    fun `malformed service responses fail instead of appearing as an empty successful list`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<GeoapifyPlacesResponse>("""{"error": "Invalid key"}""")
        }
    }
}
