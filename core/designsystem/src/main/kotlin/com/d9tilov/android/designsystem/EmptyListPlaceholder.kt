package com.d9tilov.android.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmptyListPlaceholder(
    modifier: Modifier,
    icon: Painter,
    title: String,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = icon,
            contentDescription = "No transactions",
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
        subtitle?.let { str ->
            Text(
                text = str,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyListPlaceholderPreview() {
    EmptyListPlaceholder(
        modifier = Modifier.fillMaxSize(),
        icon = painterResource(id = MoneyManagerIcons.EmptyStatisticsPlaceholder),
        title = "No data to display long long long long long text",
    )
}
