package com.d9tilov.android.insights.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.d9tilov.android.common.android.ui.permissions.rememberNotificationPermissionRequester
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.insights.domain.model.Insight
import com.d9tilov.android.insights.domain.model.InsightGenerationStatus
import com.d9tilov.android.insights.domain.model.InsightsConstants.MIN_INSIGHT_TRANSACTION_COUNT
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

private const val INSIGHT_BUBBLE_WIDTH_FRACTION = 0.92f
private const val INSIGHT_BUBBLE_SURFACE_ALPHA = 0.94f

@Composable
fun InsightsRoute(
    viewModel: InsightsViewModel = hiltViewModel(),
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val requestNotificationPermission = rememberNotificationPermissionRequester()
    var showConsentDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val granted = viewModel.isConsentGranted()
        showConsentDialog = !granted
        if (granted) requestNotificationPermission()
    }

    InsightsScreen(
        state = state,
        onGenerateClick = {
            if (viewModel.isConsentGranted()) {
                viewModel.generate()
            } else {
                showConsentDialog = true
            }
        },
        onShowSnackBar = onShowSnackBar,
        onErrorDismissed = viewModel::dismissSnackbarError,
    )
    InsightsConsentDialog(
        visible = showConsentDialog,
        onConfirm = {
            scope.launch {
                viewModel.grantConsent()
                showConsentDialog = false
                requestNotificationPermission()
            }
        },
        onDismiss = { showConsentDialog = false },
    )
}

@Composable
fun InsightsScreen(
    state: InsightsUiState,
    onGenerateClick: suspend () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    onErrorDismissed: () -> Unit = {},
) {
    val errorState = state.errorState
    if (errorState != null) {
        InsightErrorSnackBar(
            error = errorState,
            onShowSnackBar = onShowSnackBar,
            onSnackbarResult = { retry ->
                if (retry) onGenerateClick() else onErrorDismissed()
            },
        )
    }

    val colors = MaterialTheme.colorScheme
    val gradient = Brush.linearGradient(listOf(colors.primaryContainer, colors.secondaryContainer, colors.surface))

    Scaffold(
        modifier = Modifier.fillMaxSize().background(gradient),
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
        ) {
            val contentModifier = Modifier.weight(1f).fillMaxWidth()
            InsightHistoryContent(state, contentModifier)
            InsightGenerationAction(state.generationStatus, onGenerateClick)
        }
    }
}

@Composable
private fun InsightGenerationAction(
    status: InsightGenerationStatus,
    onGenerateClick: suspend () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(R.dimen.insights_action_height))
                .padding(bottom = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
        contentAlignment = Alignment.Center,
    ) {
        when (status) {
            InsightGenerationStatus.AVAILABLE -> {
                Button(onClick = { scope.launch { onGenerateClick() } }) {
                    Text(stringResource(R.string.insights_generate))
                }
            }

            InsightGenerationStatus.INSUFFICIENT_DATA -> {
                InsightGenerationNotice(
                    stringResource(R.string.insights_insufficient_data, MIN_INSIGHT_TRANSACTION_COUNT),
                )
            }

            InsightGenerationStatus.DAILY_LIMIT_REACHED -> {
                InsightGenerationNotice(stringResource(R.string.insights_daily_limit))
            }

            InsightGenerationStatus.UNDEFINED, InsightGenerationStatus.LOADING -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun InsightGenerationNotice(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun InsightErrorSnackBar(
    error: InsightsErrorState,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    onSnackbarResult: suspend (retry: Boolean) -> Unit,
) {
    val message =
        error.messageArg?.let { stringResource(error.messageRes, it) }
            ?: stringResource(error.messageRes)
    val actionLabel = if (error.canRetry) stringResource(R.string.insights_retry) else null
    LaunchedEffect(error) {
        onSnackbarResult(onShowSnackBar(message, actionLabel))
    }
}

@Composable
private fun InsightHistoryContent(
    state: InsightsUiState,
    modifier: Modifier,
) {
    if (state.isHistoryLoading) {
        Box(modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        InsightHistoryList(state.insights, modifier)
    }
}

@Composable
private fun InsightHistoryList(
    insights: List<Insight>,
    modifier: Modifier,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(insights.lastOrNull()?.id) {
        if (insights.isNotEmpty()) {
            listState.animateScrollToItem(insights.lastIndex)
        }
    }
    if (insights.isEmpty()) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.insights_empty_history),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.insight_bubble_spacing)),
            contentPadding =
                PaddingValues(vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
        ) {
            items(insights, key = Insight::id) { insight -> InsightBubble(insight) }
        }
    }
}

