package com.d9tilov.android.transaction.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.CategoryDestination
import com.d9tilov.android.common.android.utils.TRANSACTION_DATE_FORMAT
import com.d9tilov.android.common.android.utils.formatDate
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.MainPriceFieldParser.isInputValid
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.designsystem.AutoSizeTextField
import com.d9tilov.android.designsystem.BottomActionButton
import com.d9tilov.android.designsystem.CheckboxWithLabel
import com.d9tilov.android.designsystem.DottedDivider
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.vm.TransactionCreationViewModel
import com.d9tilov.android.transaction.ui.vm.TransactionUiState
import com.d9tilov.android.transaction_ui.R
import java.math.BigDecimal
import java.util.Locale

@Composable
fun TransactionCreationRoute(
    viewModel: TransactionCreationViewModel = hiltViewModel(),
    clickBack: () -> Unit,
    clickCurrency: (String) -> Unit,
    clickCategory: (TransactionType, CategoryDestination) -> Unit,
) {

    val state: TransactionUiState by viewModel.uiState.collectAsStateWithLifecycle()
    TransactionCreationScreen(
        uiState = state,
        onBackClicked = clickBack,
        onSumChanged = viewModel::updateAmount,
        onSaveClicked = {
            viewModel.save()
            clickBack()
        },
        onInStatisticsChanged = viewModel::updateInStatistics,
        onDescriptionChanged = viewModel::updateDescription,
        onCurrencyClicked = clickCurrency,
        onCategoryClicked = clickCategory,
        onDateClicked = viewModel::updateDate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCreationScreen(
    uiState: TransactionUiState,
    onSumChanged: (String) -> Unit,
    onInStatisticsChanged: (Boolean) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onBackClicked: () -> Unit,
    onCurrencyClicked: (String) -> Unit,
    onCategoryClicked: (TransactionType, CategoryDestination) -> Unit,
    onDateClicked: (Long) -> Unit,
    onSaveClicked: () -> Unit,
) {
    val context = LocalContext.current
    var showError by remember { mutableStateOf(false) }
    val showDatePickerDialog = remember { mutableStateOf(false) }
    val datePickerState: DatePickerState = rememberDatePickerState().also { it.selectedDateMillis = uiState.transaction.date.toMillis() }
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = R.string.title_transaction,
            onNavigationClick = onBackClicked
        )
    }) { padding ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = padding.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(
                        start = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        end = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        top = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small),
                    ),
                    text = stringResource(id = R.string.transaction_edit_sum_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .alignByBaseline()
                            .clickable(onClick = { onCurrencyClicked(uiState.transaction.currencyCode) }),
                        text = uiState.transaction.currencyCode.getSymbolByCode(),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.currency_sign_big_text_size).value.sp
                    )
                    AutoSizeTextField(
                        modifier = Modifier.alignByBaseline(),
                        inputValue = uiState.amount,
                        inputValueChanged = { text ->
                            showError = !isInputValid(text)
                            onSumChanged(text)
                        },
                        showError = { if (showError) ShowError() }
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            onCategoryClicked(
                                uiState.transaction.type,
                                CategoryDestination.EDIT_TRANSACTION_SCREEN
                            )
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(dimensionResource(id = com.d9tilov.android.common.android.R.dimen.category_creation_icon_size)),
                            imageVector = ImageVector.vectorResource(id = uiState.transaction.category.icon),
                            contentDescription = "Category",
                            tint = Color(
                                ContextCompat.getColor(
                                    context,
                                    uiState.transaction.category.color
                                )
                            )
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small)),
                            text = uiState.transaction.category.name,
                            color = Color(
                                ContextCompat.getColor(
                                    context,
                                    uiState.transaction.category.color
                                )
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small))
                            .clickable { showDatePickerDialog.value = true },
                        text = formatDate(uiState.transaction.date, TRANSACTION_DATE_FORMAT),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                CheckboxWithLabel(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_medium),
                        vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large)
                    ),
                    value = uiState.transaction.inStatistics,
                    label = stringResource(id = R.string.transaction_edit_in_statistics),
                    onCheckChanged = { onInStatisticsChanged(it) }
                )
                DottedDivider(
                    modifier = Modifier.padding(
                        start = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        end = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        top = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large)
                    )
                )
                TextField(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small)
                    ),
                    value = uiState.transaction.description,
                    onValueChange = onDescriptionChanged,
                    colors = TextFieldDefaults.colors().copy(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.tertiary,
                        cursorColor = MaterialTheme.colorScheme.tertiary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.transaction_edit_description_hint),
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                        )
                    }
                )
            }
            BottomActionButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                onClick = onSaveClicked,
                enabled = !showError
            )
            if (showDatePickerDialog.value) {
                DatePickerDialog(
                    onDismissRequest = { showDatePickerDialog.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDateClicked(datePickerState.selectedDateMillis ?: currentDateTime().toMillis())
                                showDatePickerDialog.value = false
                            }
                        ) {
                            Text(
                                stringResource(id = com.d9tilov.android.common.android.R.string.ok)
                                    .uppercase(Locale.getDefault())
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePickerDialog.value = false }
                        ) {
                            Text(
                                stringResource(id = com.d9tilov.android.common.android.R.string.cancel)
                                    .uppercase(Locale.getDefault())
                            )
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

@Composable
fun ShowError() {
    Text(
        text = stringResource(id = R.string.transaction_invalid_amount),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultTransactionCreationPreview() {
    MoneyManagerTheme {
        TransactionCreationScreen(
            uiState = TransactionUiState.EMPTY.copy(
                transaction = Transaction.EMPTY.copy(
                    sum = BigDecimal(42),
                    category = Category.EMPTY_EXPENSE.copy(
                        name = "My category",
                        icon = com.d9tilov.android.designsystem.R.drawable.ic_category_food,
                        color = android.R.color.holo_red_dark
                    )
                ),
            ),
            onBackClicked = {},
            onSumChanged = {},
            onSaveClicked = {},
            onInStatisticsChanged = {},
            onDescriptionChanged = {},
            onCurrencyClicked = {},
            onCategoryClicked = {_, _ ->},
            onDateClicked = {}
        )
    }
}
