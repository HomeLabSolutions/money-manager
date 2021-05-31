package com.d9tilov.moneymanager.core.util

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DECIMAL_SEPARATOR
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DEFAULT_DECIMAL_SEPARATOR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

val <T> T.exhaustive: T
    get() = this

val String?.toBigDecimal: BigDecimal
    get() {
        val df = DecimalFormat()
        df.isParseBigDecimal = true
        if (TextUtils.isEmpty(this)) {
            return BigDecimal.ZERO
        }
        var result: BigDecimal
        result = try {
            this?.let {
                replace("\\s".toRegex(), "")
                    .replace(DECIMAL_SEPARATOR, DEFAULT_DECIMAL_SEPARATOR)
                    .toBigDecimal()
            } ?: BigDecimal.ZERO
        } catch (ex: NumberFormatException) {
            BigDecimal.ZERO
        }
        result = result.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()
        if (result.scale() < 0) {
            result = result.setScale(0)
        }
        return result
    }

val BigDecimal?.removeScale: BigDecimal
    get() {
        if (this == null || this == BigDecimal.ZERO) {
            return BigDecimal.ZERO
        }
        var result = setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()
        if (result.scale() < 0) {
            result = result.setScale(0)
        }

        return result
    }

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun <T> debounce(
    waitMs: Long = 300L,
    scope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

fun Long.toModInt(): Int = (this % Int.MAX_VALUE).toInt()

