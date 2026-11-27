package com.d9tilov.android.designsystem

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.MainPriceFieldParser

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

@Composable
fun CurrencyTextFieldExtraSmall(
    modifier: Modifier = Modifier,
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    style: TextStyle,
    inputValueChanged: (String) -> Unit = {},
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged,
    )
}

@Composable
fun CurrencyTextFieldSmall(
    modifier: Modifier = Modifier,
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    style: TextStyle =
        MaterialTheme.typography.titleMedium
            .copy(
                color = MaterialTheme.colorScheme.primary,
            ),
    inputValueChanged: (String) -> Unit = {},
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged,
    )
}

@Composable
fun CurrencyTextFieldMedium(
    modifier: Modifier = Modifier,
    value: String,
    currencyCode: String,
    isEditable: Boolean = false,
    style: TextStyle =
        MaterialTheme.typography.titleLarge
            .copy(
                color = MaterialTheme.colorScheme.primary,
            ),
    inputValueChanged: (String) -> Unit = {},
) {
    CurrencyTextField(
        amount = value,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged,
    )
}

@Composable
fun CurrencyTextFieldBig(
    modifier: Modifier = Modifier,
    value: String,
    currencyCode: String,
    isEditable: Boolean = false,
    style: TextStyle =
        MaterialTheme.typography.headlineMedium
            .copy(
                color = MaterialTheme.colorScheme.primary,
            ),
    inputValueChanged: (String) -> Unit = {},
) {
    CurrencyTextField(
        amount = value,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged,
    )
}

@Composable
fun CurrencyTextFieldExtraBig(
    modifier: Modifier = Modifier,
    amount: String,
    currencyCode: String,
    isEditable: Boolean = false,
    style: TextStyle =
        MaterialTheme.typography.headlineLarge
            .copy(
                color = MaterialTheme.colorScheme.primary,
            ),
    inputValueChanged: (String) -> Unit = {},
) {
    CurrencyTextField(
        amount = amount,
        currencyCode = currencyCode,
        isEditable = isEditable,
        modifier = modifier,
        style = style,
        inputValueChanged = inputValueChanged,
    )
}

@Composable
private fun CurrencyTextField(
    amount: String,
    currencyCode: String,
    isEditable: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle,
    inputValueChanged: (String) -> Unit,
) {
    val symbol = currencyCode.getSymbolByCode()
    val currency = Currency.fromCode(currencyCode)
    if (isEditable && currency != null) {
        BoxWithConstraints(modifier = modifier.fillMaxWidth().clipToBounds()) {
            var shrunkFontSize = style.fontSize
            val calculateIntrinsics = @Composable {
                ParagraphIntrinsics(
                    text = symbol + amount,
                    style =
                        TextStyle(
                            fontSize = shrunkFontSize,
                            fontWeight = style.fontWeight,
                            lineHeight = style.lineHeight,
                            textAlign = style.textAlign,
                        ),
                    density = LocalDensity.current,
                    fontFamilyResolver = createFontFamilyResolver(LocalContext.current),
                )
            }

            var intrinsics = calculateIntrinsics()
            val maxInputWidth =
                LocalDensity.current.run {
                    val textFieldDefaultHorizontalPadding =
                        dimensionResource(
                            R.dimen.padding_medium,
                        ).toPx()
                    maxWidth.toPx() - 2 * textFieldDefaultHorizontalPadding
                }
            while (intrinsics.maxIntrinsicWidth > maxInputWidth) {
                shrunkFontSize *= TEXT_SCALE_REDUCTION_INTERVAL
                intrinsics = calculateIntrinsics()
            }
            Row(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = symbol,
                    fontSize = shrunkFontSize * 0.6,
                    style = style,
                    modifier =
                        Modifier
                            .alignByBaseline()
                            .offset(
                                x = dimensionResource(R.dimen.currency_symbol_offset),
                            ),
                )
                TextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .alignByBaseline(),
                    value = amount,
                    onValueChange = { input ->
                        if (MainPriceFieldParser.allowsAmountInput(input, currency.precision)) {
                            inputValueChanged(input.replace(',', '.'))
                        }
                    },
                    placeholder = {
                        Text(
                            text = "0",
                            style = style.copy(color = MaterialTheme.colorScheme.primaryContainer),
                        )
                    },
                    textStyle =
                        TextStyle(
                            fontSize = shrunkFontSize,
                            fontWeight = style.fontWeight,
                            lineHeight = style.lineHeight,
                            textAlign = style.textAlign,
                            color = style.color,
                        ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors =
                        TextFieldDefaults.colors().copy(
                            focusedPlaceholderColor = MaterialTheme.colorScheme.error,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                    singleLine = true,
                )
            }
        }
    } else {
        Row(modifier = modifier) {
            Text(
                text = symbol,
                modifier = Modifier.alignByBaseline(),
                style =
                    TextStyle(
                        color = style.color,
                        fontSize = style.fontSize * 0.6,
                    ),
            )
            Text(
                text = amount,
                modifier = Modifier.alignByBaseline(),
                style =
                    TextStyle(
                        color = style.color,
                        fontSize = style.fontSize,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewCurrencyTextField() {
    CurrencyTextFieldSmall(
        amount = "123",
        currencyCode = Currency.USD.name,
        isEditable = false,
    )
}
