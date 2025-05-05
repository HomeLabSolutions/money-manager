package com.d9tilov.android.incomeexpense.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.category.domain.entity.Category.Companion.ALL_ITEMS_ID
import com.d9tilov.android.category.domain.entity.CategoryDestination
import com.d9tilov.android.common.android.utils.TRANSACTION_DATE_FORMAT
import com.d9tilov.android.common.android.utils.formatDate
import com.d9tilov.android.core.constants.CurrencyConstants.ZERO
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.core.utils.toKeyPress
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.EmptyListPlaceholder
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.incomeexpense.ui.vm.EditMode
import com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo
import com.d9tilov.android.incomeexpense.ui.vm.ExpenseUiState
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseUiState
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.android.incomeexpense.ui.vm.IncomeInfo
import com.d9tilov.android.incomeexpense.ui.vm.IncomeUiState
import com.d9tilov.android.incomeexpense.ui.vm.MainPrice
import com.d9tilov.android.incomeexpense.ui.vm.Price
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.EXPENSE
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.INCOME
import com.d9tilov.android.incomeexpense.ui.vm.screenTypes
import com.d9tilov.android.incomeexpense.ui.vm.toScreenType
import com.d9tilov.android.transaction.ui.TransactionItem
import com.d9tilov.android.transaction.ui.model.BaseTransaction
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun IncomeExpenseRoute(
    viewModel: IncomeExpenseViewModel = hiltViewModel(),
    onTransactionClicked: (TransactionUiModel) -> Unit,
    onCurrencyClicked: (String) -> Unit,
    onAllCategoryClicked: (ScreenType, CategoryDestination) -> Unit,
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.errorMessageFlow
            .collect { message ->
                Toast
                    .makeText(context, message, Toast.LENGTH_SHORT)
                    .show()
            }
    }
    IncomeExpenseScreen(
        uiState = uiState,
        onTransactionClicked = onTransactionClicked,
        onNumberClicked = { viewModel.addNumber(it) },
        onCategoryClicked = { category -> viewModel.addTransaction(category.id) },
        onEditModeChanged = { mode -> viewModel.updateMode(mode) },
        onCurrencyClicked = { onCurrencyClicked(uiState.price.currencyCode) },
        onAllCategoryClicked = onAllCategoryClicked,
        onDeleteTransactionConfirmClicked = viewModel::deleteTransaction,
    )
}

