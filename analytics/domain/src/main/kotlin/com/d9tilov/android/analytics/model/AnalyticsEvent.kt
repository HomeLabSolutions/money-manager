package com.d9tilov.android.analytics.model

sealed class AnalyticsEvent(
    val name: String,
) {
    sealed class Internal(
        name: String,
    ) : AnalyticsEvent(name)

    sealed class Client(
        name: String,
    ) : AnalyticsEvent(name) {
        sealed class Auth(
            name: String,
        ) : Client(name) {
            data object Login : Auth("login")

            data object Logout : Auth("logout")
        }
    }
}
