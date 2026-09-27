package com.d9tilov.android.insights.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d9tilov.android.designsystem.MmTopAppBar

@Composable
fun InsightsScreen(
    onBackClick: () -> Unit,
    insight: InsightUiModel? = null,
    onAnotherClick: () -> Unit = {},
) {
    val colors = MaterialTheme.colorScheme
    val gradient =
        Brush.linearGradient(
            listOf(
                colors.primaryContainer,
                colors.secondaryContainer,
                colors.surface,
            ),
        )

    Scaffold(
        modifier = Modifier.fillMaxSize().background(gradient),
        containerColor = Color.Transparent,
        topBar = {
            MmTopAppBar(
                titleRes = R.string.insights_screen_title,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                surfaceColor = Color.Transparent,
                shadowElevation = 0.dp,
                onNavigationClick = onBackClick,
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Spacer(Modifier.weight(1f))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = colors.surface.copy(alpha = 0.92f),
                tonalElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = insight?.title ?: stringResource(R.string.insights_unavailable_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.onSurface,
                    )
                    Text(
                        text = insight?.text ?: stringResource(R.string.insights_unavailable_message),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant,
                    )
                    if (insight != null) {
                        Text(
                            text = insight.period,
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.primary,
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = onAnotherClick,
                    enabled = insight != null,
                ) {
                    Text(
                        text = stringResource(R.string.insights_another),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
