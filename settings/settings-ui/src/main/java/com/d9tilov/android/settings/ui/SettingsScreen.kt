package com.d9tilov.android.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
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
        onSave = viewModel::save,
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
    onClickBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            MmTopAppBar(
                titleRes = R.string.title_settings,
                onNavigationClick = onClickBack
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            uiState.subscriptionState?.let { subscriptionState ->
                SubscriptionLayout(
                    uiState = subscriptionState,
                    modifier = Modifier
                        .padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium))
                        .fillMaxWidth(),
                    onClick = onClickSubscription
                )
            }
            StartOfPeriodLayout(
                day = uiState.startPeriodDay,
                modifier = Modifier.fillMaxWidth().padding(
                    dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)
                ),
                onPeriodDateChanged = onPeriodDateChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartOfPeriodLayout(day: String, modifier: Modifier, onPeriodDateChanged: (String) -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.settings_start_period_day_title),
            style = MaterialTheme.typography.bodyLarge
        )
        Box(modifier = Modifier.width(64.dp)) {
            OutlinedTextField(
                value = day,
                modifier = Modifier.padding(start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
                onValueChange = onPeriodDateChanged
            )
        }
        Text(
            text = stringResource(R.string.settings_start_period_day_postfix),
            modifier = Modifier.padding(start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small))
        )
    }
}

@Composable
fun SubscriptionLayout(
    uiState: SubscriptionUiState,
    modifier: Modifier,
    onClick: () -> Unit
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
