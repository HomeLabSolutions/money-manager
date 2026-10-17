package com.d9tilov.android.network

import com.d9tilov.android.network.model.GeoapifyPlaceProperties
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
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
        val properties = response.features.single().properties
        assertEquals("cafe", properties.id)
        assertEquals("Cafe", properties.name)
        assertEquals("High Street", properties.address)
        assertEquals(51.5, checkNotNull(properties.latitude), 0.0)
        assertEquals(-0.1, checkNotNull(properties.longitude), 0.0)
        assertEquals(10.5, checkNotNull(properties.distance), 0.0)
    }

    @Test
    fun `missing or null optional place fields deserialize to null`() {
        listOf(
            "{}",
            """
            {
              "place_id": null, "name": null, "formatted": null,
              "distance": null, "lat": null, "lon": null
            }
            """.trimIndent(),
        ).forEach { properties ->
            val response =
                json.decodeFromString<GeoapifyPlacesResponse>("""{"features":[{"properties":$properties}]}""")
            assertEquals(GeoapifyPlaceProperties(), response.features.single().properties)
        }
    }

    @Test
    fun `malformed service responses fail instead of appearing as an empty successful list`() {
        listOf(
            """{"error": "Invalid key"}""",
            """{"features": null}""",
            """{"features": [null]}""",
            """{"features": [{}]}""",
            """{"features": [{"properties": null}]}""",
        ).forEach { response ->
            assertThrows(SerializationException::class.java) {
                json.decodeFromString<GeoapifyPlacesResponse>(response)
            }
        }
    }
}
