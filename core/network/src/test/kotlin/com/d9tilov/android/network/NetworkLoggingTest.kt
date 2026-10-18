package com.d9tilov.android.network

import com.d9tilov.android.network.di.NetworkLoggingModule
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import timber.log.Timber

class NetworkLoggingTest {
    @Test
    fun `shared debug logger hides the geoapify key in request urls`() {
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
            val client =
                OkHttpClient
                    .Builder()
                    .addInterceptor(NetworkLoggingModule.provideLoggingInterceptor())
                    .addInterceptor { chain ->
                        Response
                            .Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .code(200)
                            .message("OK")
                            .body("{}".toResponseBody())
                            .build()
                    }.build()
            val request =
                Request
                    .Builder()
                    .url("https://api.geoapify.com/v2/places?apiKey=test-only-key")
                    .build()
            client.newCall(request).execute().close()
            val output = messages.joinToString("\n")
            assertTrue(output.contains("api.geoapify.com"))
            assertFalse(output.contains("test-only-key"))
        } finally {
            Timber.uproot(tree)
        }
    }
}
