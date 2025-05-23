package com.d9tilov.android.statistics.ui.model.chart

import androidx.annotation.ColorRes
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Pie(
    val label: String? = null,
    val data: Double,
    @ColorRes val color: Int,
    val selectedScale: Float? = null,
    val selectedPaddingDegree: Float? = null,
    val selected: Boolean = false,
    val colorAnimEnterSpec: AnimationSpec<Color>? = null,
    val scaleAnimEnterSpec: AnimationSpec<Float>? = null,
    val spaceDegreeAnimEnterSpec: AnimationSpec<Float>? = null,
    val colorAnimExitSpec: AnimationSpec<Color>? = null,
    val scaleAnimExitSpec: AnimationSpec<Float>? = null,
    val spaceDegreeAnimExitSpec: AnimationSpec<Float>? = null,
    val style: Style? = null,
) {
    sealed class Style {
        data object Fill : Style()

        data class Stroke(
            val width: Dp = 42.dp,
        ) : Style()
    }
}
