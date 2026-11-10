package com.d9tilov.android.currency.data.impl.api

import com.d9tilov.android.network.createNetworkClient
import com.d9tilov.android.network.di.NetworkModule
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class KtorCurrencyApiTest {
    private val json = NetworkModule.provideJson()

    @Test
    fun `currency request encodes path segments and decodes rates with unknown fields`() =
        runTest {
            MockEngine { request ->
                assertEquals("https://v6.exchangerate-api.com/v6/test%2Fkey/latest/GBP", request.url.toString())
                respond(
                    """
                    {"result":"success","base_code":"GBP","time_last_update_unix":42,
                    "conversion_rates":{"GBP":1.0,"USD":1.25},"extra":"ignored"}
                    """.trimIndent(),
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }.use { engine ->
                createNetworkClient(engine, "https://v6.exchangerate-api.com/", json = json).use { client ->
                    val response = KtorCurrencyApi(client).getCurrencies("GBP", "test/key")
                    assertEquals("GBP", response.base)
                    assertEquals(42L, response.date)
                    assertEquals(1.25, response.rates["USD"])
                }
            }
        }

    @Test
    fun `geocoding preserves coordinates plus signs and query credentials`() =
        runTest {
            MockEngine { request ->
                assertEquals("/geocode/v1/json", request.url.encodedPath)
                assertEquals("51.5+-0.1", request.url.parameters["q"])
                assertEquals("test+key&value", request.url.parameters["key"])
                respond(
                    """{"results":[{"annotations":{"currency":{"iso_code":"GBP"}}}],"status":{"code":200}}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }.use { engine ->
                createNetworkClient(engine, "https://api.opencagedata.com/", json = json).use { client ->
                    val response = KtorGeocodeApi(client).getCurrencies("51.5+-0.1", "test+key&value")
                    assertEquals(
                        "GBP",
                        response.results
                            .single()
                            .annotations.currency.isoCode,
                    )
                }
            }
        }
}
