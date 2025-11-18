package com.d9tilov.android.designsystem

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

private const val FULL_PERCENTAGE = 100f

@Composable
fun ProgressIndicator(
    indicatorProgress: Float,
    modifier: Modifier,
) {
    val progressAnimation by animateFloatAsState(
        targetValue = indicatorProgress,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "",
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .height(4.dp),
    ) {
        val cornerRadius = 2.dp.toPx()

        drawRoundRect(
            color = surfaceVariant,
            topLeft = Offset.Zero,
            size = Size(size.width, size.height),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        )

        if (progressAnimation > 0f) {
            val progressWidth = size.width * (progressAnimation / FULL_PERCENTAGE)
            drawRoundRect(
                color = primaryColor,
                topLeft = Offset.Zero,
                size = Size(progressWidth, size.height),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            )
        }
    }
}
