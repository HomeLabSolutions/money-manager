package com.d9tilov.android.insights.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.insights.domain.Insight
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.DateFormat
import java.util.Date

private const val INSIGHT_BUBBLE_WIDTH_FRACTION = 0.92f

@Composable
fun InsightsRoute(
    viewModel: InsightsViewModel = hiltViewModel(),
    consentViewModel: InsightsConsentViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
    val consentSaveFailed = stringResource(R.string.insights_consent_save_failed)
    var showConsentDialog by remember { mutableStateOf(false) }
    var isSavingConsent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val granted = consentViewModel.isConsentGranted()
        showConsentDialog = !granted
        if (granted) requestNotificationPermission(context, notificationPermissionLauncher)
    }

    val generate: () -> Unit = {
        scope.launch {
            if (consentViewModel.isConsentGranted()) {
                viewModel.generate()
            } else {
                showConsentDialog = true
            }
        }
    }

    InsightsScreen(
        onBackClick = onBackClick,
        state = state,
        onGenerateClick = generate,
        onShowSnackBar = onShowSnackBar,
    )
    InsightsConsentDialog(
        visible = showConsentDialog,
        isSaving = isSavingConsent,
        onConfirm = {
            scope.launch {
                isSavingConsent = true
                try {
                    consentViewModel.grantConsent()
                    showConsentDialog = false
                    requestNotificationPermission(context, notificationPermissionLauncher)
                    viewModel.generate()
                } catch (_: IOException) {
                    onShowSnackBar(consentSaveFailed, null)
                } catch (_: IllegalArgumentException) {
                    onShowSnackBar(consentSaveFailed, null)
                } finally {
                    isSavingConsent = false
                }
            }
        },
        onDismiss = { showConsentDialog = false },
    )
}

private fun requestNotificationPermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    ) {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun InsightsScreen(
    onBackClick: () -> Unit,
    state: InsightsUiState,
    onGenerateClick: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    InsightErrorSnackBar(state, onGenerateClick, onShowSnackBar)

    val colors = MaterialTheme.colorScheme
    val gradient = Brush.linearGradient(listOf(colors.primaryContainer, colors.secondaryContainer, colors.surface))
    val isGenerating = state is InsightsUiState.Data && state.isGenerating

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
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
        ) {
            InsightHistoryContent(state = state, modifier = Modifier.weight(1f).fillMaxWidth())
            Box(
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (state == InsightsUiState.Loading || isGenerating) {
                    CircularProgressIndicator()
                } else {
                    Button(onClick = onGenerateClick) {
                        Text(stringResource(R.string.insights_generate))
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightErrorSnackBar(
    state: InsightsUiState,
    onGenerateClick: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
) {
    if (state is InsightsUiState.Error) {
        val message = stringResource(state.messageRes)
        val retry =
            if (state.messageRes == R.string.insights_daily_limit) null else stringResource(R.string.insights_retry)
        LaunchedEffect(state.messageRes) {
            if (onShowSnackBar(message, retry) && retry != null) onGenerateClick()
        }
    }
}

@Composable
private fun InsightHistoryContent(
    state: InsightsUiState,
    modifier: Modifier,
) {
    val insights =
        when (state) {
            is InsightsUiState.Data -> state.insights
            is InsightsUiState.Error -> state.insights
            InsightsUiState.Loading -> emptyList()
        }
    val listState = rememberLazyListState()
    LaunchedEffect(insights.lastOrNull()?.id) {
        if (insights.isNotEmpty()) {
            withFrameNanos { }
            listState.animateScrollToItem(insights.lastIndex)
        }
    }
    when {
        state == InsightsUiState.Loading -> {
            Box(modifier)
        }

        insights.isEmpty() -> {
            Box(modifier, contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.insights_empty_history),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier,
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                items(insights, key = Insight::id) { insight -> InsightBubble(insight) }
            }
        }
    }
}

@Composable
private fun InsightBubble(insight: Insight) {
    val colors = MaterialTheme.colorScheme
    val date =
        remember(insight.createdAtMillis) {
            DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(insight.createdAtMillis))
        }
    Surface(
        modifier = Modifier.fillMaxWidth(INSIGHT_BUBBLE_WIDTH_FRACTION),
        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 22.dp, bottomEnd = 22.dp, bottomStart = 22.dp),
        color = colors.surface.copy(alpha = 0.94f),
        tonalElevation = 3.dp,
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = insight.text, style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
            Text(text = date, style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant)
        }
    }
}
