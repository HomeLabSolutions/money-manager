package com.d9tilov.android.common.android.utils // ktlint-disable filename

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

