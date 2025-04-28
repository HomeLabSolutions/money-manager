package com.d9tilov.android.budget.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import com.d9tilov.android.designsystem.BottomActionButton
import com.d9tilov.android.designsystem.CurrencyTextFieldExtraBig
import com.d9tilov.android.designsystem.MmTopAppBar

@Composable
fun BudgetRoute(
    viewModel: BudgetAmountViewModel = hiltViewModel(),
    clickBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BudgetScreen(
        uiState = uiState,
        onBudgetInputChanged = viewModel::changeBudgetAmount,
        onSave = {
            viewModel.saveBudgetAmount()
            clickBack()
        },
        onClickBack = clickBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    uiState: BudgetUiState,
    showInPrepopulate: Boolean = false,
    onBudgetInputChanged: (String) -> Unit,
    onSave: () -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            if (!showInPrepopulate) {
                MmTopAppBar(
                    titleRes = R.string.title_prepopulate_budget,
                    onNavigationClick = onClickBack,
                )
            }
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.budget_sum_title),
                modifier =
                    Modifier.padding(
                        start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large),
                        top = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large),
                    ),
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.secondary),
            )
            var text by rememberSaveable { mutableStateOf("") }
            CurrencyTextFieldExtraBig(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
                uiState.budgetSum,
                uiState.currencySymbol,
                true,
            ) { s ->
                text = s
                onBudgetInputChanged(s)
            }
            Spacer(modifier = Modifier.weight(1f))
            if (!showInPrepopulate) {
                BottomActionButton(
                    modifier =
                        Modifier
                            .navigationBarsPadding()
                            .imePadding(),
                    onClick = onSave,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewBudget() {
    BudgetScreen(uiState = BudgetUiState(), onBudgetInputChanged = {})
}
