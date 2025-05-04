package com.d9tilov.android.statistics.ui.extensions

import androidx.compose.ui.geometry.Offset
import com.d9tilov.android.core.constants.UiConstants.DEGREES_IN_CIRCLE
import com.d9tilov.android.statistics.ui.model.chart.Vector
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

fun getAngleInDegree(
    touchTapOffset: Offset,
    pieceOffset: Offset,
): Float {
    // Create vectors: u (from piece to tap point) and v (reference vector along x-axis)
    val u = Vector(touchTapOffset.x - pieceOffset.x, touchTapOffset.y - pieceOffset.y)

    // Reference vector along the positive x-axis
    val v = Vector(1f, 0f)

    // Compute the dot product of vectors u and v
    val dotProduct = u.x * v.x + u.y * v.y

    // Compute the magnitudes of vectors u and v
    val magnitudeU = sqrt(u.x.pow(2) + u.y.pow(2))
    val magnitudeV = sqrt(v.x.pow(2) + v.y.pow(2))

    // Calculate the angle in radians using the dot product formula
    val angleInRadians = acos(dotProduct / (magnitudeU * magnitudeV))

    // Convert the angle from radians to degrees
    // Adjust the angle based on the y-direction to get the correct orientation
    val angleInDegrees =
        if (touchTapOffset.y - pieceOffset.y > 0) {
            angleInRadians * (DEGREES_IN_CIRCLE / 2 / PI)
        } else {
            DEGREES_IN_CIRCLE - angleInRadians * (DEGREES_IN_CIRCLE / 2 / PI)
        }
    return angleInDegrees.toFloat()
}

fun isDegreeBetween(
    target: Float,
    start: Float,
    end: Float,
): Boolean {
    return target in start..end
}
