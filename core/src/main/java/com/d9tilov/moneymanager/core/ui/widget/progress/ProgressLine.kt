package com.d9tilov.moneymanager.core.ui.widget.progress

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.d9tilov.moneymanager.core.R
import com.d9tilov.moneymanager.core.util.divideBy
import java.math.BigDecimal.ROUND_UP

class ProgressLine @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var showPercent = false

    private var barWidth = 24f
    private var valueTextSize = 24f
    private var percentTextSize = 20f
    private var layoutHeight = 0
    private var layoutWidth = 0

    private var progressColor = Color.GREEN
    private var valueTextColor = Color.GREEN
    private var percentTextColor = Color.WHITE
    private var underLineColor = Color.GRAY

    private val valueTextPaint = TextPaint()
    private val percentTextPaint = TextPaint()
    private val progressLinePaint = Paint()
    private val underLinePaint = Paint()

    private val defaultPaddingLeft = 0
    private val defaultPaddingRight = 30
    private val percentPaddingRight = 16

    private var valueText: String = ""
    private var percentText: String = ""
    private var valueTextWidth: Float = 0.toFloat()
    private var percentTextWidth: Float = 0.toFloat()
    private var percentTextHeight: Float = 0.toFloat()

    private var currentValue: Float = 0f
    private var maxValue: Float = 100f
    private var percentage = 0f
    private var barLength: Int = 0
    private var scale = 1f

    init {
        attrs?.let {
            with(getContext().obtainStyledAttributes(it, R.styleable.ProgressLine)) {
                if (hasValue(R.styleable.ProgressLine_value))
                    valueText = getString(R.styleable.ProgressLine_value) ?: ""

                barWidth = getDimension(R.styleable.ProgressLine_lineBarWidth, barWidth)
                showPercent = getBoolean(R.styleable.ProgressLine_showPercent, showPercent)
                progressColor =
                    getColor(R.styleable.ProgressLine_lineProgressColor, progressColor)
                valueTextSize =
                    getDimension(R.styleable.ProgressLine_valueTextSize, valueTextSize)
                percentTextSize =
                    getDimension(R.styleable.ProgressLine_percentTextSize, percentTextSize)
                currentValue = getFloat(R.styleable.ProgressLine_currentValue, currentValue)
                maxValue = getFloat(R.styleable.ProgressLine_maxValue, maxValue)
                percentage = currentValue.toBigDecimal().divideBy(maxValue.toBigDecimal())
                    .multiply(100.toBigDecimal()).toFloat()
                underLineColor = getColor(R.styleable.ProgressLine_underLineColor, underLineColor)
                valueTextColor = getColor(R.styleable.ProgressLine_valueTextColor, valueTextColor)
                percentTextColor =
                    getColor(R.styleable.ProgressLine_percentTextColor, percentTextColor)
                calculateBarScale()
                recycle()
                invalidate()
            }
        }
    }

    private fun calculateBarScale() {
        scale = if (percentage > 100) 1f else percentage * 0.01f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layoutWidth = w
        layoutHeight = h
        setupBounds()
        setupPaints()
        invalidate()
    }

    private fun setupPaints() {
        valueTextPaint.color = valueTextColor
        valueTextPaint.flags = Paint.ANTI_ALIAS_FLAG

        percentTextPaint.color = percentTextColor
        percentTextPaint.flags = Paint.ANTI_ALIAS_FLAG

        progressLinePaint.color = progressColor
        progressLinePaint.isAntiAlias = true
        progressLinePaint.style = Paint.Style.FILL
        progressLinePaint.strokeWidth = 0f

        underLinePaint.color = underLineColor
        underLinePaint.isAntiAlias = true
        underLinePaint.style = Paint.Style.FILL
        underLinePaint.strokeWidth = barWidth

        barLength =
            this.width - defaultPaddingLeft - defaultPaddingRight - paddingLeft - paddingRight
    }

    private fun setupBounds() {
        valueTextPaint.textSize = valueTextSize
        percentTextPaint.textSize = percentTextSize
        valueTextWidth = valueTextPaint.measureText(valueText)
        percentTextWidth = percentTextPaint.measureText(percentText)
        val bounds = Rect()
        percentTextPaint.getTextBounds(percentText, 0, percentText.length, bounds)
        percentTextHeight = bounds.height().toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val startXp = (paddingLeft + defaultPaddingLeft).toFloat()
        val endXp = startXp + barLength * scale
        val endXu = endXp + barLength * (1 - scale)
        canvas.drawRoundRect(
            startXp,
            (this.height / 2).toFloat() - barWidth,
            endXu,
            (this.height / 2 + barWidth),
            CORNER_RADIUS,
            CORNER_RADIUS,
            underLinePaint
        )
        canvas.drawRoundRect(
            startXp,
            (this.height / 2).toFloat() - barWidth,
            endXp,
            (this.height / 2 + barWidth),
            CORNER_RADIUS,
            CORNER_RADIUS,
            progressLinePaint
        )
        canvas.drawText(
            valueText,
            endXu - valueTextWidth,
            (this.height / 3).toFloat(),
            valueTextPaint
        )
        if (showPercent) {
            canvas.drawText(
                percentText,
                endXu - percentTextWidth - percentPaddingRight,
                (this.height / 2 + barWidth / 2 + percentTextHeight / 2),
                percentTextPaint
            )
        }
    }

    fun setProgress(progress: Float, color: Int, delayAnimDuration: Long = 0L) {
        progressColor = color
        val valueAnimator = ValueAnimator
            .ofFloat(0f, progress)
            .setDuration(ANIMATION_DURATION)
        valueAnimator.addUpdateListener { animation ->
            percentage = animation.animatedValue as Float
            setupBounds()
            setupPaints()
            calculateBarScale()
            invalidate()
        }
        Handler(Looper.myLooper()!!).postDelayed({ valueAnimator.start() }, delayAnimDuration)
    }

    fun setProgress(
        currentValue: Float,
        maxValue: Float,
        postfix: String = "",
        delayAnimDuration: Long = 0L
    ) {
        valueText = "$currentValue$postfix / $maxValue$postfix"
        val progress = currentValue.toBigDecimal().divideBy(maxValue.toBigDecimal())
            .multiply(100.toBigDecimal()).setScale(ROUND_UP).toFloat()
        percentText = context.getString(R.string.number_with_percent, progress.toString())
        val diff =
            currentValue.toBigDecimal().divideBy(maxValue.toBigDecimal())
                .multiply(100.toBigDecimal())
                .toFloat() - progress
        val valueAnimator = ValueAnimator
            .ofFloat(progress, progress + diff)
            .setDuration(ANIMATION_DURATION)
        valueAnimator.addUpdateListener { animation ->
            percentage = animation.animatedValue as Float
            setupBounds()
            setupPaints()
            calculateBarScale()
            invalidate()
        }
        Handler(Looper.myLooper()!!).postDelayed({ valueAnimator.start() }, delayAnimDuration)
    }

    companion object {
        private const val ANIMATION_DURATION = 400L
        private const val CORNER_RADIUS = 100f
    }
}
