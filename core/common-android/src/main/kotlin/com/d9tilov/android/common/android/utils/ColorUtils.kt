package com.d9tilov.android.common.android.utils // ktlint-disable filename

import android.content.Context
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.widget.TextViewCompat

fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.setTextAppearanceFromAttr(
    textView: TextView,
    @AttrRes attrTextAppearance: Int
) {
    val styleId = TypedValue()
    if (theme.resolveAttribute(attrTextAppearance, styleId, true)) {
        TextViewCompat.setTextAppearance(textView, styleId.data)
    }
}
