package com.d9tilov.android.statistics.ui.charts

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.d9tilov.android.core.constants.UiConstants.DEGREES_IN_CIRCLE
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.statistics.ui.extensions.getAngleInDegree
import com.d9tilov.android.statistics.ui.extensions.isDegreeBetween
import com.d9tilov.android.statistics.ui.extensions.isInsideCircle
import com.d9tilov.android.statistics.ui.model.chart.Pie
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val UNSELECTED_PIE_ALPHA = 0.8f
private const val MIN_PERCENT_TO_SHOW_LABEL = 5.0f
private const val LABEL_SIZE = 14f
private const val CENTER_LABEL_SIZE = 16f
private const val LABEL_RADIUS_PART = 0.7f
private const val ANIMATION_DURATION = 500
private const val ID_RANGE = 999999

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: List<Pie>,
    centerLabel: String = "",
    spaceDegree: Float = 0f,
    onPieClick: (Pie) -> Unit = {},
    selectedScale: Float = 1.1f,
    selectedPaddingDegree: Float = 5f,
    colorAnimEnterSpec: AnimationSpec<Color> = tween(ANIMATION_DURATION),
    scaleAnimEnterSpec: AnimationSpec<Float> = tween(ANIMATION_DURATION),
    spaceDegreeAnimEnterSpec: AnimationSpec<Float> = tween(ANIMATION_DURATION),
    colorAnimExitSpec: AnimationSpec<Color> = colorAnimEnterSpec,
    scaleAnimExitSpec: AnimationSpec<Float> = scaleAnimEnterSpec,
    spaceDegreeAnimExitSpec: AnimationSpec<Float> = spaceDegreeAnimEnterSpec,
    style: Pie.Style = Pie.Style.Fill,
) {
    require(data.none { it.data < 0f }) { "Data must be at least 0" }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var pieChartCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var details by remember {
        mutableStateOf(emptyList<PieDetails>())
    }
    val pieces =
        remember {
            mutableListOf<PiePiece>()
        }

    val pathMeasure =
        remember {
            PathMeasure()
        }
    LaunchedEffect(data) {
        val currDetailsSize = details.size
        details =
            if (details.isNotEmpty()) {
                data.mapIndexed { mapIndex, chart ->
                    if (mapIndex < currDetailsSize) {
                        PieDetails(
                            id = details[mapIndex].id,
                            pie = chart,
                            scale = details[mapIndex].scale,
                            color = details[mapIndex].color,
                            space = details[mapIndex].space,
                        )
                    } else {
                        PieDetails(pie = chart, color = Animatable(Color(ContextCompat.getColor(context, chart.color))))
                    }
                }
            } else {
                data.map { PieDetails(pie = it, color = Animatable(Color(ContextCompat.getColor(context, it.color)))) }
            }
        pieces.clear()
    }
    LaunchedEffect(details) {
        details.forEach {
            if (it.pie.selected) {
                scope.launch {
                    it.color.animateTo(
                        Color(ContextCompat.getColor(context, it.pie.color)).copy(alpha = UNSELECTED_PIE_ALPHA),
                        animationSpec = it.pie.colorAnimEnterSpec ?: colorAnimEnterSpec,
                    )
                }
                scope.launch {
                    it.scale.animateTo(
                        it.pie.selectedScale ?: selectedScale,
                        animationSpec = it.pie.scaleAnimEnterSpec ?: scaleAnimEnterSpec,
                    )
                }
                scope.launch {
                    it.space.animateTo(
                        it.pie.selectedPaddingDegree ?: selectedPaddingDegree,
                        animationSpec = it.pie.spaceDegreeAnimEnterSpec ?: spaceDegreeAnimEnterSpec,
                    )
                }
            } else {
                scope.launch {
                    it.color.animateTo(
                        Color(ContextCompat.getColor(context, it.pie.color)),
                        animationSpec = it.pie.colorAnimExitSpec ?: colorAnimExitSpec,
                    )
                }
                scope.launch {
                    it.scale.animateTo(
                        1f,
                        animationSpec = it.pie.scaleAnimExitSpec ?: scaleAnimExitSpec,
                    )
                }
                scope.launch {
                    it.space.animateTo(
                        0f,
                        animationSpec = it.pie.spaceDegreeAnimExitSpec ?: spaceDegreeAnimExitSpec,
                    )
                }
            }
        }
    }
    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSecondary = MaterialTheme.colorScheme.secondary
    Canvas(
        modifier =
            modifier
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val angleInDegree =
                            getAngleInDegree(
                                touchTapOffset = offset,
                                pieceOffset = pieChartCenter,
                            )

                        pieces
                            .firstOrNull { piece ->
                                isDegreeBetween(angleInDegree, piece.startFromDegree, piece.endToDegree) &&
                                    isInsideCircle(offset, pieChartCenter, piece.radius)
                            }?.let {
                                val (id, _) = it
                                details
                                    .find { it.id == id }
                                    ?.let {
                                        onPieClick(it.pie)
                                    }
                            }
                    }
                },
    ) {
        pieChartCenter = center

        val radius: Float =
            when (style) {
                is Pie.Style.Fill -> minOf(size.width, size.height) / 2
                is Pie.Style.Stroke -> minOf(size.width, size.height) / 2 - style.width.toPx() / 2
            }
        val total = details.sumOf { it.pie.data }
        details.forEachIndexed { index, detail ->
            val degree = detail.pie.data * DEGREES_IN_CIRCLE / total

            val pieStyle = detail.pie.style ?: style
            val drawStyle: DrawStyle =
                if (pieStyle is Pie.Style.Stroke) {
                    Stroke(width = ((detail.pie.style ?: style) as Pie.Style.Stroke).width.toPx())
                } else {
                    Fill
                }
            val piecePath =
                if (degree >= DEGREES_IN_CIRCLE) {
                    pieces.add(
                        PiePiece(
                            id = detail.id,
                            radius = radius * detail.scale.value,
                            startFromDegree = 0f,
                            endToDegree = DEGREES_IN_CIRCLE,
                        ),
                    )

                    Path().apply {
                        addOval(
                            oval =
                                Rect(
                                    center = center,
                                    radius = radius * detail.scale.value,
                                ),
                        )
                    }
                } else {
                    val beforeItems = data.filterIndexed { filterIndex, item -> filterIndex < index }
                    val startFromDegree = beforeItems.sumOf { it.data * DEGREES_IN_CIRCLE / total }

                    val arcRect =
                        Rect(
                            center = center,
                            radius = radius * detail.scale.value,
                        )

                    val arcStart = startFromDegree.toFloat() + detail.space.value
                    val arcSweep = degree.toFloat() - (detail.space.value * 2 + spaceDegree)

                    val piecePath =
                        Path().apply {
                            arcTo(arcRect, arcStart, arcSweep, true)
                        }

                    if (pieStyle is Pie.Style.Fill) {
                        pathMeasure.setPath(piecePath, false)
                        piecePath.reset()
                        val start = pathMeasure.getPosition(0f)
                        if (!start.isUnspecified) {
                            piecePath.moveTo(start.x, start.y)
                        }
                        piecePath.lineTo(
                            size.width / 2,
                            size.height / 2,
                        )
                        piecePath.arcTo(arcRect, arcStart, arcSweep, true)
                        piecePath.lineTo(
                            size.width / 2,
                            size.height / 2,
                        )
                    }

                    pieces.add(
                        PiePiece(
                            id = detail.id,
                            radius = radius * detail.scale.value,
                            startFromDegree = arcStart,
                            endToDegree =
                                if (arcStart + arcSweep >= DEGREES_IN_CIRCLE) {
                                    DEGREES_IN_CIRCLE
                                } else {
                                    arcStart + arcSweep
                                },
                        ),
                    )
                    piecePath
                }

            drawPath(
                path = piecePath,
                color = detail.color.value,
                style = drawStyle,
            )
            if (degree >= MIN_PERCENT_TO_SHOW_LABEL) {
                val beforeItems = data.filterIndexed { filterIndex, _ -> filterIndex < index }
                val startFromDegree = beforeItems.sumOf { it.data * DEGREES_IN_CIRCLE / total }

                // Calculate middle angle of the slice
                val middleAngle = startFromDegree + degree / 2

                // Calculate position for the label
                val labelRadius = radius * LABEL_RADIUS_PART // Place label at 70% of the radius
                var x = center.x
                var y = center.y
                if (details.size > 1 || drawStyle is Stroke) {
                    x = center.x + labelRadius * kotlin.math.cos(Math.toRadians(middleAngle)).toFloat()
                    y = center.y + labelRadius * kotlin.math.sin(Math.toRadians(middleAngle)).toFloat()
                }
                drawIntoCanvas { canvas ->
                    val textPaint =
                        android.graphics.Paint().apply {
                            color = colorPrimary.toArgb()
                            textSize = LABEL_SIZE * density
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    textPaint.setShadowLayer(2f, 1f, 1f, Color.Black.toArgb())
                    canvas.nativeCanvas.drawText(
                        detail.pie.label ?: "${detail.pie.data}",
                        x,
                        y,
                        textPaint,
                    )
                    if (drawStyle is Stroke) {
                        val textPaintCenterLabel =
                            android.graphics.Paint().apply {
                                color = colorSecondary.toArgb()
                                textSize = CENTER_LABEL_SIZE * density
                                textAlign = android.graphics.Paint.Align.CENTER
                            }

                        val maxWidth = radius
                        val textWidth = textPaintCenterLabel.measureText(centerLabel)
                        if (textWidth > maxWidth) {
                            val scale = maxWidth / textWidth
                            textPaintCenterLabel.textSize *= scale
                        }
                        canvas.nativeCanvas.drawText(
                            centerLabel,
                            center.x,
                            center.y,
                            textPaintCenterLabel,
                        )
                    }
                }
            }
        }
    }
}

