package com.d9tilov.android.core.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger

/** An exact decimal with a fixed number of fractional digits in its plain representation. */
class Decimal private constructor(
    private val value: BigDecimal,
    val fractionDigits: Int = 0,
) : Comparable<Decimal> {
    init {
        require(value.exponent in supportedPowers) { "Decimal exponent out of range: ${value.exponent}" }
    }

    constructor(text: String) : this(parse(text))

    private constructor(parts: ParsedDecimal) : this(parts.value, parts.fractionDigits)

    constructor(value: Int) : this(BigDecimal.fromInt(value))

    constructor(value: Long) : this(BigDecimal.fromLong(value))

    operator fun plus(other: Decimal): Decimal =
        Decimal(value + other.value, maxOf(fractionDigits, other.fractionDigits))

    operator fun minus(other: Decimal): Decimal =
        Decimal(value - other.value, maxOf(fractionDigits, other.fractionDigits))

    operator fun times(other: Decimal): Decimal {
        val scale = checkedFractionDigits(fractionDigits.toLong() + other.fractionDigits)
        return Decimal(value * other.value, scale)
    }

    fun negate(): Decimal = Decimal(value.negate(), fractionDigits)

    fun signum(): Int = value.signum()

    fun divide(
        divisor: Decimal,
        fractionDigits: Int,
    ): Decimal {
        checkedFractionDigits(fractionDigits.toLong())
        if (divisor.signum() == 0) throw ArithmeticException("Division by zero")
        if (signum() == 0) return Decimal(BigDecimal.ZERO, fractionDigits)

        // Divide the unscaled magnitudes and round the exact remainder away from zero on a tie.
        val shift = value.powerOfTen() - divisor.value.powerOfTen() + fractionDigits
        require(shift in supportedPowers) { "Decimal division shift out of range: $shift" }
        val numerator = value.significand.abs()
        val denominator = divisor.value.significand.abs()
        val scaledNumerator = if (shift > 0) numerator * BigInteger.TEN.pow(shift) else numerator
        val scaledDenominator = if (shift < 0) denominator * BigInteger.TEN.pow(-shift) else denominator
        val (quotient, remainder) = scaledNumerator.divideAndRemainder(scaledDenominator)
        val rounded = if (remainder + remainder >= scaledDenominator) quotient + BigInteger.ONE else quotient
        val signed = if (signum() == divisor.signum()) rounded else rounded.negate()

        val result = BigDecimal.fromBigInteger(signed).moveDecimalPoint(-fractionDigits.toLong())
        return Decimal(result, fractionDigits)
    }

    fun roundTo(fractionDigits: Int): Decimal =
        when {
            fractionDigits == this.fractionDigits -> this
            fractionDigits > this.fractionDigits -> Decimal(value, checkedFractionDigits(fractionDigits.toLong()))
            else -> divide(ONE, fractionDigits)
        }

    fun movePoint(power: Int): Decimal {
        val scale = checkedFractionDigits((fractionDigits.toLong() - power).coerceAtLeast(0))
        require(value.exponent + power in supportedPowers) { "Decimal point shift out of range: $power" }
        return Decimal(value.moveDecimalPoint(power), scale)
    }

    fun withoutTrailingFractionZeros(): Decimal {
        if (fractionDigits == 0) return this
        if (signum() == 0) return ZERO

        var scale = -value.powerOfTen()
        var significand = value.significand
        while (scale > 0) {
            val (quotient, remainder) = significand.divideAndRemainder(BigInteger.TEN)
            if (!remainder.isZero()) break
            significand = quotient
            scale--
        }
        val compactScale = scale.coerceAtLeast(0).toInt()
        return if (compactScale == fractionDigits) this else Decimal(value, compactScale)
    }

    fun toPlainString(): String {
        val expanded = value.toStringExpanded()
        val point = expanded.indexOf('.')
        val existingFractionDigits = if (point < 0) 0 else expanded.length - point - 1
        if (existingFractionDigits == fractionDigits) return expanded

        val whole = if (point < 0) expanded else expanded.substring(0, point)
        val fraction = if (point < 0) "" else expanded.substring(point + 1).trimEnd('0')
        check(fraction.length <= fractionDigits) { "Decimal value exceeds its fractional digit count" }
        if (fractionDigits == 0) return whole
        return "$whole.${fraction.padEnd(fractionDigits, '0')}"
    }

    override fun compareTo(other: Decimal): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is Decimal && compareTo(other) == 0

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = toPlainString()

    companion object {
        val ZERO = Decimal(BigDecimal.ZERO)
        val ONE = Decimal(BigDecimal.ONE)
        val TEN = Decimal(BigDecimal.TEN)
    }
}

private const val MAX_DECIMAL_DIGITS = 100_000
private val supportedPowers = -MAX_DECIMAL_DIGITS.toLong()..MAX_DECIMAL_DIGITS.toLong()

private data class ParsedDecimal(
    val value: BigDecimal,
    val fractionDigits: Int,
)

private fun parse(text: String): ParsedDecimal {
    val start = text.leadingSignLength()
    var end = start
    var point = -1
    while (end < text.length) {
        val character = text[end]
        if (character == '.' && point == -1) {
            point = end
        } else if (character !in '0'..'9') {
            break
        }
        end++
    }

    val fractionLength = if (point == -1) 0 else end - point - 1
    val digitCount = end - start - if (point == -1) 0 else 1
    val exponent = parseExponent(text, end)
    if (digitCount !in 1..MAX_DECIMAL_DIGITS || exponent == null) throw invalidDecimal(text)

    val digits =
        if (point == -1) {
            text.substring(start, end)
        } else {
            text.substring(start, point) + text.substring(point + 1, end)
        }
    return buildDecimal(text, digits, exponent.toLong() - fractionLength)
}

private fun buildDecimal(
    text: String,
    digits: String,
    power: Long,
): ParsedDecimal {
    val significantDigits = digits.trimStart('0').length
    val normalizedExponent = power + (significantDigits - 1).coerceAtLeast(0)
    if (power !in supportedPowers || normalizedExponent !in supportedPowers) throw invalidDecimal(text)

    val significand = BigInteger.parseString(if (text.startsWith('-')) "-$digits" else digits)
    val value = BigDecimal.fromBigInteger(significand).moveDecimalPoint(power)
    return ParsedDecimal(value, (-power).coerceAtLeast(0).toInt())
}

private fun parseExponent(
    text: String,
    start: Int,
): Int? {
    if (start == text.length) return 0
    if (text[start] != 'e' && text[start] != 'E') return null
    val exponent = text.substring(start + 1)
    if ((exponent.leadingSignLength() until exponent.length).any { exponent[it] !in '0'..'9' }) return null
    return exponent.toIntOrNull()
}

private fun String.leadingSignLength(): Int =
    when (firstOrNull()) {
        '+', '-' -> 1
        else -> 0
    }

private fun invalidDecimal(text: String): NumberFormatException = NumberFormatException("Invalid decimal: $text")

private fun checkedFractionDigits(value: Long): Int {
    require(value in 0..MAX_DECIMAL_DIGITS.toLong()) { "Fractional digit count out of range: $value" }
    return value.toInt()
}

private fun BigDecimal.powerOfTen(): Long = exponent - precision + 1
