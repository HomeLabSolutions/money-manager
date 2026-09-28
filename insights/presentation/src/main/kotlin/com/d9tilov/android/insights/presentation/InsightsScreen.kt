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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.d9tilov.android.designsystem.MmTopAppBar

@Composable
fun InsightsRoute(
    viewModel: InsightsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    val state by viewModel.state.collectAsState()
    InsightsScreen(
        onBackClick = onBackClick,
        state = state,
        onRetryClick = viewModel::generate,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
fun InsightsScreen(
    onBackClick: () -> Unit,
    state: InsightsUiState,
    onRetryClick: () -> Unit = {},
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    if (state is InsightsUiState.Error) {
        val message = stringResource(state.messageRes)
        val retry = stringResource(R.string.insights_retry)
        LaunchedEffect(state) {
            if (onShowSnackBar(message, retry)) onRetryClick()
        }
    }

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
            InsightCard(state)
            Spacer(Modifier.weight(1f))
            if (state is InsightsUiState.Data) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(onClick = onRetryClick) {
                        Text(text = stringResource(R.string.insights_another), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightCard(state: InsightsUiState) {
    val colors = MaterialTheme.colorScheme
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
            when (state) {
                InsightsUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is InsightsUiState.Data -> {
                    Text(
                        text = state.insight.ifEmpty { stringResource(R.string.insights_unavailable_message) },
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurface,
                    )
                }

                is InsightsUiState.Error -> {
                    Text(
                        text = stringResource(state.messageRes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurface,
                    )
                }
            }
        }
    }
}