private data class PieDetails(
    val id: String = Random.nextInt(0, ID_RANGE).toString(),
    val pie: Pie,
    val color: Animatable<Color, AnimationVector4D>,
    val scale: Animatable<Float, AnimationVector1D> = Animatable(1f),
    val space: Animatable<Float, AnimationVector1D> = Animatable(0f),
)

private data class PiePiece(
    val id: String,
    val radius: Float,
    val startFromDegree: Float,
    val endToDegree: Float,
)

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    MoneyManagerTheme {
        Column {
            val data by remember { mutableStateOf(pieData) }
            PieChart(
                modifier = Modifier.fillMaxSize(),
                centerLabel = "23.04.2024 - 23.05.2024",
                data = data,
                style = Pie.Style.Stroke(42.dp),
            )
        }
    }
}

private val pieData =
    listOf(
        Pie(
            label = "Android",
            data = 20.0,
            color = android.R.color.holo_red_dark,
        ),
        Pie(
            label = "IOS",
            data = 14.0,
            color = android.R.color.holo_orange_dark,
        ),
        Pie(
            label = "MacOS",
            data = 40.0,
            color = android.R.color.holo_purple,
        ),
        Pie(
            label = "Ubuntu",
            data = 10.0,
            color = android.R.color.holo_blue_bright,
        ),
        Pie(
            label = "Manjaro",
            data = 9.0,
            color = android.R.color.holo_green_light,
        ),
        Pie(
            label = "Abc",
            data = 1.0,
            color = android.R.color.holo_blue_light,
        ),
    )
