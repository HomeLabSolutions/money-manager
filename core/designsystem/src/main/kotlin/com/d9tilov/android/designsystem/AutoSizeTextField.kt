package com.d9tilov.android.designsystem

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.8f
private const val MAX_LENGTH = 13

@ExperimentalMaterialApi
@Composable
fun AutoSizeTextField(
    inputValue: String,
    inputValueChanged: (String) -> Unit,
    showError: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 72.sp,
) {
    var amount by remember { mutableStateOf(TextFieldValue()) }
    amount = amount.copy(text = inputValue, selection = TextRange(inputValue.length))
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        var shrunkFontSize = fontSize
        val calculateIntrinsics = @Composable {
            ParagraphIntrinsics(
                text = inputValue,
                style = TextStyle(fontSize = shrunkFontSize),
                density = LocalDensity.current,
                fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
            )
        }

        var intrinsics = calculateIntrinsics()
        with(LocalDensity.current) {
            // TextField and OutlinedText field have default horizontal padding of 16.dp
            val textFieldDefaultHorizontalPadding = 16.dp.toPx()
            val maxInputWidth = maxWidth.toPx() - 2 * textFieldDefaultHorizontalPadding

            while (intrinsics.maxIntrinsicWidth > maxInputWidth) {
                shrunkFontSize *= TEXT_SCALE_REDUCTION_INTERVAL
                intrinsics = calculateIntrinsics()
            }
        }
        val focusRequester = remember { FocusRequester() }
        OutlinedTextField(
            value = amount,
            modifier = Modifier
                .fillMaxWidth()
                .width(IntrinsicSize.Min)
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_extra_small),
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
            textStyle = TextStyle.Default.copy(fontSize = shrunkFontSize),
            singleLine = true,
            onValueChange = { text -> if (text.text.length <= MAX_LENGTH) inputValueChanged(text.text) },
            supportingText = { showError.invoke() }
        )
        LaunchedEffect(Unit) { focusRequester.requestFocus() }
    }
}
