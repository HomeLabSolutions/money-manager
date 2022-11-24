package com.d9tilov.moneymanager.prepopulate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.budget.vm.BudgetUiState
import com.d9tilov.moneymanager.core.ui.views.CurrencyTextFieldExtraBig
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BudgetScreen(
    budgetUiState: BudgetUiState,
    modifier: Modifier,
    onBudgetInputChanged: (String) -> Unit
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
        var text by rememberSaveable { mutableStateOf(budgetUiState.budgetData.sum.toString()) }

        CurrencyTextFieldExtraBig(
            text,
            budgetUiState.budgetData.currencyCode.getSymbolByCode(),
            true,
            Modifier.fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.standard_padding),
                )
        ) { s ->
            text = s
            onBudgetInputChanged.invoke(s)
        }

    }
    Spacer(modifier = modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewBudget() {
    BudgetScreen(BudgetUiState(BudgetData.EMPTY), Modifier.fillMaxSize()) {}
}