@Composable
fun IncomeExpenseScreen(
    uiState: IncomeExpenseUiState,
    onNumberClicked: (KeyPress) -> Unit,
    onTransactionClicked: (TransactionUiModel) -> Unit,
    onCategoryClicked: (Category) -> Unit,
    onEditModeChanged: (EditMode) -> Unit,
    onCurrencyClicked: () -> Unit,
    onDeleteTransactionConfirmClicked: (TransactionUiModel) -> Unit,
    onAllCategoryClicked: (ScreenType, CategoryDestination) -> Unit,
) {
    val listState = rememberLazyListState()
    Scaffold(
        floatingActionButton = {
            if (uiState.mode == EditMode.LIST) {
                AnimatedFloatingActionButton(
                    listState,
                    onClick = { onEditModeChanged(EditMode.KEYBOARD) },
                )
            }
        },
    ) { paddingValues ->
        HomeTabs(
            listState = listState,
            uiState = uiState,
            modifier = Modifier.padding(paddingValues),
            onNumberClicked = onNumberClicked,
            onCategoryClicked = onCategoryClicked,
            onKeyboardClicked = { onEditModeChanged(EditMode.LIST) },
            onCurrencyClicked = onCurrencyClicked,
            onAllCategoryClicked = onAllCategoryClicked,
            onTransactionClicked = onTransactionClicked,
            onDeleteTransactionConfirmClicked = onDeleteTransactionConfirmClicked,
        )
    }
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var prevIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var prevScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (prevIndex != firstVisibleItemIndex) {
                prevIndex > firstVisibleItemIndex
            } else {
                prevScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                prevIndex = firstVisibleItemIndex
                prevScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun AnimatedFloatingActionButton(
    listState: LazyListState,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = listState.isScrollingUp(),
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.secondary,
        ) {
            Icon(
                imageVector = MoneyManagerIcons.AddCircle,
                contentDescription = "Add transaction",
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun MainPriceInput(
    price: MainPrice,
    modifier: Modifier,
    onCurrencyClicked: () -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = onCurrencyClicked,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(50),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            ComposeCurrencyView(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                symbol = price.currencyCode.getSymbolByCode(),
                value = price.value,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionListLayout(
    listState: LazyListState,
    modifier: Modifier,
    transactions: Flow<PagingData<BaseTransaction>>,
    screenType: ScreenType,
    onTransactionClicked: (TransactionUiModel) -> Unit,
    onDeleteTransactionConfirmClicked: (TransactionUiModel) -> Unit,
) {
    val lazyTransactionItems: LazyPagingItems<BaseTransaction> =
        transactions.collectAsLazyPagingItems()
    if (lazyTransactionItems.loadState.refresh is LoadState.NotLoading && lazyTransactionItems.itemCount == 0) {
        EmptyListPlaceholder(
            modifier = Modifier.fillMaxSize(),
            icon = painterResource(id = MoneyManagerIcons.EmptyPlaceholder),
            title =
                when (screenType) {
                    EXPENSE -> stringResource(id = R.string.transaction_empty_placeholder_expense_title)
                    INCOME -> stringResource(id = R.string.transaction_empty_placeholder_income_title)
                },
            subtitle = stringResource(id = R.string.transaction_empty_placeholder_subtitle),
        )
        return
    }
    if (lazyTransactionItems.loadState.refresh is LoadState.Error) {
        // handle error
    }
    val openRemoveDialog = remember { mutableStateOf<TransactionUiModel?>(null) }
    LazyColumn(modifier = modifier, state = listState) {
        for (index in 0 until lazyTransactionItems.itemCount) {
            val currentItem = lazyTransactionItems.peek(index)
            currentItem?.let { tr ->
                if (tr.itemType == BaseTransaction.HEADER) {
                    stickyHeader(key = tr.date.hashCode()) {
                        Surface(
                            modifier = Modifier.fillParentMaxWidth(),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 24.dp),
                                text = formatDate(currentItem.date, TRANSACTION_DATE_FORMAT),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                } else {
                    item(key = index) {
                        val item = lazyTransactionItems[index] as TransactionUiModel
                        val dismissState =
                            rememberSwipeToDismissBoxState(
                                confirmValueChange = {
                                    if (it == SwipeToDismissBoxValue.EndToStart) {
                                        openRemoveDialog.value = item
                                    }
                                    true
                                },
                            )
                        if (openRemoveDialog.value == null) LaunchedEffect(Unit) { dismissState.reset() }
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val backgroundColor by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                                        else -> Color.Transparent
                                    },
                                    label = "",
                                )
                                val iconScale by animateFloatAsState(
                                    targetValue =
                                        if (dismissState.targetValue ==
                                            SwipeToDismissBoxValue.Settled
                                        ) {
                                            0.0f
                                        } else {
                                            1.3f
                                        },
                                    label = "",
                                )
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color = backgroundColor)
                                        .padding(
                                            horizontal =
                                                dimensionResource(
                                                    id = com.d9tilov.android.designsystem.R.dimen.padding_medium,
                                                ),
                                        ),
                                    contentAlignment = Alignment.CenterEnd,
                                ) {
                                    Icon(
                                        modifier = Modifier.scale(iconScale),
                                        imageVector = MoneyManagerIcons.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onError,
                                    )
                                }
                            },
                            content = {
                                TransactionItem(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable { onTransactionClicked(item) },
                                    transaction = item,
                                )
                            },
                        )
                    }
                }
            }
        }
        item {
            if (lazyTransactionItems.loadState.append is LoadState.Loading) {
                // handle loading
            }
        }
    }
    SimpleDialog(
        show = openRemoveDialog.value != null,
        title = stringResource(R.string.transaction_delete_dialog_title),
        subtitle = stringResource(R.string.transaction_delete_dialog_subtitle),
        dismissButton = stringResource(com.d9tilov.android.common.android.R.string.cancel),
        confirmButton = stringResource(com.d9tilov.android.common.android.R.string.delete),
        onConfirm = {
            openRemoveDialog.value?.let { transactionToDelete ->
                onDeleteTransactionConfirmClicked(transactionToDelete)
                openRemoveDialog.value = null
            }
        },
        onDismiss = { openRemoveDialog.value = null },
    )
}

@Composable
@Suppress("CognitiveComplexMethod")
fun HomeTabs(
    listState: LazyListState,
    uiState: IncomeExpenseUiState,
    modifier: Modifier = Modifier,
    onTransactionClicked: (TransactionUiModel) -> Unit,
    onNumberClicked: (KeyPress) -> Unit,
    onCategoryClicked: (Category) -> Unit,
    onKeyboardClicked: () -> Unit,
    onCurrencyClicked: () -> Unit,
    onDeleteTransactionConfirmClicked: (TransactionUiModel) -> Unit,
    onAllCategoryClicked: (ScreenType, CategoryDestination) -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val pagerState =
        rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
        ) { screenTypes.size }
    val coroutineScope = rememberCoroutineScope()
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        HomeTabIndicator(Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]))
    }
    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = indicator,
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            screenTypes.forEachIndexed { index, type ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
                        tabIndex = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(tabIndex)
                        }
                    },
                    text = {
                        Text(
                            text =
                                when (type) {
                                    EXPENSE -> stringResource(R.string.tab_expense)
                                    INCOME -> stringResource(R.string.tab_income)
                                },
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                )
            }
        }
        if (uiState.mode == EditMode.KEYBOARD) {
            MainPriceInput(
                price = uiState.price,
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp),
                onCurrencyClicked = onCurrencyClicked,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        HorizontalPager(state = pagerState) { tabIndex: Int ->
            if (uiState.mode == EditMode.KEYBOARD) {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
                    val screenType = tabIndex.toScreenType()
                    if (screenType == INCOME) {
                        uiState.incomeUiState.incomeInfo?.let {
                            IncomeInfoBlock(
                                Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(start = 32.dp),
                                it,
                            )
                        }
                    } else {
                        uiState.expenseUiState.expenseInfo?.let {
                            ExpenseInfoBlock(
                                Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(start = 32.dp),
                                it,
                            )
                        }
                    }

                    CategoryListLayout(
                        categoryList =
                            if (screenType == INCOME) {
                                uiState.incomeUiState.incomeCategoryList
                            } else {
                                uiState.expenseUiState.expenseCategoryList
                            },
                        modifier = Modifier.fillMaxWidth(),
                        onItemClicked = { category -> onCategoryClicked(category) },
                        onAllCategoryClicked = {
                            onAllCategoryClicked(
                                screenType,
                                if (uiState.price.value == ZERO) {
                                    CategoryDestination.MAIN_SCREEN
                                } else {
                                    CategoryDestination.MAIN_WITH_SUM_SCREEN
                                },
                            )
                        },
                    )
                }
            } else {
                TransactionListLayout(
                    listState,
                    Modifier.fillMaxSize(),
                    if (tabIndex.toScreenType() == INCOME) {
                        uiState.incomeUiState.incomeTransactions
                    } else {
                        uiState.expenseUiState.expenseTransactions
                    },
                    tabIndex.toScreenType(),
                    onTransactionClicked = onTransactionClicked,
                    onDeleteTransactionConfirmClicked = onDeleteTransactionConfirmClicked,
                )
            }
        }
        if (uiState.mode == EditMode.KEYBOARD) {
            KeyBoardLayout(
                modifier = Modifier.fillMaxWidth(),
                onNumberClicked = onNumberClicked,
                onKeyboardClicked = onKeyboardClicked,
            )
        }
    }
}

