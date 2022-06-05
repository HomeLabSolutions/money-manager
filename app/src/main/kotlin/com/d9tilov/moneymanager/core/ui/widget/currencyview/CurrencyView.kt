package com.d9tilov.moneymanager.core.ui.widget.currencyview

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import androidx.core.widget.addTextChangedListener
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.DECIMAL_LENGTH
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.MINUS_SIGN
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.removeScale
import com.d9tilov.moneymanager.core.util.toBigDecimal
import java.math.BigDecimal
import java.util.Locale
import kotlin.math.min

class CurrencyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var sumTextStyle = DEFAULT
    private var sumTextColor = DEFAULT
    private var signTextStyle = DEFAULT
    private var signTextColor = DEFAULT
    private var signTextSize = DEFAULT
    private var sumTextSize = DEFAULT
    private var currencyGravity = Gravity.START
    private var showUnderline = false
    private var showCurrencySymbol = true
    private var showCurrencySymbolBeforeValue = true
    private var enableInput = false
    private var showDecimalPart = false
    private var showShortDecimalPart = false
    private var scaleEnable = enableInput

    private var sum: BigDecimal = BigDecimal.ZERO
        set(value) = moneyEditText.setText(formatInputSum(value))
    private var currencySymbolText = ""
        set(value) {
            field = value
            currencySymbolTextView.text = value
        }
    private var minusSignText = ""
        set(value) {
            field = value
            minusSignTextView.text = value
        }

    var marginBetweenCurrencySymbol =
        resources.getDimensionPixelSize(R.dimen.currency_margin_between_currency_symbol)
        private set
    val moneyEditText = MoneyEditText(context)
    val currencySymbolTextView: TextView = TextView(context)
    val minusSignTextView: TextView = TextView(context)

    init {
        attrs?.let {
            with(getContext().obtainStyledAttributes(it, R.styleable.CurrencyView)) {
                showUnderline = getBoolean(R.styleable.CurrencyView_showUnderline, showUnderline)
                showCurrencySymbol =
                    getBoolean(R.styleable.CurrencyView_showCurrencySymbol, showCurrencySymbol)
                showCurrencySymbolBeforeValue =
                    getBoolean(
                        R.styleable.CurrencyView_showCurrencySymbolBeforeValue,
                        showCurrencySymbolBeforeValue
                    )
                enableInput = getBoolean(R.styleable.CurrencyView_enableInput, enableInput)
                scaleEnable = getBoolean(R.styleable.CurrencyView_scaleEnabled, false)
                showDecimalPart =
                    getBoolean(R.styleable.CurrencyView_showDecimalPart, showDecimalPart)
                showShortDecimalPart =
                    getBoolean(R.styleable.CurrencyView_showShortDecimalPart, showShortDecimalPart)
                sumTextSize =
                    getDimensionPixelSize(R.styleable.CurrencyView_sumTextSize, DEFAULT)
                sumTextColor = getColor(R.styleable.CurrencyView_sumTextColor, DEFAULT)
                sumTextStyle = getResourceId(R.styleable.CurrencyView_sumTextStyle, DEFAULT)
                currencyGravity = getInt(R.styleable.CurrencyView_gravity, Gravity.START)
                sum = getString(R.styleable.CurrencyView_sum)?.toBigDecimal ?: BigDecimal.ZERO
                initSum()

                initMinusSign()
                signTextSize =
                    getDimensionPixelSize(R.styleable.CurrencyView_signTextSize, DEFAULT)
                signTextColor = getColor(R.styleable.CurrencyView_signTextColor, DEFAULT)

                currencySymbolText =
                    getString(R.styleable.CurrencyView_signSymbol) ?: DEFAULT_CURRENCY_SYMBOL
                signTextStyle = getResourceId(R.styleable.CurrencyView_signTextStyle, DEFAULT)
                initCurrencySymbol()

                marginBetweenCurrencySymbol = getDimensionPixelSize(
                    R.styleable.CurrencyView_marginBetweenSign,
                    resources.getDimensionPixelSize(R.dimen.currency_margin_between_currency_symbol)
                )
                recycle()
            }
        }
        if (scaleEnable) AutoScaleHelper.create(this)
    }

    private fun formatInputSum(value: BigDecimal): String {
        if (value.signum() == 0) return "0"
        minusSignText = if (value.signum() < 0) MINUS_SIGN else ""
        return when {
            showShortDecimalPart -> {
                if (value.scale() == 0) String.format(Locale.getDefault(), value.toString())
                else String.format(
                    Locale.getDefault(),
                    value.setScale(DECIMAL_LENGTH, BigDecimal.ROUND_HALF_UP).toString()
                )
            }
            showDecimalPart -> String.format(Locale.getDefault(), value.removeScale.toString())
            else -> String.format(
                Locale.getDefault(),
                value.setScale(0, BigDecimal.ROUND_HALF_UP).toString()
            )
        }
    }

    private fun initMinusSign() {
        with(minusSignTextView) {
            if (sumTextStyle == DEFAULT) {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimensionPixelSize(R.dimen.currency_sum_text_size).toFloat()
                )
                setTextColor(R.attr.colorControlActivated)
            } else TextViewCompat.setTextAppearance(this, sumTextStyle)
            if (sumTextColor != DEFAULT) setTextColor(sumTextColor)
            if (sumTextSize != DEFAULT) setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                sumTextSize.toFloat()
            )
            if (!showUnderline) background = null
            isSingleLine = true
            gravity = currencyGravity
            text = minusSignText
            includeFontPadding = false
            setPadding(0)
            addView(this)
        }
    }

    private fun initSum() {
        with(moneyEditText) {
            showDelimiter = showDecimalPart
            if (sumTextStyle == DEFAULT) {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimensionPixelSize(R.dimen.currency_sum_text_size).toFloat()
                )
                setTextColor(R.attr.colorControlActivated)
            } else TextViewCompat.setTextAppearance(this, sumTextStyle)
            if (sumTextColor != DEFAULT) setTextColor(sumTextColor)
            if (sumTextSize != DEFAULT) setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                sumTextSize.toFloat()
            )
            if (!showUnderline) background = null
            isSingleLine = true
            gravity = currencyGravity
            setText(formatInputSum(sum))
            isEnabled = enableInput
            isFocusable = enableInput
            includeFontPadding = false
            setPadding(0)
            addView(this)
            addTextChangedListener { text: Editable? -> if (text?.isEmpty() == true) minusSignTextView.gone() }
        }
    }

    private fun initCurrencySymbol() {
        with(currencySymbolTextView) {
            if (signTextStyle == DEFAULT) {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimensionPixelSize(R.dimen.currency_sign_text_size).toFloat()
                )
                setTextColor(R.attr.colorControlActivated)
            } else TextViewCompat.setTextAppearance(this, signTextStyle)
            if (signTextColor != DEFAULT) setTextColor(signTextColor)
            if (signTextSize != DEFAULT) setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                signTextSize.toFloat()
            )
            if (!showCurrencySymbol) gone()
            text = currencySymbolText
            gravity = currencyGravity
            includeFontPadding = false
            setPadding(0)
            addView(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(minusSignTextView, widthMeasureSpec, heightMeasureSpec)
        measureChild(moneyEditText, widthMeasureSpec, heightMeasureSpec)
        if (showCurrencySymbol) {
            measureChild(currencySymbolTextView, widthMeasureSpec, heightMeasureSpec)
        }
        val minusSignTextViewWidth = minusSignTextView.measuredWidth
        val minusSignTextViewHeight = minusSignTextView.measuredHeight
        val moneyEditTextWidth = moneyEditText.measuredWidth
        val moneyEditTextHeight = moneyEditText.measuredHeight
        val currencySymbolTextViewWidth = currencySymbolTextView.measuredWidth
        val currencySymbolTextViewHeight = currencySymbolTextView.measuredHeight
        val width =
            paddingLeft + minusSignTextViewWidth + moneyEditTextWidth + marginBetweenCurrencySymbol + currencySymbolTextViewWidth + paddingRight
        val height = paddingTop + maxOf(
            moneyEditTextHeight,
            currencySymbolTextViewHeight,
            minusSignTextViewHeight
        ) + paddingBottom
        setMeasuredDimension(
            measureDimension(width, widthMeasureSpec),
            height
        )
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        if (result == desiredSize && enableInput) {
            result = MIN_LENGTH_OF_TEXT_SIZE_MULTIPLIER * sumTextSize
        }
        return result
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val centerY = (bottom - top) / 2
        val wholeLength = right - left
        val minusSignTextViewWidth = minusSignTextView.measuredWidth
        val minusSignTextViewHeight = minusSignTextView.measuredHeight
        val valueEditTextWidth = moneyEditText.measuredWidth
        val valueEditTextHeight = moneyEditText.measuredHeight
        val halfValueSize = valueEditTextHeight / 2
        val currencySymbolTextViewWidth = currencySymbolTextView.measuredWidth
        val currencySymbolTextViewHeight = currencySymbolTextView.measuredHeight
        val minusSignBaseLineMargin =
            TextViewCompat.getLastBaselineToBottomHeight(minusSignTextView)
        val valueBaseLineMargin = TextViewCompat.getLastBaselineToBottomHeight(moneyEditText)
        val currencySymbolBaseLineMargin =
            TextViewCompat.getLastBaselineToBottomHeight(currencySymbolTextView)

        when (currencyGravity) {
            Gravity.START -> {
                minusSignTextView.layout(
                    paddingLeft,
                    centerY + minusSignBaseLineMargin + halfValueSize - valueBaseLineMargin - minusSignTextViewHeight,
                    paddingLeft + minusSignTextViewWidth,
                    centerY + minusSignBaseLineMargin + halfValueSize - valueBaseLineMargin
                )
                if (showCurrencySymbol) {
                    currencySymbolTextView.layout(
                        paddingLeft + (if (showCurrencySymbolBeforeValue) minusSignTextViewWidth else (minusSignTextViewWidth + valueEditTextWidth + marginBetweenCurrencySymbol)),
                        centerY + currencySymbolBaseLineMargin + halfValueSize - valueBaseLineMargin - currencySymbolTextViewHeight,
                        paddingLeft + (if (showCurrencySymbolBeforeValue) minusSignTextViewWidth else (minusSignTextViewWidth + valueEditTextWidth + marginBetweenCurrencySymbol + paddingRight)) + currencySymbolTextViewWidth,
                        centerY + currencySymbolBaseLineMargin + halfValueSize - valueBaseLineMargin
                    )
                }
                moneyEditText.layout(
                    paddingLeft + minusSignTextViewWidth + (if (showCurrencySymbolBeforeValue) (currencySymbolTextViewWidth + marginBetweenCurrencySymbol) else 0),
                    centerY - halfValueSize,
                    paddingLeft + minusSignTextViewWidth + (if (showCurrencySymbolBeforeValue) (currencySymbolTextViewWidth + marginBetweenCurrencySymbol) else 0) + valueEditTextWidth,
                    centerY - halfValueSize + valueEditTextHeight
                )
            }
            Gravity.CENTER -> {
                minusSignTextView.layout(
                    (wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2 - if (showCurrencySymbolBeforeValue) (marginBetweenCurrencySymbol + currencySymbolTextViewWidth + minusSignTextViewWidth) else minusSignTextViewWidth,
                    centerY + minusSignBaseLineMargin + halfValueSize - valueBaseLineMargin - minusSignTextViewHeight,
                    (wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2 - if (showCurrencySymbolBeforeValue) (marginBetweenCurrencySymbol + currencySymbolTextViewWidth) else 0,
                    centerY + minusSignBaseLineMargin + halfValueSize - valueBaseLineMargin
                )
                if (showCurrencySymbol) {
                    currencySymbolTextView.layout(
                        if (showCurrencySymbolBeforeValue) (wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2 - marginBetweenCurrencySymbol - currencySymbolTextViewWidth
                        else ((wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2 + valueEditTextWidth + marginBetweenCurrencySymbol),
                        centerY + currencySymbolBaseLineMargin + halfValueSize - valueBaseLineMargin - currencySymbolTextViewHeight,
                        if (showCurrencySymbolBeforeValue) (wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2 - marginBetweenCurrencySymbol
                        else ((wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2 + valueEditTextWidth + marginBetweenCurrencySymbol + currencySymbolTextViewWidth),
                        centerY + currencySymbolBaseLineMargin + halfValueSize - valueBaseLineMargin
                    )
                }
                moneyEditText.layout(
                    (wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2,
                    centerY - halfValueSize,
                    (wholeLength - paddingLeft - paddingRight) / 2 - valueEditTextWidth / 2 + valueEditTextWidth,
                    centerY - halfValueSize + valueEditTextHeight
                )
            }
            Gravity.END -> {
                minusSignTextView.layout(
                    right - left - paddingRight - valueEditTextWidth - marginBetweenCurrencySymbol - currencySymbolTextViewWidth - minusSignTextViewWidth,
                    centerY + minusSignBaseLineMargin + halfValueSize - valueBaseLineMargin - minusSignTextViewHeight,
                    right - left - paddingRight - valueEditTextWidth - marginBetweenCurrencySymbol - currencySymbolTextViewWidth,
                    centerY + minusSignBaseLineMargin + halfValueSize - valueBaseLineMargin
                )
                if (showCurrencySymbol) {
                    currencySymbolTextView.layout(
                        if (showCurrencySymbolBeforeValue) right - left - paddingRight - valueEditTextWidth - marginBetweenCurrencySymbol - currencySymbolTextViewWidth
                        else right - left - paddingRight - currencySymbolTextViewWidth,
                        centerY + currencySymbolBaseLineMargin + halfValueSize - valueBaseLineMargin - currencySymbolTextViewHeight,
                        if (showCurrencySymbolBeforeValue) right - left - paddingRight - valueEditTextWidth - marginBetweenCurrencySymbol
                        else right - left - paddingRight,
                        centerY + currencySymbolBaseLineMargin + halfValueSize - valueBaseLineMargin
                    )
                }
                moneyEditText.layout(
                    if (showCurrencySymbolBeforeValue) right - left - paddingRight - valueEditTextWidth
                    else right - left - paddingRight - currencySymbolTextViewWidth - marginBetweenCurrencySymbol - valueEditTextWidth,
                    centerY - halfValueSize,
                    if (showCurrencySymbolBeforeValue) right - left - paddingRight
                    else right - left - paddingRight - currencySymbolTextViewWidth - marginBetweenCurrencySymbol,
                    centerY - halfValueSize + valueEditTextHeight
                )
            }
        }
    }

    fun setValue(value: BigDecimal, currencyCode: String) {
        this.sum = value
        this.currencySymbolText = currencyCode.getSymbolByCode()
    }

    fun setColor(color: Int) {
        moneyEditText.setTextColor(color)
        currencySymbolTextView.setTextColor(color)
        minusSignTextView.setTextColor(color)
    }

    fun getValue(): BigDecimal = moneyEditText.text.toString().toBigDecimal

    override fun onInterceptTouchEvent(ev: MotionEvent?) = !enableInput

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        bundle.putString(SPARSE_STATE_KEY, moneyEditText.text.toString())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            val bundle = newState
            moneyEditText.setText(bundle.getString(SPARSE_STATE_KEY, "0"))
            newState = bundle.getParcelable(SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(newState)
    }

    fun addOnCurrencyClickListener(listener: (view: View) -> Unit) {
        currencySymbolTextView.setOnClickListener { listener.invoke(it) }
    }

    companion object {
        private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"

        private const val DEFAULT = -1
        private const val MIN_LENGTH_OF_TEXT_SIZE_MULTIPLIER = 5
    }
}
