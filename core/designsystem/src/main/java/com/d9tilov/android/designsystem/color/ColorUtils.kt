package com.d9tilov.android.designsystem.color // ktlint-disable filename

import android.content.Context
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.widget.TextViewCompat
import com.d9tilov.android.designsystem.R

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

@Suppress("MagicNumber")
val colorMap = mapOf(
    0 to R.color.category_all_color,
    1 to R.color.category_red,
    2 to R.color.category_red_theme,
    3 to R.color.category_pink,
    4 to R.color.category_pink_theme,
    5 to R.color.category_purple,
    6 to R.color.category_purple_theme,
    7 to R.color.category_deep_purple,
    8 to R.color.category_deep_purple_theme,
    9 to R.color.category_indigo,
    10 to R.color.category_indigo_theme,
    11 to R.color.category_blue,
    12 to R.color.category_blue_theme,
    13 to R.color.category_light_blue,
    14 to R.color.category_light_blue_theme,
    15 to R.color.category_cyan,
    16 to R.color.category_cyan_theme,
    17 to R.color.category_teal,
    18 to R.color.category_teal_theme,
    19 to R.color.category_green,
    20 to R.color.category_green_theme,
    21 to R.color.category_light_green,
    22 to R.color.category_light_green_theme,
    23 to R.color.category_lime,
    24 to R.color.category_lime_theme,
    25 to R.color.category_yellow,
    26 to R.color.category_yellow_theme,
    27 to R.color.category_amber,
    28 to R.color.category_amber_theme,
    29 to R.color.category_orange,
    30 to R.color.category_orange_theme,
    31 to R.color.category_deep_orange,
    32 to R.color.category_deep_orange_theme,
    33 to R.color.category_brown,
    34 to R.color.category_brown_theme,
    35 to R.color.category_grey,
    36 to R.color.category_grey_theme,
    37 to R.color.category_blue_grey,
    38 to R.color.category_blue_grey_theme
)

fun fromColorRes(@ColorRes colorId: Int?) = colorMap.entries.first { it.value == colorId }.key
