package com.d9tilov.android.insights.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.insights.domain.InsightsConstants.INSIGHT_WINDOW_DAYS

@Composable
fun InsightsConsentDialog(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    SimpleDialog(
        show = visible,
        title = stringResource(R.string.insights_screen_title),
        subtitle = stringResource(R.string.insights_consent_message, INSIGHT_WINDOW_DAYS),
        confirmButton = stringResource(R.string.insights_consent_confirm),
        dismissButton = stringResource(R.string.insights_consent_dismiss),
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}
