package com.d9tilov.android.analytics.model

sealed class AnalyticsParams(
    val name: String,
) {
    data object MainScreenEditMode : AnalyticsParams("main_screen_edit_mode")

    data object MainScreenType : AnalyticsParams("main_screen_type")

    data object LoginResultProvider : AnalyticsParams("login_provider_type")

    data object Exception : AnalyticsParams("error")
}