@Composable
fun HomeTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Spacer(
        modifier
            .padding(horizontal = 24.dp)
            .height(4.dp)
            .background(color, RoundedCornerShape(topStartPercent = 100, topEndPercent = 100)),
    )
}

@Composable
fun KeyBoardLayout(
    modifier: Modifier,
    onKeyboardClicked: () -> Unit,
    onNumberClicked: (KeyPress) -> Unit,
) {
    val data: List<String> = KeyPress.entries.map { it.value }
    Box(modifier = modifier.padding(bottom = 8.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
            contentPadding = PaddingValues(start = 40.dp, end = 40.dp),
        ) {
            items(data, key = { it.hashCode() }) { item ->
                val keyPress = item.toKeyPress() ?: return@items
                if (keyPress != KeyPress.Del) {
                    Text(
                        modifier = Modifier.clickable { onNumberClicked(keyPress) },
                        text = item,
                        style =
                            TextStyle.Default.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center,
                            ),
                    )
                } else {
                    IconButton(onClick = { onNumberClicked(keyPress) }) {
                        Icon(
                            imageVector = MoneyManagerIcons.BackSpace,
                            contentDescription = "BackSpace",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
        IconButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp),
            onClick = onKeyboardClicked,
        ) {
            Icon(
                imageVector = MoneyManagerIcons.HideKeyboard,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "HideKeyboard",
            )
        }
    }
}

@Composable
fun ExpenseInfoBlock(
    modifier: Modifier,
    info: ExpenseInfo,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        InfoLabel(info.ableToSpendToday)
        InfoLabel(info.wasSpendToday)
        InfoLabel(info.wasSpendInPeriod)
    }
}

@Composable
fun IncomeInfoBlock(
    modifier: Modifier,
    info: IncomeInfo,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        InfoLabel(info.wasEarnedInPeriod)
    }
}

