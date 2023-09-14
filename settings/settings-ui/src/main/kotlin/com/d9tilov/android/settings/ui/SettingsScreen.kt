package com.d9tilov.android.settings.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.SaveButton
import com.d9tilov.android.settings.ui.vm.BackupState
import com.d9tilov.android.settings.ui.vm.SettingsUiState
import com.d9tilov.android.settings.ui.vm.SettingsViewModel
import com.d9tilov.android.settings.ui.vm.SubscriptionUiState
import com.d9tilov.android.settings_ui.R

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel(), clickBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        onPeriodDateChanged = viewModel::changeFiscalDay,
        onSave = {
            viewModel.save()
            clickBack.invoke()
        },
        onBackupClick = viewModel::backup,
        onClearBackupClick = viewModel::backup,
        onClickBack = clickBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    modifier: Modifier = Modifier,
    onPeriodDateChanged: (String) -> Unit,
    onSave: () -> Unit = {},
    onClickSubscription: () -> Unit = {},
    onBackupClick: () -> Unit = {},
    onClearBackupClick: () -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MmTopAppBar(
                titleRes = R.string.title_settings,
                onNavigationClick = onClickBack
            )
        }
    ) { padding ->
        Column(modifier = modifier) {
            uiState.subscriptionState?.let { subscriptionState ->
                SubscriptionLayout(
                    uiState = subscriptionState,
                    modifier = Modifier
                        .padding(padding)
                        .padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium))
                        .fillMaxWidth(),
                    onClick = onClickSubscription
                )
            }
            StartOfPeriodLayout(
                day = uiState.startPeriodDay,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
                onPeriodDateChanged = onPeriodDateChanged
            )
            BackupLayout(
                backupState = uiState.backupState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
                onBackupClick = onBackupClick,
                onClearBackupClick = onClearBackupClick,
            )
            Spacer(modifier = Modifier.weight(1f))
            SaveButton(onClick = onSave)
        }
    }
}

@Composable
fun StartOfPeriodLayout(day: String, modifier: Modifier, onPeriodDateChanged: (String) -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.settings_start_period_day_title),
            style = MaterialTheme.typography.titleMedium
        )
        Box(modifier = Modifier.width(64.dp)) {
            OutlinedTextField(
                value = day,
                modifier = Modifier.padding(start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
                maxLines = 1,
                onValueChange = { text: String ->
                    if (isInputDateValid(text)) onPeriodDateChanged.invoke(text)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Text(
            text = stringResource(R.string.settings_start_period_day_postfix),
            modifier = Modifier.padding(start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun BackupLayout(
    backupState: BackupState,
    modifier: Modifier,
    onBackupClick: () -> Unit,
    onClearBackupClick: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    Row(
        modifier = modifier
            .padding(top = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(2f)) {
            Text(
                text = stringResource(R.string.settings_backup),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(
                    id = R.string.settings_backup_info,
                    backupState.lastBackupTimestamp
                ),
                style = MaterialTheme.typography.labelSmall
            )
        }
        IconButton(
            modifier = Modifier.weight(2f),
            enabled = !backupState.backupLoading,
            onClick = onBackupClick
        ) {
            Icon(
                modifier = if (backupState.backupLoading) Modifier.rotate(angle) else Modifier,
                painter = painterResource(id = MoneyManagerIcons.Backup),
                contentDescription = "Backup"
            )
        }
        IconButton(
            modifier = Modifier.weight(1f),
            onClick = onClearBackupClick
        ) {
            Icon(
                imageVector = MoneyManagerIcons.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

private fun isInputDateValid(input: String): Boolean {
    if (input.isEmpty()) return false
    return try {
        val num = input.toInt()
        num in 1..31
    } catch (ex: NumberFormatException) {
        false
    }
}

@Composable
fun SubscriptionLayout(
    uiState: SubscriptionUiState,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(dimensionResource(com.d9tilov.android.designsystem.R.dimen.card_view_corner_radius)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
                painter = painterResource(id = MoneyManagerIcons.Subscription),
                contentDescription = "Subscription Icon"
            )
            Column(
                modifier = Modifier.padding(
                    start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large),
                    top = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)
                )
            ) {
                SubscriptionTitle(stringResource(uiState.title))
                SubscriptionDescription(stringResource(uiState.description))
            }
        }
    }
}

@Composable
fun SubscriptionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium.copy(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = dimensionResource(R.dimen.settings_subscription_title_text_size).value.sp
        )
    )
}

@Composable
fun SubscriptionDescription(subtitle: String) {
    Text(
        text = subtitle,
        modifier = Modifier.padding(top = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultSubscriptionPreview() {
    SettingsScreen(
        SettingsUiState(SubscriptionUiState(), startPeriodDay = 30.toString()),
        onPeriodDateChanged = {}
    )
}
