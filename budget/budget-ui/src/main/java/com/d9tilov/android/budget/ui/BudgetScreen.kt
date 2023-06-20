package com.d9tilov.android.budget.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.budget_ui.R
import com.d9tilov.android.designsystem.CurrencyTextFieldExtraBig
import com.d9tilov.android.designsystem.FilledButton
import com.d9tilov.android.designsystem.MmTopAppBar

@Composable
fun BudgetRoute(viewModel: BudgetAmountViewModel = hiltViewModel(), clickBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BudgetScreen(
        uiState = uiState,
        onBudgetInputChanged = viewModel::changeBudgetAmount,
        onSave = {
            viewModel.saveBudgetAmount()
            clickBack.invoke()
        },
        onClickBack = clickBack
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    uiState: BudgetUiState,
    showInPrepopulate: Boolean = false,
    onBudgetInputChanged: (String) -> Unit,
    onSave: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            if (!showInPrepopulate)
                MmTopAppBar(
                    titleRes = R.string.title_prepopulate_budget,
                    onNavigationClick = onClickBack
                )

        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.budget_sum_title),
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_large),
                    top = dimensionResource(R.dimen.padding_large),
                ),
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.secondary)
            )
            var text by rememberSaveable { mutableStateOf("") }
            CurrencyTextFieldExtraBig(
                uiState.budgetSum,
                uiState.currencySymbol,
                true,
                Modifier.fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium),
                    )
            ) { s ->
                text = s
                onBudgetInputChanged.invoke(s)
            }
            Spacer(modifier = Modifier.weight(1f))
            if (!showInPrepopulate) {
                FilledButton(
                    onClick = onSave,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.save)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewBudget() {
    BudgetRoute {}
}