@Composable
fun InfoLabel(price: Price) {
    Row(Modifier.padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        InfoLabelTitle(text = stringResource(id = price.label))
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = price.value,
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
fun InfoLabelTitle(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primary),
    ) {
        Text(
            modifier = Modifier.padding(6.dp),
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
        )
    }
}

@Composable
fun CategoryListLayout(
    categoryList: List<Category>,
    modifier: Modifier,
    onItemClicked: (Category) -> Unit,
    onAllCategoryClicked: () -> Unit,
) {
    val context = LocalContext.current

    LazyHorizontalGrid(
        modifier =
            modifier
                .height(200.dp)
                .padding(16.dp),
        rows = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        items(categoryList, { it.id }) { item ->
            Column(
                modifier =
                    Modifier
                        .size(dimensionResource(id = R.dimen.category_item_size))
                        .padding(8.dp)
                        .clickable {
                            if (item.id == ALL_ITEMS_ID) {
                                onAllCategoryClicked()
                            } else {
                                onItemClicked(item)
                            }
                        },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = item.icon),
                    contentDescription = "Backup",
                    tint = Color(ContextCompat.getColor(context, item.color)),
                )
                Text(
                    text = if (item.id == ALL_ITEMS_ID) stringResource(id = R.string.category_all) else item.name,
                    color = Color(ContextCompat.getColor(context, item.color)),
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
@Preview
fun TransactionListItemPreview() {
    MoneyManagerTheme {
        TransactionItem(
            modifier = Modifier.fillMaxWidth(),
            transaction =
                TransactionUiModel.EMPTY.copy(
                    sum = BigDecimal.TEN,
                    usdSum = BigDecimal(5),
                    currencyCode = "RUB",
                    isRegular = true,
                    inStatistics = true,
                    description = "description",
                    category = mockCategory(2, "Cafe"),
                ),
        )
    }
}

@Composable
@Preview
fun PreviewIncomeExpenseScreen() {
    MoneyManagerTheme {
        IncomeExpenseScreen(
            uiState =
                IncomeExpenseUiState.EMPTY.copy(
                    incomeUiState =
                        IncomeUiState(
                            incomeCategoryList =
                                listOf(
                                    mockCategory(1L, "Category1"),
                                    mockCategory(2L, "Category2"),
                                    mockCategory(3L, "Category3"),
                                    mockCategory(4L, "Category4"),
                                    mockCategory(5L, "Category5"),
                                    mockCategory(6L, "Category6"),
                                    mockCategory(7L, "Category7"),
                                    mockCategory(8L, "Category8"),
                                    mockCategory(9L, "Category9"),
                                    mockCategory(10L, "Category10"),
                                    mockCategory(11L, "Category11"),
                                    mockCategory(12L, "Category12"),
                                    mockCategory(13L, "Category13"),
                                    mockCategory(14L, "Category14"),
                                    mockCategory(15L, "Category15"),
                                ),
                        ),
                    expenseUiState =
                        ExpenseUiState.EMPTY.copy(
                            expenseInfo =
                                ExpenseInfo(
                                    ableToSpendToday =
                                        Price(
                                            R.string.expense_info_can_spend_today_title,
                                            "$42",
                                        ),
                                    wasSpendToday = Price(R.string.expense_info_today_title, "$43"),
                                    wasSpendInPeriod = Price(R.string.expense_info_period_title, "$44"),
                                ),
                        ),
                ),
            onNumberClicked = {},
            onCategoryClicked = {},
            onEditModeChanged = {},
            onCurrencyClicked = {},
            onAllCategoryClicked = { _, _ -> },
            onTransactionClicked = {},
            onDeleteTransactionConfirmClicked = {},
        )
    }
}

@Composable
@Preview
fun InfoLabelPreview() {
    MoneyManagerTheme {
        ExpenseInfoBlock(
            Modifier,
            ExpenseInfo(
                ableToSpendToday = Price(R.string.expense_info_can_spend_today_title, "$41"),
                wasSpendToday = Price(R.string.expense_info_today_title, "$42"),
                wasSpendInPeriod = Price(R.string.expense_info_period_title, "$43"),
            ),
        )
    }
}

private fun mockCategory(
    id: Long,
    name: String,
) = Category.EMPTY_INCOME.copy(
    id = id,
    name = name,
    icon = R.drawable.ic_category_beach,
    color = android.R.color.holo_blue_light,
)
