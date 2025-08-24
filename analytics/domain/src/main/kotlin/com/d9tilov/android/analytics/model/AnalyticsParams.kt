package com.d9tilov.android.analytics.model

sealed class AnalyticsParams(
    val name: String,
) {
    sealed class Screen(
        name: String,
    ) : AnalyticsParams(name) {
        data object Name : Screen("name")

        data object Click : Screen("click")

        data object Mode : Screen("mode")

        data object Type : Screen("type")
    }

    data object Method : AnalyticsParams("method")

    data object Currency : AnalyticsParams("currency")

    sealed class Auth(
        name: String,
    ) : AnalyticsParams(name) {
        data object Action : Auth("action")

        data object Provider : Auth("provider")
    }

    data object Exception : AnalyticsParams("error")
}
