package com.d9tilov.android.analytics.data.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

@Composable
fun TrackScreenEvent(screenName: String) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
        onDispose {
            FirebaseAnalytics.getInstance(context).logEvent(SCREEN_CLOSE) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            }
        }
    }
}

private const val SCREEN_CLOSE = "screen_close"