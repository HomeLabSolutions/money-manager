package com.d9tilov.moneymanager.core.util.glide

import android.app.ActivityManager
import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class MoneyManagerGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val defaultOptions = RequestOptions()
            .format(if (activityManager.isLowRamDevice) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888)
            // Disable hardware bitmaps as they don't play nicely with Palette
            .disallowHardwareConfig()
        builder.setDefaultRequestOptions(defaultOptions)
    }

    override fun isManifestParsingEnabled() = false
}
