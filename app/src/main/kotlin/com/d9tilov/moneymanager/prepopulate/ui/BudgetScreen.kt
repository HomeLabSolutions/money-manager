package com.d9tilov.moneymanager.prepopulate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.budget.vm.BudgetAmountViewModel2
import com.d9tilov.moneymanager.budget.vm.BudgetUiState
import com.d9tilov.moneymanager.core.ui.views.CurrencyTextFieldExtraBig
import com.d9tilov.moneymanager.designsystem.MoneyManagerFilledButton

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun BudgetRoute(viewModel: BudgetAmountViewModel2 = hiltViewModel(), clickBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BudgetScreen2(
        budgetUiState = uiState,
        showButton = true,
        onBudgetInputChanged = viewModel::changeBudgetAmount,
        onBudgetSave = {
            viewModel.saveBudgetAmount()
            clickBack.invoke()
        },
        onClickBack = clickBack
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BudgetScreen2(
    budgetUiState: BudgetUiState,
    modifier: Modifier = Modifier,
    showButton: Boolean = false,
    onBudgetInputChanged: (String) -> Unit,
    onBudgetSave: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    Column {
        Text(
            text = stringResource(R.string.transaction_edit_sum_title),
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.standard_large_padding),
                top = dimensionResource(R.dimen.standard_large_padding),
            ),
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.secondary)
        )
        var text by rememberSaveable { mutableStateOf("") }
        CurrencyTextFieldExtraBig(
            budgetUiState.budgetSum,
            budgetUiState.currencySymbol,
            true,
            Modifier.fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.standard_padding),
                )
        ) { s ->
            text = s
            onBudgetInputChanged.invoke(s)
        }
        Spacer(modifier = Modifier.weight(1f))
        if (showButton) {
            MoneyManagerFilledButton(
                onClick = onBudgetSave,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.standard_padding))
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BudgetScreen(
    budgetUiState: BudgetUiState,
    modifier: Modifier = Modifier,
    showButton: Boolean = false,
    onBudgetInputChanged: (String) -> Unit,
    onBudgetSave: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    Column {
        Text(
            text = stringResource(R.string.transaction_edit_sum_title),
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.standard_large_padding),
                top = dimensionResource(R.dimen.standard_large_padding),
            ),
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.secondary)
        )
        var text by rememberSaveable { mutableStateOf(budgetUiState.budgetSum) }
        CurrencyTextFieldExtraBig(
            budgetUiState.budgetSum,
            budgetUiState.currencySymbol,
            true,
            Modifier.fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.standard_padding),
                )
        ) { s ->
            text = s
            onBudgetInputChanged.invoke(s)
        }
        Spacer(modifier = Modifier.weight(1f))
        if (showButton) {
            MoneyManagerFilledButton(
                onClick = onBudgetSave,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.standard_padding))
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewBudget() {
    BudgetRoute {}
}
