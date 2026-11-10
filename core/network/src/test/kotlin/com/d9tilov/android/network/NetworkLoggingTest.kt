package com.d9tilov.android.network

import com.d9tilov.android.network.di.NetworkModule
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import timber.log.Timber

class NetworkLoggingTest {
    private val json = NetworkModule.provideJson()

    @Test
    fun `network logging follows the build type and excludes credentials`() =
        runTest {
            val messages = mutableListOf<String>()
            val tree =
                object : Timber.Tree() {
                    override fun log(
                        priority: Int,
                        tag: String?,
                        message: String,
                        t: Throwable?,
                    ) {
                        messages.add(message)
                    }
                }
            Timber.plant(tree)
            try {
                MockEngine { respond("body-secret", HttpStatusCode.OK) }.use { engine ->
                    createNetworkClient(engine, "https://api.geoapify.com/", json = json).use { client ->
                        client.get("v6/path-secret/latest/USD?apiKey=query-secret&key=other-secret")
                    }
                }
                val output = messages.joinToString("\n")
                assertEquals(BuildConfig.DEBUG, output.contains("HTTP REQUEST: GET api.geoapify.com"))
                assertEquals(BuildConfig.DEBUG, output.contains("HTTP RESPONSE: 200 api.geoapify.com"))
                if (!BuildConfig.DEBUG) assertEquals(emptyList<String>(), messages)
                listOf("path-secret", "query-secret", "other-secret", "body-secret").forEach {
                    assertFalse(output.contains(it))
                }
            } finally {
                Timber.uproot(tree)
            }
        }
}
