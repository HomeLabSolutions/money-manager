package com.d9tilov.android.designsystem

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f
private const val DECIMAL_SIZE = 2
private const val DELIMITER = "."

@ExperimentalMaterialApi
@Composable
fun CurrencyTextFieldExtraSmall(
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
        .copy(
            fontSize = dimensionResource(R.dimen.currency_extra_small_text_size).value.sp,
            color = MaterialTheme.colorScheme.primary
        ),
    inputValueChanged: (String) -> Unit = {}
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged
    )
}

@ExperimentalMaterialApi
@Composable
fun CurrencyTextFieldSmall(
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
        .copy(
            fontSize = dimensionResource(R.dimen.currency_sum_small_text_size).value.sp,
            color = MaterialTheme.colorScheme.primary
        ),
    inputValueChanged: (String) -> Unit = {}
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged
    )
}

@ExperimentalMaterialApi
@Composable
fun CurrencyTextFieldMedium(
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
        .copy(
            fontSize = dimensionResource(R.dimen.currency_sum_medium_text_size).value.sp,
            color = MaterialTheme.colorScheme.primary
        ),
    inputValueChanged: (String) -> Unit = {}
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged
    )
}

@ExperimentalMaterialApi
@Composable
fun CurrencyTextFieldBig(
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
        .copy(
            fontSize = dimensionResource(R.dimen.currency_sum_big_text_size).value.sp,
            color = MaterialTheme.colorScheme.primary
        ),
    inputValueChanged: (String) -> Unit = {}
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged
    )
}

@ExperimentalMaterialApi
@Composable
fun CurrencyTextFieldExtraBig(
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
        .copy(
            fontSize = dimensionResource(R.dimen.currency_sum_extra_big_text_size).value.sp,
            color = MaterialTheme.colorScheme.primary
        ),
    inputValueChanged: (String) -> Unit = {}
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged
    )
}

@ExperimentalMaterialApi
@Composable
private fun CurrencyTextField(
    amount: String,
    currencyCode: String,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle,
    inputValueChanged: (String) -> Unit
) {
    if (isEditable) {
        BoxWithConstraints(modifier = modifier.fillMaxWidth().clipToBounds()) {
            var shrunkFontSize = style.fontSize
            val calculateIntrinsics = @Composable {
                ParagraphIntrinsics(
                    text = currencyCode + amount,
                    style = TextStyle(
                        fontSize = shrunkFontSize,
                        fontWeight = style.fontWeight,
                        lineHeight = style.lineHeight,
                        textAlign = style.textAlign
                    ),
                    density = LocalDensity.current,
                    fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
                )
            }

            var intrinsics = calculateIntrinsics()
            val maxInputWidth = LocalDensity.current.run {
                val textFieldDefaultHorizontalPadding = 16.dp.toPx()
                maxWidth.toPx() - 2 * textFieldDefaultHorizontalPadding
            }
            while (intrinsics.maxIntrinsicWidth > maxInputWidth) {
                shrunkFontSize *= TEXT_SCALE_REDUCTION_INTERVAL
                intrinsics = calculateIntrinsics()
            }
            Row(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = currencyCode,
                    fontSize = shrunkFontSize * 0.6,
                    style = style,
                    modifier = Modifier.alignByBaseline()
                        .offset(x = 12.dp)
                )
                var text by remember { mutableStateOf(amount) }
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alignByBaseline(),
                    value = amount,
                    onValueChange = { input ->
                        text = when {
                            input.isEmpty() -> input
                            input[0] == '0' -> input.substring(1)
                            else -> {
                                when (input.toBigDecimalOrNull()) {
                                    null -> text
                                    else -> {
                                        val number = input.toBigDecimal()
                                        if (number.scale() > DECIMAL_SIZE) {
                                            input.substring(
                                                0,
                                                input.indexOf(DELIMITER) + DECIMAL_SIZE + 1
                                            )
                                        } else {
                                            input
                                        }
                                    }
                                }
                            }
                        }
                        inputValueChanged(text)
                    },
                    placeholder = {
                        Text(
                            text = BigDecimal.ZERO.toString(),
                            style = style.copy(color = MaterialTheme.colorScheme.primaryContainer)
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = shrunkFontSize,
                        fontWeight = style.fontWeight,
                        lineHeight = style.lineHeight,
                        textAlign = style.textAlign,
                        color = style.color
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = TextFieldDefaults.textFieldColors(
                        placeholderColor = MaterialTheme.colorScheme.error,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }
    } else {
        Row(modifier = modifier) {
            Text(
                text = currencyCode,
                modifier = Modifier.alignByBaseline(),
                style = TextStyle(
                    color = style.color,
                    fontSize = style.fontSize * 0.6
                )
            )
            Text(
                text = amount,
                modifier = Modifier.alignByBaseline(),
                style = TextStyle(
                    color = style.color,
                    fontSize = style.fontSize
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreviewCurrencyTextField() {
    CurrencyTextFieldSmall(
        amount = BigDecimal(123).toString(),
        currencyCode = "$",
        isEditable = false
    )
}
