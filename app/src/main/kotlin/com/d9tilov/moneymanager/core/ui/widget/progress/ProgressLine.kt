package com.d9tilov.moneymanager.core.ui.widget.progress

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.d9tilov.moneymanager.R
import com.d9tilov.android.core.utils.divideBy

class ProgressLine @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var barWidth = BAR_WIDTH
    private var cornerRadius = DEFAULT_CORNER_RADIUS
    private var animationDuration = DEFAULT_ANIMATION_DURATION

    private var layoutHeight = 0
    private var layoutWidth = 0

    private var progressColor = Color.GREEN
    private var underLineColor = Color.GRAY

    private val progressLinePaint = Paint()
    private val underLinePaint = Paint()

    private val defaultPaddingLeft = 0
    private val defaultPaddingRight = 0

    private var currentValue: Float = 0f
    private var maxValue: Float = MAX_PERCENT_VALUE
    private var percentage = 0f
    private var barLength: Int = 0
    private var scale = 1f

    init {
        attrs?.let {
            with(getContext().obtainStyledAttributes(it, R.styleable.ProgressLine)) {
                barWidth = getDimension(R.styleable.ProgressLine_lineBarWidth, barWidth)
                cornerRadius = getDimension(R.styleable.ProgressLine_barCornerRadius, cornerRadius)
                animationDuration =
                    getInt(R.styleable.ProgressLine_barAnimationDuration, animationDuration)
                progressColor =
                    getColor(R.styleable.ProgressLine_lineProgressColor, progressColor)
                currentValue = getFloat(R.styleable.ProgressLine_currentValue, currentValue)
                maxValue = getFloat(R.styleable.ProgressLine_maxValue, maxValue)
                percentage = currentValue.toBigDecimal().divideBy(maxValue.toBigDecimal())
                    .multiply(100.toBigDecimal()).toFloat()
                underLineColor = getColor(R.styleable.ProgressLine_underLineColor, underLineColor)
                calculateBarScale()
                recycle()
                invalidate()
            }
        }
    }

    private fun calculateBarScale() {
        scale = if (percentage > MAX_PERCENT_VALUE) 1f else percentage * ERROR_PERCENT_VALUE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layoutWidth = w
        layoutHeight = h
        setupPaints()
        invalidate()
    }

    private fun setupPaints() {
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
            cornerRadius,
            cornerRadius,
            underLinePaint
        )
        canvas.drawRoundRect(
            startXp,
            (this.height / 2).toFloat() - barWidth,
            endXp,
            (this.height / 2 + barWidth),
            cornerRadius,
            cornerRadius,
            progressLinePaint
        )
    }

    fun setProgress(progress: Float, color: Int) {
        progressColor = color
        val valueAnimator = ValueAnimator
            .ofFloat(0f, progress)
            .setDuration(animationDuration.toLong())
        valueAnimator.addUpdateListener { animation ->
            percentage = animation.animatedValue as Float
            setupPaints()
            calculateBarScale()
            invalidate()
        }
        valueAnimator.start()
    }

    fun setProgress(currentValue: Float, maxValue: Float) {
        val diff = currentValue.toBigDecimal().divideBy(maxValue.toBigDecimal())
            .multiply(100.toBigDecimal())
            .toFloat() - percentage
        val valueAnimator = ValueAnimator
            .ofFloat(percentage, percentage + diff)
            .setDuration(animationDuration.toLong())
        valueAnimator.addUpdateListener { animation ->
            percentage = animation.animatedValue as Float
            setupPaints()
            calculateBarScale()
            invalidate()
        }
        valueAnimator.start()
    }

    companion object {
        private const val DEFAULT_ANIMATION_DURATION = 400
        private const val DEFAULT_CORNER_RADIUS = 0f
        private const val BAR_WIDTH = 24f
        private const val MAX_PERCENT_VALUE = 100f
        private const val ERROR_PERCENT_VALUE = 0.01f
    }
}
