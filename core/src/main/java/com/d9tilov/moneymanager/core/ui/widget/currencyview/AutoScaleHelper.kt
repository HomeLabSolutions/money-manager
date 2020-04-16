package com.d9tilov.moneymanager.core.ui.widget.currencyview

import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.TransformationMethod
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import kotlin.math.max
import kotlin.math.min

class AutoScaleHelper(
    private val currencyView: CurrencyView, private val enableInput: Boolean
) {

    companion object {
        private const val PRECISION = 0.5f
        private const val TEXT_WIDTH_OF_WHOLE_WIDTH_PERCENTAGE = 0.95

        fun create(
            rootView: CurrencyView, enableInput: Boolean
        ): AutoScaleHelper {
            val helper = AutoScaleHelper(rootView, enableInput)
            val minTextSize = helper.minTextSize
            helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, minTextSize)
            helper.setEnabled()
            return helper
        }

    }

    private val prefixTextView: TextView = currencyView.prefixTextView
    private val valueTextView: TextView = currencyView.moneyEditText
    private val signTextView = currencyView.signTextView
    private val valuePaint = TextPaint()
    private val signPaint = TextPaint()
    private val prefixPaint = TextPaint()
    private val textSizeMultiplier: Float = valueTextView.textSize / signTextView.textSize
    private var minTextSize = min(
        valueTextView.context.resources.displayMetrics.scaledDensity * PRECISION,
        signTextView.context.resources.displayMetrics.scaledDensity * PRECISION
    )
    private val valueTextSize = valueTextView.textSize
    private val signTextSize = signTextView.textSize
    private val maxTextSize = max(valueTextSize, signTextSize)
    private val textWatcher = AutoScaleTextWatcher()
    private val onLayoutChangeListener = AutoScaleOnLayoutChangeListener()

    fun setMinTextSize(
        unit: Int,
        size: Float
    ): AutoScaleHelper {
        setRawMinTextSize(
            TypedValue.applyDimension(
                unit,
                size,
                valueTextView.context.resources.displayMetrics
            )
        )
        return this
    }

    private fun setRawMinTextSize(size: Float) {
        if (size != minTextSize) {
            minTextSize = size
            autoScale()
        }
    }

    private fun setEnabled() {
        valueTextView.addTextChangedListener(textWatcher)
        valueTextView.addOnLayoutChangeListener(onLayoutChangeListener)
        autoScale()
    }

    private fun autoScale() {
        val targetWidth =
            currencyView.width * TEXT_WIDTH_OF_WHOLE_WIDTH_PERCENTAGE - currencyView.paddingLeft - currencyView.paddingRight
        if (targetWidth <= 0) {
            return
        }
        var text = valueTextView.text
        var signText = signTextView.text
        val method: TransformationMethod? = valueTextView.transformationMethod
        text = method?.getTransformation(text, valueTextView) ?: text
        val methodSign: TransformationMethod? = signTextView.transformationMethod
        signText = methodSign?.getTransformation(signText, signTextView) ?: signText
        val context = valueTextView.context
        val displayMetrics: DisplayMetrics
        var size = maxTextSize
        val high = size
        displayMetrics = context.resources.displayMetrics
        valuePaint.set(valueTextView.paint)
        signPaint.set(signTextView.paint)
        prefixPaint.set(prefixTextView.paint)
        valuePaint.textSize = size
        signPaint.textSize = size / textSizeMultiplier
        if ((valuePaint.measureText(text, 0, text.length)
                    + currencyView.marginBetweenSign
                    + signPaint.measureText(signText, 0, signText.length)) > targetWidth
        ) {
            size = getAutoScaleTextSize(
                text, signText, targetWidth.toFloat(), 0f, high,
                displayMetrics
            )
        }
        if (size < minTextSize) {
            size = minTextSize
        }
        prefixTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        setRealPrefixWidth()
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        setRealValueWidth()
        signTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size / textSizeMultiplier)
        setRealSignWidth()
    }

    private fun setRealPrefixWidth() {
        if (!enableInput) {
            val text = prefixTextView.text
            val measuredTextWidth = prefixPaint.measureText(text, 0, text.length)
            val measuredView = prefixTextView.width
            if (measuredTextWidth < measuredView) {
                prefixTextView.layoutParams.width = measuredTextWidth.toInt()
            }
        }
    }

    private fun setRealValueWidth() {
        if (!enableInput) {
            val text = valueTextView.text
            val measuredTextWidth = valuePaint.measureText(text, 0, text.length)
            val measuredView = valueTextView.width
            if (measuredTextWidth < measuredView) {
                valueTextView.layoutParams.width = measuredTextWidth.toInt()
            }
        }
    }

    private fun setRealSignWidth() {
        if (!enableInput) {
            val text = signTextView.text
            val measuredTextWidth = signPaint.measureText(text, 0, text.length)
            val measuredView = signTextView.width
            if (measuredTextWidth < measuredView) {
                signTextView.layoutParams.width = measuredTextWidth.toInt()
            }
        }
    }

    //Binary search to define new text size
    private fun getAutoScaleTextSize(
        valueText: CharSequence,
        signText: CharSequence,
        targetWidth: Float,
        low: Float,
        high: Float,
        displayMetrics: DisplayMetrics
    ): Float {
        val mid = (low + high) / 2
        valuePaint.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX, mid,
            displayMetrics
        )
        signPaint.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX, mid / textSizeMultiplier,
            displayMetrics
        )

        val maxLineWidth =
            valuePaint.measureText(valueText, 0, valueText.length) +
                    currencyView.marginBetweenSign +
                    signPaint.measureText(signText, 0, signText.length)
        return when {
            high - low < PRECISION -> {
                low
            }
            maxLineWidth > targetWidth -> {
                getAutoScaleTextSize(
                    valueText, signText, targetWidth, low, mid,
                    displayMetrics
                )
            }
            maxLineWidth < targetWidth -> {
                getAutoScaleTextSize(
                    valueText, signText, targetWidth, mid, high,
                    displayMetrics
                )
            }
            else -> {
                mid
            }
        }
    }

    private inner class AutoScaleTextWatcher : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            autoScale()
        }

        override fun afterTextChanged(editable: Editable) { /* do nothing */
        }
    }

    private inner class AutoScaleOnLayoutChangeListener :
        View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            autoScale()
        }
    }
}
