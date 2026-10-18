package com.d9tilov.android.places.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.network.model.GeoapifyPlaceFeature
import com.d9tilov.android.network.model.GeoapifyPlaceProperties
import com.d9tilov.android.network.model.GeoapifyPlacesResponse
import com.d9tilov.android.places.data.contract.NearbyPlacesRemoteSource
import com.d9tilov.android.places.domain.model.NearbyPlace
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class NearbyPlacesDataRepoTest {
    private val location = LocationData(51.5, -0.1)
    private val source = RecordingNearbyPlacesRemoteSource()
    private val repo = NearbyPlacesDataRepo(source)

    @Test
    fun `repository passes coordinates to source and maps the response`() =
        runTest {
            source.response =
                GeoapifyPlacesResponse(
                    listOf(
                        GeoapifyPlaceFeature(
                            GeoapifyPlaceProperties(
                                id = "cafe",
                                name = "Cafe",
                                address = "High Street",
                                distance = 10.5,
                            ),
                        ),
                    ),
                )
            assertEquals(listOf(NearbyPlace("cafe", "Cafe", "High Street", 10)), repo.getNearbyPlaces(location))
            assertEquals(listOf(location), source.requests)
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
            assertTrue(source.requests.isEmpty())
        }

    @Test
    fun `service failures and cancellation propagate for view model retry and cancellation handling`() =
        runTest {
            listOf(IOException("Unavailable"), CancellationException("Cancelled")).forEach { error ->
                source.error = error
                val failure = runCatching { repo.getNearbyPlaces(location) }.exceptionOrNull()
                assertSame(error, failure)
            }
        }

    private class RecordingNearbyPlacesRemoteSource : NearbyPlacesRemoteSource {
        val requests = mutableListOf<LocationData>()
        var response = GeoapifyPlacesResponse(emptyList())
        var error: Exception? = null

        override suspend fun getNearbyPlaces(location: LocationData): GeoapifyPlacesResponse {
            requests.add(location)
            error?.let { throw it }
            return response
        }
    }
}
