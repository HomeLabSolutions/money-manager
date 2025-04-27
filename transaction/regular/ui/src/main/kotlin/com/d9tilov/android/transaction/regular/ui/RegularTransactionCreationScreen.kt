package com.d9tilov.android.transaction.regular.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.category.domain.entity.CategoryDestination
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.MainPriceFieldParser
import com.d9tilov.android.designsystem.AutoSizeTextField
import com.d9tilov.android.designsystem.BottomActionButton
import com.d9tilov.android.designsystem.DescriptionTextField
import com.d9tilov.android.designsystem.DottedDivider
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.transaction.regular.ui.vm.DaysInWeek
import com.d9tilov.android.transaction.regular.ui.vm.PeriodMenuItem
import com.d9tilov.android.transaction.regular.ui.vm.RegularTransactionCreationUiState
import com.d9tilov.android.transaction.regular.ui.vm.RegularTransactionCreationViewModel

@Composable
fun RegularTransactionCreationRoute(
    viewModel: RegularTransactionCreationViewModel,
    onCurrencyClicked: (String) -> Unit,
    clickCategory: (TransactionType, CategoryDestination) -> Unit,
    onSaveClicked: () -> Unit,
    clickBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    RegularTransactionCreationScreen(
        uiState = state,
        onCurrencyClicked = onCurrencyClicked,
        onBackClicked = clickBack,
        onSaveClicked = {
            viewModel.saveOrUpdate()
            onSaveClicked()
        },
        onSumChanged = viewModel::updateAmount,
        onCategoryClicked = clickCategory,
        onCurPeriodItemUpdate = viewModel::updateCurPeriodItem,
        onWeekDayClicked = viewModel::updateWeekDay,
        onDayOfMonthClicked = viewModel::updateDayOfMonth,
        onDescriptionChanged = viewModel::updateDescription,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTransactionCreationScreen(
    uiState: RegularTransactionCreationUiState,
    onSumChanged: (String) -> Unit,
    onCurrencyClicked: (String) -> Unit,
    onCategoryClicked: (TransactionType, CategoryDestination) -> Unit,
    onCurPeriodItemUpdate: (PeriodMenuItem) -> Unit,
    onWeekDayClicked: (DaysInWeek) -> Unit,
    onDayOfMonthClicked: (Int) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    val context = LocalContext.current
    var showError by remember { mutableStateOf(false) }
    var saveBtnEnabled by remember { mutableStateOf(showError) }
    var openDayOfMonthDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            MmTopAppBar(
                titleRes =
                    when (uiState.transaction.type) {
                        TransactionType.EXPENSE -> R.string.regular_transaction_expense_title
                        TransactionType.INCOME -> R.string.regular_transaction_income_title
                    },
                onNavigationClick = onBackClicked,
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = padding.calculateTopPadding()),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .weight(1f),
            ) {
                Text(
                    modifier =
                        Modifier.padding(
                            start = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                            end = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                            top = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small),
                        ),
                    text = stringResource(id = R.string.regular_transaction_edit_sum_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(
                    modifier =
                        Modifier.padding(
                            horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier =
                            Modifier
                                .alignByBaseline()
                                .clickable(onClick = { onCurrencyClicked(uiState.transaction.currencyCode) }),
                        text = uiState.transaction.currencyCode.getSymbolByCode(),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize =
                            dimensionResource(
                                id = com.d9tilov.android.designsystem.R.dimen.currency_sign_big_text_size,
                            ).value.sp,
                    )
                    AutoSizeTextField(
                        modifier = Modifier.alignByBaseline(),
                        inputValue = uiState.amount,
                        inputValueChanged = { text ->
                            showError = !MainPriceFieldParser.isInputValid(text)
                            saveBtnEnabled = !showError
                            onSumChanged(text)
                        },
                        showError = { if (showError) ShowError() },
                    )
                }
                Row(
                    modifier =
                        Modifier
                            .padding(
                                horizontal =
                                    dimensionResource(
                                        id = com.d9tilov.android.designsystem.R.dimen.padding_large,
                                    ),
                            ).clickable {
                                onCategoryClicked(
                                    uiState.transaction.type,
                                    CategoryDestination.EDIT_REGULAR_TRANSACTION_SCREEN,
                                )
                            },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val tintColor: Color?
                    if (uiState.transaction.category != Category.EMPTY_EXPENSE) {
                        saveBtnEnabled = !showError
                        Icon(
                            modifier =
                                Modifier.size(
                                    dimensionResource(
                                        id = com.d9tilov.android.common.android.R.dimen.category_creation_icon_size,
                                    ),
                                ),
                            imageVector = ImageVector.vectorResource(id = uiState.transaction.category.icon),
                            contentDescription = "Category",
                            tint =
                                Color(
                                    ContextCompat.getColor(
                                        context,
                                        uiState.transaction.category.color,
                                    ),
                                ),
                        )
                        Text(
                            modifier =
                                Modifier.padding(
                                    horizontal =
                                        dimensionResource(
                                            id = com.d9tilov.android.designsystem.R.dimen.padding_small,
                                        ),
                                ),
                            text = uiState.transaction.category.name,
                            color =
                                Color(
                                    ContextCompat.getColor(
                                        context,
                                        uiState.transaction.category.color,
                                    ),
                                ),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        tintColor =
                            Color(
                                ContextCompat.getColor(
                                    context,
                                    uiState.transaction.category.color,
                                ),
                            )
                    } else {
                        Text(
                            modifier =
                                Modifier.padding(
                                    start =
                                        dimensionResource(
                                            id = com.d9tilov.android.designsystem.R.dimen.padding_small,
                                        ),
                                    end =
                                        dimensionResource(
                                            id = com.d9tilov.android.designsystem.R.dimen.padding_extra_small,
                                        ),
                                ),
                            text = stringResource(id = R.string.regular_transaction_choose_category),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        tintColor = MaterialTheme.colorScheme.primary
                        saveBtnEnabled = false
                    }
                    Icon(
                        modifier =
                            Modifier.size(
                                dimensionResource(
                                    id = com.d9tilov.android.common.android.R.dimen.category_creation_icon_size,
                                ),
                            ),
                        imageVector = MoneyManagerIcons.ArrowRight,
                        contentDescription = "Category",
                        tint = tintColor,
                    )
                }
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(82.dp)
                            .padding(
                                horizontal =
                                    dimensionResource(
                                        id = com.d9tilov.android.designsystem.R.dimen.padding_large,
                                    ),
                                vertical =
                                    dimensionResource(
                                        id = com.d9tilov.android.designsystem.R.dimen.padding_small,
                                    ),
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier =
                            Modifier.size(
                                dimensionResource(
                                    id = com.d9tilov.android.common.android.R.dimen.category_creation_icon_size,
                                ),
                            ),
                        imageVector = MoneyManagerIcons.Repeat,
                        contentDescription = "Repeat",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Column(
                        modifier =
                            Modifier.padding(
                                horizontal =
                                    dimensionResource(
                                        id = com.d9tilov.android.designsystem.R.dimen.padding_small,
                                    ),
                            ),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(id = R.string.regular_transaction_repeat_title),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            DropdownPeriodMenu(
                                modifier =
                                    Modifier.padding(
                                        horizontal =
                                            dimensionResource(
                                                id = com.d9tilov.android.designsystem.R.dimen.padding_small,
                                            ),
                                    ),
                                selectedItem = uiState.curPeriodItem,
                                onMenuItemClick = onCurPeriodItemUpdate::invoke,
                            )
                        }
                        when (uiState.curPeriodItem) {
                            PeriodMenuItem.DAY -> {}
                            PeriodMenuItem.WEEK ->
                                DaysOfWeek(uiState.curDayInWeek) {
                                    onWeekDayClicked(it)
                                }

                            PeriodMenuItem.MONTH ->
                                Text(
                                    text =
                                        stringResource(
                                            id = R.string.regular_transaction_repeat_every_month_on,
                                            uiState.curDayOfMonth,
                                        ),
                                    modifier =
                                        Modifier
                                            .clickable { openDayOfMonthDialog = true }
                                            .padding(
                                                vertical =
                                                    dimensionResource(
                                                        id = com.d9tilov.android.designsystem.R.dimen.padding_small,
                                                    ),
                                            ),
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                        }
                    }
                }
                DottedDivider(
                    modifier =
                        Modifier.padding(
                            start = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                            end = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                            top = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        ),
                )
                DescriptionTextField(
                    modifier =
                        Modifier.padding(
                            horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                            vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small),
                        ),
                    value = uiState.transaction.description,
                    onValueChange = onDescriptionChanged,
                )
            }
            BottomActionButton(
                modifier =
                    Modifier
                        .navigationBarsPadding()
                        .imePadding(),
                onClick = onSaveClicked,
                enabled = saveBtnEnabled,
            )
            if (openDayOfMonthDialog) {
                DayInMonthDialog(
                    uiState.curDayOfMonth,
                    onDismiss = { openDayOfMonthDialog = false },
                    onDayClicked = {
                        onDayOfMonthClicked(it)
                        openDayOfMonthDialog = false
                    },
                )
            }
        }
    }
}

@Composable
fun DayInMonthDialog(
    selectedDay: Int,
    onDismiss: () -> Unit,
    onDayClicked: (Int) -> Unit,
) {
    val days = (1..31).toList()
    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Column(
                modifier =
                    Modifier
                        .height(342.dp)
                        .width(260.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.regular_transaction_repeat_day_of_month),
                    modifier =
                        Modifier.padding(
                            dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small),
                        ),
                    color = MaterialTheme.colorScheme.primary,
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    items(days, { it }) { item ->
                        Box(
                            modifier =
                                Modifier
                                    .size(36.dp)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        CircleShape,
                                    ).clickable { onDayClicked(item) }
                                    .background(
                                        color =
                                            if (item == selectedDay) {
                                                MaterialTheme.colorScheme.secondary
                                            } else {
                                                MaterialTheme.colorScheme.background
                                            },
                                        shape = CircleShape,
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = item.toString(),
                                fontSize = 12.sp,
                                color =
                                    if (item == selectedDay) {
                                        MaterialTheme.colorScheme.onSecondary
                                    } else {
                                        MaterialTheme.colorScheme.secondary
                                    },
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun DropdownPeriodMenu(
    modifier: Modifier = Modifier,
    selectedItem: PeriodMenuItem,
    onMenuItemClick: (PeriodMenuItem) -> Unit,
) {
    val items = PeriodMenuItem.entries
    var isExpanded by remember { mutableStateOf(false) }
    var curValue by remember { mutableStateOf(selectedItem) }
    Row(
        modifier =
            modifier
                .clickable { isExpanded = !isExpanded },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text =
                when (curValue) {
                    PeriodMenuItem.DAY -> stringResource(id = R.string.regular_transaction_repeat_list_day)
                    PeriodMenuItem.WEEK -> stringResource(id = R.string.regular_transaction_repeat_list_week)
                    PeriodMenuItem.MONTH -> stringResource(id = R.string.regular_transaction_repeat_list_month)
                },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
        )
        Icon(
            imageVector = MoneyManagerIcons.ArrowDropDown,
            contentDescription = "ArrowDropDown",
            tint = MaterialTheme.colorScheme.primary,
        )
        DropdownMenu(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.tertiaryContainer),
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text =
                                when (item) {
                                    PeriodMenuItem.DAY ->
                                        stringResource(
                                            id = R.string.regular_transaction_repeat_list_day,
                                        )
                                    PeriodMenuItem.WEEK ->
                                        stringResource(
                                            id = R.string.regular_transaction_repeat_list_week,
                                        )
                                    PeriodMenuItem.MONTH ->
                                        stringResource(
                                            id = R.string.regular_transaction_repeat_list_month,
                                        )
                                },
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    onClick = {
                        curValue = item
                        onMenuItemClick(item)
                        isExpanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun DaysOfWeek(
    selected: DaysInWeek,
    onDayClicked: (DaysInWeek) -> Unit,
) {
    val daysOfWeek = DaysInWeek.entries
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        items(daysOfWeek, { it.name }) { item ->
            Box(
                modifier =
                    Modifier
                        .size(32.dp)
                        .padding(2.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                        .clickable { onDayClicked(item) }
                        .background(
                            color =
                                if (item == selected) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.background
                                },
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = item.id),
                    fontSize = 12.sp,
                    color =
                        if (item == selected) {
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        },
                )
            }
        }
    }
}

@Composable
fun ShowError() {
    Text(
        text = stringResource(id = R.string.regular_transaction_invalid_amount),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultRegularTransactionCreationPreview() {
    RegularTransactionCreationScreen(
        uiState = RegularTransactionCreationUiState.EMPTY.copy(curPeriodItem = PeriodMenuItem.WEEK),
        onBackClicked = {},
        onSumChanged = {},
        onSaveClicked = {},
        onCurrencyClicked = {},
        onCategoryClicked = { _, _ -> },
        onCurPeriodItemUpdate = {},
        onWeekDayClicked = {},
        onDayOfMonthClicked = {},
        onDescriptionChanged = {},
    )
}