@Composable
private fun InsightBubble(insight: Insight) {
    val colors = MaterialTheme.colorScheme
    val cornerRadius = dimensionResource(R.dimen.insight_bubble_corner_radius)
    val date =
        remember(insight.createdAtMillis) {
            DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(insight.createdAtMillis))
        }
    Surface(
        modifier = Modifier.fillMaxWidth(INSIGHT_BUBBLE_WIDTH_FRACTION),
        shape =
            RoundedCornerShape(
                topStart = dimensionResource(R.dimen.insight_bubble_small_corner_radius),
                topEnd = cornerRadius,
                bottomEnd = cornerRadius,
                bottomStart = cornerRadius,
            ),
        color = colors.surface.copy(alpha = INSIGHT_BUBBLE_SURFACE_ALPHA),
        tonalElevation = dimensionResource(com.d9tilov.android.designsystem.R.dimen.surface_elevation),
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
            verticalArrangement =
                Arrangement.spacedBy(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
        ) {
            Text(text = insight.text, style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
            Text(text = date, style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant)
        }
    }
}

class InsightsHistoryPreviewProvider : PreviewParameterProvider<List<Insight>> {
    override val values: Sequence<List<Insight>> =
        sequenceOf(
            listOf(
                Insight(
                    id = 1L,
                    createdAtMillis = 1_790_856_000_000L,
                    text = "Food expenses rose from $240 to $310 over the last two weeks, an increase of 29%.",
                ),
                Insight(
                    id = 2L,
                    createdAtMillis = 1_791_460_800_000L,
                    text = "Transport expenses fell by $45 compared with the preceding two weeks.",
                ),
            ),
            emptyList(),
        )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InsightsScreenPreview(
    @PreviewParameter(InsightsHistoryPreviewProvider::class) insights: List<Insight>,
) {
    MoneyManagerTheme(dynamicColor = false) {
        InsightsScreen(
            state =
                InsightsUiState(
                    insights = insights,
                    isHistoryLoading = false,
                    generationStatus = InsightGenerationStatus.AVAILABLE,
                ),
            onGenerateClick = {},
            onShowSnackBar = { _, _ -> false },
        )
    }
}

@Preview(name = "Loading light", showBackground = true)
@Preview(name = "Loading dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InsightsScreenLoadingPreview(
    @PreviewParameter(InsightsHistoryPreviewProvider::class) insights: List<Insight>,
) {
    MoneyManagerTheme(dynamicColor = false) {
        InsightsScreen(
            state =
                InsightsUiState(
                    insights = insights,
                    isHistoryLoading = false,
                    generationStatus = InsightGenerationStatus.LOADING,
                ),
            onGenerateClick = {},
            onShowSnackBar = { _, _ -> false },
        )
    }
}

@Preview(name = "History loading", showBackground = true)
@Composable
fun InsightsScreenHistoryLoadingPreview() {
    MoneyManagerTheme(dynamicColor = false) {
        InsightsScreen(
            state = InsightsUiState(),
            onGenerateClick = {},
            onShowSnackBar = { _, _ -> false },
        )
    }
}

class InsightGenerationBlockedPreviewProvider : PreviewParameterProvider<InsightGenerationStatus> {
    override val values =
        sequenceOf(InsightGenerationStatus.INSUFFICIENT_DATA, InsightGenerationStatus.DAILY_LIMIT_REACHED)
}

@Preview(name = "Generation unavailable light", showBackground = true)
@Preview(name = "Generation unavailable dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InsightsScreenGenerationBlockedPreview(
    @PreviewParameter(InsightGenerationBlockedPreviewProvider::class) status: InsightGenerationStatus,
) {
    MoneyManagerTheme(dynamicColor = false) {
        InsightsScreen(
            state = InsightsUiState(isHistoryLoading = false, generationStatus = status),
            onGenerateClick = {},
            onShowSnackBar = { _, _ -> false },
        )
    }
}
