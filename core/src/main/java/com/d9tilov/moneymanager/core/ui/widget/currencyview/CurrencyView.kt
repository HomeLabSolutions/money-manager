package com.d9tilov.moneymanager.core.ui.widget.currencyview

import android.content.Context
import android.os.Build
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.d9tilov.moneymanager.core.R
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.core.util.getColorFromAttr
import com.d9tilov.moneymanager.core.util.removeScale
import java.math.BigDecimal
import java.util.*
import kotlin.math.min

class CurrencyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var sumTextStyle =
        DEFAULT_VALUE
    private var sumTextColor =
        DEFAULT_VALUE
    private var signTextStyle =
        DEFAULT_VALUE
    private var signTextColor =
        DEFAULT_VALUE
    private var signTextSize = resources.getDimensionPixelSize(R.dimen.currency_sign_text_size)
    private var sumTextSize = resources.getDimensionPixelSize(R.dimen.currency_sum_text_size)
    private var signText = ""
    private var showUnderline = false
    private var showSign = true
    private var showSignBeforeValue = false
    private var enableInput = false
    private var showCurrencyComma = false
    private var scaleEnable = enableInput

    var sum: BigDecimal = BigDecimal.ZERO
        set(value) {
            moneyEditText.setText(formatInputSum(value))
        }
    var prefixText = ""
        set(value) {
            field = value
            prefixTextView.text = value
        }

    var marginBetweenSign = resources.getDimensionPixelSize(R.dimen.currency_margin_between_sign)
        private set
    val moneyEditText =
        MoneyEditText(context)
    val signTextView: TextView = TextView(context)
    val prefixTextView: TextView = TextView(context)

    companion object {
        private const val DEFAULT_VALUE = -1
        private const val MIN_LENGTH_OF_TEXT_SIZE_MULTIPLIER = 3

        lateinit var PLUS_SIGN: String
        lateinit var MINUS_SIGN: String
    }

    init {
        PLUS_SIGN = context.getString(R.string.plus_sign)
        MINUS_SIGN = context.getString(R.string.minus_sign)
        attrs?.let {
            with(getContext().obtainStyledAttributes(it, R.styleable.CurrencyView)) {
                showUnderline = getBoolean(R.styleable.CurrencyView_showUnderline, false)
                showSign = getBoolean(R.styleable.CurrencyView_showSign, true)
                showSignBeforeValue =
                    getBoolean(R.styleable.CurrencyView_showSignBeforeValue, false)
                enableInput = getBoolean(R.styleable.CurrencyView_enableInput, false)
                scaleEnable = getBoolean(R.styleable.CurrencyView_scaleEnabled, false)
                showCurrencyComma = getBoolean(R.styleable.CurrencyView_showComma, false)
                sumTextSize = getDimensionPixelSize(
                    R.styleable.CurrencyView_sumTextSize,
                    resources.getDimensionPixelSize(R.dimen.currency_sum_text_size)
                )
                sumTextColor = getColor(
                    R.styleable.CurrencyView_sumTextColor,
                    DEFAULT_VALUE
                )
                sumTextStyle = getResourceId(
                    R.styleable.CurrencyView_sumTextStyle,
                    DEFAULT_VALUE
                )
                sum = getString(R.styleable.CurrencyView_sum)?.toBigDecimal() ?: BigDecimal.ZERO
                initSum()

                prefixText = getString(R.styleable.CurrencyView_prefixText) ?: ""
                initPrefix()
                signTextSize = getDimensionPixelSize(
                    R.styleable.CurrencyView_signTextSize,
                    resources.getDimensionPixelSize(R.dimen.currency_sign_text_size)
                )
                signTextColor = getColor(
                    R.styleable.CurrencyView_signTextColor,
                    DEFAULT_VALUE
                )
                signText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getString(R.styleable.CurrencyView_signCode)
                        ?: resources.getString(R.string.ruble_sign)
                } else {
                    CurrencyUtils.getCurrencySignBy(
                        getString(R.styleable.CurrencyView_signCode)
                            ?: resources.getString(R.string.ruble_code)
                    )
                }
                signTextStyle = getResourceId(
                    R.styleable.CurrencyView_signTextStyle,
                    DEFAULT_VALUE
                )
                initSign()

                marginBetweenSign = getDimensionPixelSize(
                    R.styleable.CurrencyView_marginBetweenSign,
                    resources.getDimensionPixelSize(R.dimen.currency_margin_between_sign)
                )
                recycle()
            }
        }
        if (scaleEnable) {
            AutoScaleHelper.create(this, enableInput)
        }
    }

    private fun formatInputSum(value: BigDecimal): String {
        return if (showCurrencyComma) {
            String.format(Locale.getDefault(), value.removeScale.toString())
        } else {
            String.format(
                Locale.getDefault(),
                value.setScale(0, BigDecimal.ROUND_HALF_UP).toString()
            )
        }
    }

    private fun initPrefix() {
        prefixTextView.apply {
            if (sumTextStyle < 0) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sumTextSize.toFloat())
            } else {
                TextViewCompat.setTextAppearance(this, sumTextStyle)
            }
            setTextColor(context.getColorFromAttr(android.R.attr.textColorPrimary))
            if (!showUnderline) {
                background = null
            }
            isSingleLine = true
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
            text = prefixText
            includeFontPadding = false
            setPadding(0, 0, 0, 0)
            addView(this)
        }
    }

    private fun initSum() {
        moneyEditText.apply {
            showDelimiter = showCurrencyComma
            if (sumTextStyle < 0) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sumTextSize.toFloat())
            } else {
                TextViewCompat.setTextAppearance(this, sumTextStyle)
            }
            if (sumTextColor == DEFAULT_VALUE) {
                setTextColor(context.getColorFromAttr(android.R.attr.textColorPrimary))
            } else {
                setTextColor(sumTextColor)
            }
            if (!showUnderline) {
                background = null
            }
            isSingleLine = true
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
            setText(formatInputSum(sum))
            isEnabled = enableInput
            isFocusable = enableInput
            includeFontPadding = false
            setPadding(0, 0, 0, 0)
            addView(this)
        }
    }

    private fun initSign() {
        signTextView.apply {
            if (signTextStyle < 0) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, signTextSize.toFloat())
            } else {
                TextViewCompat.setTextAppearance(this, signTextStyle)
            }
            if (signTextColor == DEFAULT_VALUE) {
                setTextColor(context.getColorFromAttr(android.R.attr.textColorSecondary))
            } else {
                setTextColor(signTextColor)
            }
            if (!showSign) {
                visibility = View.GONE
            }
            text = signText
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
            includeFontPadding = false
            setPadding(0, 0, 0, 0)
            addView(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(prefixTextView, widthMeasureSpec, heightMeasureSpec)
        measureChild(moneyEditText, widthMeasureSpec, heightMeasureSpec)
        if (showSign) {
            measureChild(signTextView, widthMeasureSpec, heightMeasureSpec)
        }
        val prefixTextViewWidth = prefixTextView.measuredWidth
        val prefixTextViewHeight = prefixTextView.measuredHeight
        val moneyEditTextWidth = moneyEditText.measuredWidth
        val moneyEditTextHeight = moneyEditText.measuredHeight
        val signTextViewWidth = signTextView.measuredWidth
        val signTextViewHeight = signTextView.measuredHeight
        val width =
            paddingLeft + prefixTextViewWidth + moneyEditTextWidth + marginBetweenSign + signTextViewWidth + paddingRight
        val height = paddingTop + maxOf(
            moneyEditTextHeight,
            signTextViewHeight,
            prefixTextViewHeight
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
        val prefixTextViewWidth = prefixTextView.measuredWidth
        val prefixTextViewHeight = prefixTextView.measuredHeight
        val valueEditTextWidth = moneyEditText.measuredWidth
        val valueEditTextHeight = moneyEditText.measuredHeight
        val halfValueSize = valueEditTextHeight / 2
        val signTextViewWidth = signTextView.measuredWidth
        val signTextViewHeight = signTextView.measuredHeight
        val prefixBaseLineMargin = TextViewCompat.getLastBaselineToBottomHeight(prefixTextView)
        val valueBaseLineMargin = TextViewCompat.getLastBaselineToBottomHeight(moneyEditText)
        val signBaseLineMargin = TextViewCompat.getLastBaselineToBottomHeight(signTextView)
        prefixTextView.layout(
            paddingLeft,
            centerY + prefixBaseLineMargin + halfValueSize - valueBaseLineMargin - prefixTextViewHeight,
            paddingLeft + prefixTextViewWidth,
            centerY + prefixBaseLineMargin + halfValueSize - valueBaseLineMargin
        )
        if (showSign) {
            signTextView.layout(
                paddingLeft + prefixTextViewWidth + (if (showSignBeforeValue) 0 else (valueEditTextWidth + marginBetweenSign)),
                centerY + signBaseLineMargin + halfValueSize - valueBaseLineMargin - signTextViewHeight,
                paddingLeft + prefixTextViewWidth + (if (showSignBeforeValue) 0 else (valueEditTextWidth + marginBetweenSign + paddingRight)) + signTextViewWidth,
                centerY + signBaseLineMargin + halfValueSize - valueBaseLineMargin
            )
        }
        moneyEditText.layout(
            paddingLeft + prefixTextViewWidth + (if (showSignBeforeValue) (signTextViewWidth + marginBetweenSign) else 0),
            centerY - halfValueSize,
            paddingLeft + prefixTextViewWidth + (if (showSignBeforeValue) (signTextViewWidth + marginBetweenSign) else 0) + valueEditTextWidth,
            centerY - halfValueSize + valueEditTextHeight
        )
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        moneyEditText.addTextChangedListener(textWatcher)
    }

    fun removeTextChangedListener(textWatcher: TextWatcher) {
        moneyEditText.removeTextChangedListener(textWatcher)
    }
}
