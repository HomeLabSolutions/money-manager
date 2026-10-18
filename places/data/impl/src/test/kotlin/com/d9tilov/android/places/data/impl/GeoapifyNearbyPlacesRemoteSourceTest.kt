package com.d9tilov.android.places.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.network.GeoapifyApi
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class GeoapifyNearbyPlacesRemoteSourceTest {
    private val location = LocationData(51.5, -0.1)
    private val api = RecordingGeoapifyApi()
    private val source = GeoapifyNearbyPlacesRemoteSource(api)

    @Test
    fun `request fixes categories radius limit and longitude first ordering`() =
        runTest {
            assertTrue(source.getNearbyPlaces(location, "ru").features.isEmpty())
            assertEquals(
                mapOf(
                    "categories" to "catering,commercial",
                    "conditions" to "named",
                    "filter" to "circle:-0.1,51.5,150",
                    "bias" to "proximity:-0.1,51.5",
                    "limit" to "20",
                    "lang" to "ru",
                ),
                api.requests.single(),
            )
        }

    @Test
    fun `service failures and cancellation propagate for view model retry and cancellation handling`() =
        runTest {
            listOf(IOException("Unavailable"), CancellationException("Cancelled")).forEach { error ->
                api.error = error
                val failure = runCatching { source.getNearbyPlaces(location, "en") }.exceptionOrNull()
                assertSame(error, failure)
            }
        }

    private class RecordingGeoapifyApi : GeoapifyApi {
        val requests = mutableListOf<Map<String, String>>()
        var error: Exception? = null

        override suspend fun getPlaces(
            filter: String,
            bias: String,
            language: String,
            limit: Int,
            categories: String,
            conditions: String,
            key: String,
        ): GeoapifyPlacesResponse {
            requests.add(
                mapOf(
                    "categories" to categories,
                    "conditions" to conditions,
                    "filter" to filter,
                    "bias" to bias,
                    "limit" to limit.toString(),
                    "lang" to language,
                ),
            )
            error?.let { throw it }
            return GeoapifyPlacesResponse(emptyList())
        }
    }
}
