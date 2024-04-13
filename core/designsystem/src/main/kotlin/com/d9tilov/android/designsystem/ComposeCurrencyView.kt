package com.d9tilov.android.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.d9tilov.android.core.constants.CurrencyConstants

@Composable
fun ComposeCurrencyView(
    modifier: Modifier = Modifier,
    symbol: String,
    symbolColor: Color = MaterialTheme.colorScheme.primary,
    symbolSize: TextUnit = 18.sp,
    symbolStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.primary,
    valueSize: TextUnit = 34.sp,
    valueStyle: TextStyle = MaterialTheme.typography.headlineLarge,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .alignByBaseline()
                .padding(end = dimensionResource(id = (R.dimen.padding_extra_small))),
            text = symbol,
            color = symbolColor,
            fontSize = symbolSize,
            style = symbolStyle,
            maxLines = 1
        )
        Text(
            modifier = Modifier.alignByBaseline(),
            text = value,
            color = valueColor,
            fontSize = valueSize,
            style = valueStyle,
            maxLines = 1
        )
    }
}

@Composable
@Preview
fun ComposeCurrencyViewPreview() {
    MaterialTheme {
        ComposeCurrencyView(
            symbol = CurrencyConstants.DEFAULT_CURRENCY_SYMBOL,
            value = "42"
        )
    }
}
