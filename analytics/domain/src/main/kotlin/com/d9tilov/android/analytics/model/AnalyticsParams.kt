package com.d9tilov.android.analytics.model

sealed class AnalyticsParams(
    val name: String,
) {
    sealed class MainScreen(
        name: String,
    ) : AnalyticsParams("main.$name") {
        data object EditMode : AnalyticsParams("edit.mode")

        data object ScreenType : AnalyticsParams("screen.type")
    }

    sealed class CategoryScreen(
        name: String,
    ) : AnalyticsParams("category.$name") {
        data object ScreenType : AnalyticsParams("screen.type")
    }

    data object LoginResultProvider : AnalyticsParams("login_provider_type")

    data object Exception : AnalyticsParams("error")
}
