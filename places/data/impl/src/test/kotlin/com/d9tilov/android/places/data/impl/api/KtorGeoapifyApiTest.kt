package com.d9tilov.android.places.data.impl.api

import com.d9tilov.android.network.createNetworkClient
import com.d9tilov.android.network.di.provideJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KtorGeoapifyApiTest {
    private val json = provideJson()

    @Test
    fun `places request preserves all parameters and decodes optional properties`() =
        runTest {
            MockEngine { request ->
                assertEquals("/v2/places", request.url.encodedPath)
                assertEquals("circle:-0.1,51.5,200", request.url.parameters["filter"])
                assertEquals("proximity:-0.1,51.5", request.url.parameters["bias"])
                assertEquals("ru", request.url.parameters["lang"])
                assertEquals("20", request.url.parameters["limit"])
                assertEquals("catering,commercial", request.url.parameters["categories"])
                assertEquals("named", request.url.parameters["conditions"])
                assertEquals("key+with&characters", request.url.parameters["apiKey"])
                respond(
                    """{"features":[{"properties":{"place_id":"place","name":"Cafe","extra":true}}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }.use { engine ->
                createNetworkClient(engine, "https://api.geoapify.com/", json = json).use { client ->
                    val response = requestPlaces(KtorGeoapifyApi(client))
                    assertEquals(
                        "Cafe",
                        response.features
                            .single()
                            .properties.name,
                    )
                }
            }
        }

    @Test
    fun `unsuccessful responses fail before json decoding`() =
        runTest {
            MockEngine { respond("denied", HttpStatusCode.Forbidden) }.use { engine ->
                createNetworkClient(engine, "https://api.geoapify.com/", json = json).use { client ->
                    val failure = runCatching { requestPlaces(KtorGeoapifyApi(client)) }.exceptionOrNull()
                    assertTrue(failure is ClientRequestException)
                    assertEquals(HttpStatusCode.Forbidden, (failure as ClientRequestException).response.status)
                }
            }
        }

    @Test
    fun `transport cancellation propagates to the caller`() =
        runTest {
            val cancellation = CancellationException("Cancelled")
            MockEngine { throw cancellation }.use { engine ->
                createNetworkClient(engine, "https://api.geoapify.com/", json = json).use { client ->
                    val failure = runCatching { requestPlaces(KtorGeoapifyApi(client)) }.exceptionOrNull()
                    assertTrue(failure is CancellationException)
                    assertEquals(cancellation.message, failure?.message)
                }
            }
        }

    private suspend fun requestPlaces(api: GeoapifyApi) =
        api.getPlaces(
            filter = "circle:-0.1,51.5,200",
            bias = "proximity:-0.1,51.5",
            language = "ru",
            limit = 20,
            categories = "catering,commercial",
            conditions = "named",
            key = "key+with&characters",
        )
}
