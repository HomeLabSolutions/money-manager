package com.d9tilov.moneymanager.core.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import org.xmlpull.v1.XmlPullParserException

fun createTintDrawable(context: Context, @DrawableRes icon: Int, @ColorRes color: Int): Drawable {
    val vectorDrawable = VectorDrawableCompat.create(
        context.resources,
        icon,
        null
    ) ?: throw XmlPullParserException("Wrong vector xml file format")
    val drawable = DrawableCompat.wrap(vectorDrawable)
    DrawableCompat.setTint(
        drawable.mutate(),
        ContextCompat.getColor(context, color)
    )
    return drawable
}
