package com.d9tilov.android.designsystem

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d9tilov.android.core.constants.CurrencyConstants

@Composable
fun ComposeCurrencyView(
    modifier: Modifier = Modifier,
    symbol: String,
    symbolColor: Color = MaterialTheme.colorScheme.primary,
    symbolStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.primary,
    valueStyle: TextStyle = MaterialTheme.typography.headlineLarge,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            modifier =
                Modifier
                    .padding(bottom = 2.dp, end = dimensionResource(id = (R.dimen.padding_extra_small))),
            text = symbol,
            color = symbolColor,
            style = symbolStyle,
            maxLines = 1,
        )

        AnimatedContent(
            targetState = value,
            transitionSpec = {
                fadeIn(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                    .togetherWith(fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)))
            },
            label = "currency_value_animation",
        ) { animatedValue ->
            Text(
                text = animatedValue,
                color = valueColor,
                style = valueStyle,
                maxLines = 1,
            )
        }
    }
}

@Composable
@Preview
fun ComposeCurrencyViewPreview() {
    MaterialTheme {
        ComposeCurrencyView(
            symbol = CurrencyConstants.DEFAULT_CURRENCY_SYMBOL,
            value = "42",
        )
    }
}
