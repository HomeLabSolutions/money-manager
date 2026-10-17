package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.network.GeoapifyApi
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.util.Locale

class NearbyPlacesDataRepoTest {
    private val locale = Locale.getDefault()
    private val location = LocationData(51.5, -0.1)
    private val api = RecordingGeoapifyApi()
    private val repo = NearbyPlacesDataRepo(api)

    @After
    fun tearDown() {
        Locale.setDefault(locale)
    }

    @Test
    fun `request fixes categories radius limit and longitude first ordering`() =
        runTest {
            Locale.setDefault(Locale.forLanguageTag("ru-RU"))
            assertTrue(repo.getNearbyPlaces(location).isEmpty())
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
    fun `locales without a supported language use english`() =
        runTest {
            Locale.setDefault(Locale.ROOT)
            repo.getNearbyPlaces(location)
            assertEquals("en", api.requests.single()["lang"])
        }

    @Test
    fun `invalid coordinates fail before sending a request`() =
        runTest {
            listOf(
                LocationData(Double.NaN, 0.0),
                LocationData(0.0, Double.POSITIVE_INFINITY),
                LocationData(91.0, 0.0),
                LocationData(0.0, -181.0),
            ).forEach { invalid ->
                val failure = runCatching { repo.getNearbyPlaces(invalid) }.exceptionOrNull()
                assertTrue(failure is IllegalArgumentException)
            }
            assertTrue(api.requests.isEmpty())
        }

    @Test
    fun `service failures and cancellation propagate for view model retry and cancellation handling`() =
        runTest {
            listOf(IOException("Unavailable"), CancellationException("Cancelled")).forEach { error ->
                api.error = error
                val failure = runCatching { repo.getNearbyPlaces(location) }.exceptionOrNull()
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
