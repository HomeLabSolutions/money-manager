package com.d9tilov.android.transaction.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.core.utils.MainPriceFieldParser.isInputValid
import com.d9tilov.android.designsystem.AutoSizeTextField
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.R
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.transaction.ui.vm.TransactionCreationViewModel
import com.d9tilov.android.transaction.ui.vm.TransactionUiState

@Composable
fun TransactionCreationRoute(
    viewModel: TransactionCreationViewModel = hiltViewModel(),
    clickBack: () -> Unit,
) {

    val state: TransactionUiState by viewModel.uiState.collectAsStateWithLifecycle()
    TransactionCreationScreen(
        uiState = state,
        onBackClicked = clickBack,
        onSumChanged = viewModel::updateAmount
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TransactionCreationScreen(
    uiState: TransactionUiState,
    onSumChanged: (String) -> Unit,
    onBackClicked: () -> Unit,
) {
    var amount by remember { mutableStateOf(TextFieldValue()) }
    amount = amount.copy(text = uiState.amount, selection = TextRange(uiState.amount.length))
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = com.d9tilov.android.transaction_ui.R.string.title_transaction,
            onNavigationClick = onBackClicked
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
        ) {
            Text(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_large),
                    end = dimensionResource(id = R.dimen.padding_large),
                    top = dimensionResource(id = R.dimen.padding_small),
                ),
                text = stringResource(id = com.d9tilov.android.transaction_ui.R.string.transaction_edit_sum_title),
                style = MaterialTheme.typography.labelLarge
            )
            Row(
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = uiState.currency,
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = dimensionResource(id = R.dimen.currency_sign_big_text_size).value.sp
                )
                AutoSizeTextField(
                    modifier = Modifier.alignByBaseline(),
                    inputValue = uiState.amount,
                    inputValueChanged = onSumChanged,
                    showError = { if (!isInputValid(uiState.amount)) showError() }
                )
            }
        }
    }
}
@Composable
fun showError() {
    Text(
        text = "Please, enter correct number",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultTransactionCreationPreview() {
    MoneyManagerTheme {
        TransactionCreationScreen(
            uiState = TransactionUiState.EMPTY.copy(amount = "42"),
            onBackClicked = { },
            onSumChanged = {}
        )
    }
}
