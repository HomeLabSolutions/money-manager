package com.d9tilov.moneymanager.core.ui.widget.currencyview

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.d9tilov.moneymanager.core.R
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DECIMAL_LENGTH
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DECIMAL_SEPARATOR
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DEFAULT_DECIMAL_SEPARATOR
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.FORMAT_STRING
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.GROUPING_SEPARATOR
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.ZERO
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.StringTokenizer

class MoneyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyle) {

    companion object {
        private const val MAX_LENGTH = 15
    }

    private var prevInput = ""
    private var isFormatting = false
    private val allowableSymbols = setOf(
        context.getString(R.string.minus_sign),
        GROUPING_SEPARATOR.toString(),
        DECIMAL_SEPARATOR,
        DEFAULT_DECIMAL_SEPARATOR
    )
    var showDelimiter = false
    private lateinit var formatter: DecimalFormat

    init {
        initInputFilter()
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        isSingleLine = true
        maxLines = 1
        initFormatter()
        includeFontPadding = false
    }

    private fun initInputFilter() {
        val inputFilterArray = arrayOfNulls<InputFilter>(2)
        inputFilterArray[0] = InputFilter.LengthFilter(MAX_LENGTH)
        inputFilterArray[1] = object : InputFilter {
            override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                var ch: Char
                for (i in start until end) {
                    ch = source[i]
                    if (!(Character.isDigit(ch)
                            || Character.isWhitespace(ch)
                            || allowableSymbols.contains(ch.toString()))
                    ) {
                        return ""
                    }
                }
                return null
            }
        }
        filters = inputFilterArray
    }

    private fun initFormatter() {
        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.groupingSeparator = GROUPING_SEPARATOR
        if (showDelimiter) {
            formatSymbols.decimalSeparator = DECIMAL_SEPARATOR[0]
        }
        formatter = DecimalFormat(FORMAT_STRING, formatSymbols)
    }

    override fun onTextChanged(s: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (isFormatting) {
            return
        }
        if (s.isNotEmpty()) {
            isFormatting = true
            formatInput(s.toString(), start, lengthAfter)
        } else {
            hint = ZERO
        }
        isFormatting = false
    }

    private fun formatInput(input: String, start: Int, lengthAfter: Int) {
        val formattedInput = input
            .dropLastWhile { !showDelimiter && it.toString() == DECIMAL_SEPARATOR }
            .replace(DEFAULT_DECIMAL_SEPARATOR, DECIMAL_SEPARATOR)
            .trim()
        val userInput = if (formattedInput.isEmpty()) ZERO else formattedInput
        // ,, -> ,
        if (userInput.count { ch -> ch.toString() == DECIMAL_SEPARATOR } > 1) {
            setText(prevInput)
            setSelection(start)
            return
        }
        val startWithDecimalSeparator = userInput.startsWith(DECIMAL_SEPARATOR)
        // , -> 0,
        if (startWithDecimalSeparator && userInput.length == 1) {
            val zeroWithSeparator = "$ZERO$DECIMAL_SEPARATOR"
            setText(zeroWithSeparator)
            setSelection(2)
            prevInput = zeroWithSeparator
            return
        }
        // ,d -> 0.d
        if (startWithDecimalSeparator && userInput.length > 1) {
            val st = StringTokenizer(userInput)
            val afterDot = st.nextToken(DECIMAL_SEPARATOR)
            var decimalPart = extractDigits(afterDot)
            decimalPart = if (decimalPart.length > DECIMAL_LENGTH) decimalPart.substring(
                0,
                DECIMAL_LENGTH
            ) else decimalPart
            val result = "$ZERO$DECIMAL_SEPARATOR$decimalPart"
            setText(result)
            setSelection(2)
            prevInput = result
            return
        }
        // 0d -> d
        if (userInput.startsWith(ZERO) && userInput.length > 1 && DECIMAL_SEPARATOR != userInput[1].toString()) {
            val result = userInput.substring(1, userInput.length)
            setText(result)
            setSelection(1)
            prevInput = result
            return
        }
        val sbResult = StringBuilder()
        val notFormattedText = userInput.replace(GROUPING_SEPARATOR.toString(), "")
        if (notFormattedText.contains(DECIMAL_SEPARATOR)) {
            // Есть дробная часть
            val wholeText =
                notFormattedText.split(DECIMAL_SEPARATOR).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            if (wholeText.isEmpty()) {
                return
            }
            val integerPart = wholeText[0]
            if (integerPart.isEmpty()) {
                return
            }
            sbResult
                .append(formatter.format(integerPart.toDouble()))
                .append(DECIMAL_SEPARATOR)
            if (wholeText.size > 1) {
                val decimalPart = wholeText[1]
                sbResult.append(
                    if (decimalPart.length > DECIMAL_LENGTH) decimalPart.substring(
                        0,
                        DECIMAL_LENGTH
                    ) else decimalPart
                )
            }
        } else {
            // без дробной части
            sbResult.append(formatter.format(notFormattedText.toDouble()))
        }
        val result = sbResult.toString()
        setText(result)

        // Вычисляем позицию селектора
        var newStart = if (getCharOccurrence(result, DECIMAL_SEPARATOR[0]) > getCharOccurrence(
                prevInput,
                DECIMAL_SEPARATOR[0]
            )
        )
            result.indexOf(DECIMAL_SEPARATOR) + 1
        else
            start + (if (lengthAfter > 0) lengthAfter else 0) + getCharOccurrence(
                result,
                GROUPING_SEPARATOR
            ) - getCharOccurrence(
                prevInput,
                GROUPING_SEPARATOR
            )
        if (newStart >= text.toString().length) {
            newStart = text.toString().length
        } else if (newStart <= 0) {
            newStart = if (result.isNotEmpty()) {
                text.toString().length
            } else {
                0
            }
        }
        setSelection(newStart)
        prevInput = result
    }

    private fun extractDigits(input: String) = input.replace("\\D+".toRegex(), "")

    private fun getCharOccurrence(input: String, c: Char) = input.filter { it == c }.length
}
