package com.d9tilov.android.incomeexpense.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.CurrencyConstants
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.core.utils.toKeyPress
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.incomeexpense.ui.vm.EditMode
import com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo
import com.d9tilov.android.incomeexpense.ui.vm.ExpenseUiState
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseUiState
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.android.incomeexpense.ui.vm.IncomeUiState
import com.d9tilov.android.incomeexpense.ui.vm.Price
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.EXPENSE
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.INCOME
import com.d9tilov.android.incomeexpense.ui.vm.TransactionSpendingTodayPrice
import com.d9tilov.android.incomeexpense.ui.vm.toScreenType
import com.d9tilov.android.incomeexpense_ui.R
import com.d9tilov.android.transaction.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode


@Composable
fun IncomeExpenseRoute(viewModel: IncomeExpenseViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    IncomeExpenseScreen(
        uiState = uiState,
        onAddBtnClicked = { viewModel.changeEditMode() },
        onNumberClicked = { viewModel.addNumber(it) },
        onHideKeyboardClicked = { viewModel.changeEditMode() },
        onCategoryClicked = {}
    )
}

@Composable
fun IncomeExpenseScreen(
    uiState: IncomeExpenseUiState,
    onAddBtnClicked: () -> Unit,
    onNumberClicked: (KeyPress) -> Unit,
    onHideKeyboardClicked: () -> Unit,
    onCategoryClicked: (Category) -> Unit,
) {

    Scaffold(
        floatingActionButton = {
            if (uiState.editMode == EditMode.LIST) {
                FloatingActionButton(
                    onClick = onAddBtnClicked,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = "Add transaction"
                    )
                }
            }
        }
    ) { paddingValues ->
        HomeTabs(
            uiState = uiState,
            modifier = Modifier.padding(paddingValues),
            onNumberClicked = onNumberClicked,
            onHideKeyboardClicked = onHideKeyboardClicked,
            onCategoryClicked = onCategoryClicked
        )
    }
}

@Composable
fun MainPriceInput(price: Price, modifier: Modifier) {
    Surface(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(50),
    ) {
        ComposeCurrencyView(symbol = price.currency, value = price.value)
    }
}

@Composable
fun TransactionListLayout(modifier: Modifier, transactions: Flow<PagingData<Transaction>>) {
    val lazyTransactionItems: LazyPagingItems<Transaction> = transactions.collectAsLazyPagingItems()

    if (lazyTransactionItems.loadState.refresh is LoadState.Error) {
        // handle error
    }
    val state = rememberLazyListState()
    LazyColumn(state = state) {
        items(
            count = lazyTransactionItems.itemCount,
            key = lazyTransactionItems.itemKey { it.id }) { index ->
            val transaction: Transaction? = lazyTransactionItems[index]
            transaction?.let { tr -> TransactionItem(modifier = Modifier.fillMaxWidth(), tr) }
        }
        item {
            if (lazyTransactionItems.loadState.append is LoadState.Loading) {
                // handle loading
            }
        }
    }
}

@Composable
fun TransactionItem(modifier: Modifier, transaction: Transaction) {
    val context = LocalContext.current
    ConstraintLayout(
        modifier = modifier
            .height(72.dp)
            .padding(start = 32.dp)
    ) {
        val (idIcon, idTitle, idDescription, idRegularIcon, idInStatisticsIcon, idSum) = createRefs()
        Icon(
            modifier = Modifier
                .constrainAs(idIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .size(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.category_item_icon_size)),
            painter = painterResource(id = transaction.category.icon),
            contentDescription = "Transaction",
            tint = Color(ContextCompat.getColor(context, transaction.category.color))
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .constrainAs(idTitle) {
                    top.linkTo(idIcon.top)
                    bottom.linkTo(idIcon.bottom)
                    start.linkTo(idIcon.end)
                },
            text = transaction.category.name,
            style = MaterialTheme.typography.displayLarge,
            fontSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.income_expense_name_text_size).value.sp,
            maxLines = 1,
            color = Color(ContextCompat.getColor(context, transaction.category.color)),
            overflow = TextOverflow.Ellipsis
        )
        if (transaction.description.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 4.dp)
                    .constrainAs(idDescription) {
                        top.linkTo(idTitle.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(idTitle.start)
                    },
                text = transaction.description,
                style = MaterialTheme.typography.bodySmall,
                fontSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.income_expense_name_description_text_size).value.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
        if (transaction.isRegular) {
            Icon(
                modifier = Modifier
                    .constrainAs(idRegularIcon) {
                        top.linkTo(idIcon.top)
                        end.linkTo(idIcon.start)
                    }
                    .size(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.transaction_meta_icon_size)),
                painter = painterResource(MoneyManagerIcons.RegularTransaction),
                contentDescription = "RegularTransaction",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
        if (!transaction.inStatistics) {
            Icon(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .constrainAs(idInStatisticsIcon) {
                        top.linkTo(idRegularIcon.bottom)
                        end.linkTo(idIcon.start)
                    }
                    .size(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.transaction_meta_icon_size)),
                painter = painterResource(MoneyManagerIcons.InStatisticsTransaction),
                contentDescription = "InStatisticsTransaction",
                tint = MaterialTheme.colorScheme.error
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 32.dp)
                .constrainAs(idSum) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            ComposeCurrencyView(
                value = transaction.sum.toString(),
                valueSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.currency_sum_small_text_size).value.sp,
                symbol = transaction.currencyCode.getSymbolByCode(),
                symbolSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.currency_sign_small_text_size).value.sp
            )
            ComposeCurrencyView(
                value = transaction.usdSum.toString(),
                valueSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.currency_extra_small_text_size).value.sp,
                symbol = DEFAULT_CURRENCY_SYMBOL,
                symbolSize = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.currency_sign_extra_small_text_size).value.sp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeTabs(
    uiState: IncomeExpenseUiState,
    modifier: Modifier = Modifier,
    onNumberClicked: (KeyPress) -> Unit,
    onHideKeyboardClicked: () -> Unit,
    onCategoryClicked: (Category) -> Unit,
) {
    Timber.tag(TAG).d("uistate: $uiState")
    var tabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { uiState.screenTypes.size }
    val coroutineScope = rememberCoroutineScope()
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        HomeTabIndicator(Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]))
    }
    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = indicator,
            modifier = modifier
        ) {
            uiState.screenTypes.forEachIndexed { index, type ->
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
                            text = when (type) {
                                EXPENSE -> stringResource(R.string.tab_expense)
                                INCOME -> stringResource(R.string.tab_income)
                            },
                        )
                    }
                )
            }
        }
        if (uiState.editMode == EditMode.KEYBOARD) {
            MainPriceInput(
                price = uiState.price,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(32.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        HorizontalPager(state = pagerState) { tabIndex: Int ->
            if (uiState.editMode == EditMode.KEYBOARD) {
                Column(Modifier.fillMaxWidth()) {
                    uiState.expenseUiState.expenseInfo?.let {
                        ExpenseInfoLayout(
                            it,
                            Modifier.fillMaxWidth()
                        )
                    }
                    CategoryListLayout(
                        categoryList =
                        if (tabIndex.toScreenType() == INCOME) uiState.incomeUiState.incomeCategoryList
                        else uiState.expenseUiState.expenseCategoryList,
                        modifier = Modifier.fillMaxWidth(),
                        onItemClicked = onCategoryClicked
                    )
                }
            } else {
                TransactionListLayout(
                    Modifier.fillMaxSize(),
                    if (tabIndex.toScreenType() == INCOME) uiState.incomeUiState.incomeTransactions
                    else uiState.expenseUiState.expenseTransactions
                )
            }

        }
        if (uiState.editMode == EditMode.KEYBOARD) {
            KeyBoardLayout(
                modifier = Modifier.fillMaxWidth(),
                onNumberClicked = onNumberClicked,
                onHideKeyboardClicked = onHideKeyboardClicked
            )
        }
    }
}

@Composable
fun HomeTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Spacer(
        modifier
            .padding(horizontal = 24.dp)
            .height(4.dp)
            .background(color, RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
    )
}

@Composable
fun KeyBoardLayout(
    modifier: Modifier,
    onNumberClicked: (KeyPress) -> Unit,
    onHideKeyboardClicked: () -> Unit,
) {
    val data: List<String> = KeyPress.values().map { it.value }
    Box(modifier = modifier.padding(bottom = 8.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            userScrollEnabled = false,
            contentPadding = PaddingValues(start = 40.dp, end = 40.dp)
        ) {
            items(data, key = { it.hashCode() }) { item ->
                val keyPress = item.toKeyPress() ?: return@items
                if (keyPress != KeyPress.Del) {
                    ClickableText(
                        text = AnnotatedString(item),
                        style = TextStyle.Default.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        ),
                        onClick = { onNumberClicked.invoke(keyPress) }
                    )
                } else {
                    IconButton(onClick = { onNumberClicked.invoke(keyPress) }) {
                        Icon(
                            painter = painterResource(id = MoneyManagerIcons.BackSpace),
                            contentDescription = "Backup",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp),
            onClick = onHideKeyboardClicked
        ) {
            Icon(
                painter = painterResource(id = MoneyManagerIcons.HideKeyboard),
                contentDescription = "HideKeyboard",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ExpenseInfoLayout(
    info: ExpenseInfo,
    modifier: Modifier,
) {
    var ableToSpendTodayTitle = remember { "" }
    var ableToSpendPrice: Price? = remember { Price.EMPTY }
    var ableToSpendColor = MaterialTheme.colorScheme.onPrimary
    when (info.ableToSpendToday) {
        is TransactionSpendingTodayPrice.OVERSPENDING -> {
            ableToSpendTodayTitle =
                stringResource(id = com.d9tilov.android.category_ui.R.string.category_expense_info_can_spend_today_negate_title)
            ableToSpendPrice = info.ableToSpendToday.trSum
            ableToSpendColor = MaterialTheme.colorScheme.error
        }

        is TransactionSpendingTodayPrice.NORMAL -> {
            ableToSpendTodayTitle =
                stringResource(id = com.d9tilov.android.category_ui.R.string.category_expense_info_can_spend_today_title)
            ableToSpendPrice = info.ableToSpendToday.trSum
            ableToSpendColor =
                if (info.wasSpendToday.value.toBigDecimal().setScale(
                        CurrencyConstants.DECIMAL_LENGTH,
                        RoundingMode.HALF_UP
                    ).signum() > 0
                ) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.error
        }
    }
    ConstraintLayout(modifier = modifier.padding(start = 48.dp)) {
        val (
            idAbleToSpendTitle, idSpendTodayTitle, idSpendInPeriodTitle,
            idApproxSignSpendToday, isApproxSignSpendInPeriod,
            idAbleToSpendValue, idSpendTodayValue, idSpendInPeriodValue,
        ) = createRefs()
        createVerticalChain(
            idAbleToSpendTitle,
            idSpendTodayTitle,
            idSpendInPeriodTitle,
            chainStyle = ChainStyle.Spread
        )
        InfoLabel(
            modifier = Modifier.constrainAs(idAbleToSpendTitle) {
                top.linkTo(parent.top)
                bottom.linkTo(idSpendTodayTitle.top)
                start.linkTo(idSpendTodayTitle.start)
            },
            text = ableToSpendTodayTitle
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .constrainAs(idAbleToSpendValue) {
                    top.linkTo(idAbleToSpendTitle.top)
                    bottom.linkTo(idAbleToSpendTitle.bottom)
                    start.linkTo(idAbleToSpendTitle.end)
                },
            text = ableToSpendPrice.currency + ableToSpendPrice.value
        )
        val padding = 8.dp
        InfoLabel(
            modifier = Modifier
                .padding(vertical = padding)
                .constrainAs(idSpendTodayTitle) {
                    top.linkTo(idAbleToSpendTitle.bottom)
                    bottom.linkTo(idSpendInPeriodTitle.top)
                    start.linkTo(parent.start)
                },
            text = stringResource(id = com.d9tilov.android.category_ui.R.string.category_expense_info_today_title)
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .constrainAs(idApproxSignSpendToday) {
                    top.linkTo(idSpendTodayTitle.bottom)
                    bottom.linkTo(idSpendTodayTitle.top)
                    start.linkTo(idSpendTodayTitle.end)
                },
            text = stringResource(id = com.d9tilov.android.common.android.R.string.approx_sign)
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .constrainAs(idSpendTodayValue) {
                    top.linkTo(idApproxSignSpendToday.top)
                    bottom.linkTo(idApproxSignSpendToday.bottom)
                    start.linkTo(idApproxSignSpendToday.end)
                },
            text = info.wasSpendToday.currency + info.wasSpendToday.value
        )
        InfoLabel(
            modifier = Modifier.constrainAs(idSpendInPeriodTitle) {
                top.linkTo(idSpendTodayTitle.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(idSpendTodayTitle.start)
            },
            text = "bottom"
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .constrainAs(isApproxSignSpendInPeriod) {
                    top.linkTo(idSpendInPeriodTitle.bottom)
                    bottom.linkTo(idSpendInPeriodTitle.top)
                    start.linkTo(idSpendInPeriodTitle.end)
                },
            text = stringResource(id = com.d9tilov.android.common.android.R.string.approx_sign)
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .constrainAs(idSpendInPeriodValue) {
                    top.linkTo(isApproxSignSpendInPeriod.top)
                    bottom.linkTo(isApproxSignSpendInPeriod.bottom)
                    start.linkTo(isApproxSignSpendInPeriod.end)
                },
            text = info.wasSpendInPeriod.currency + info.wasSpendInPeriod.value
        )
    }
}

@Composable
fun InfoLabel(modifier: Modifier, text: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = Modifier.padding(6.dp),
            text = text,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun CategoryListLayout(
    categoryList: List<Category>,
    modifier: Modifier,
    onItemClicked: (Category) -> Unit,
) {
    Timber.tag(TAG).d("categoryList: $categoryList")
    val context = LocalContext.current
    LazyHorizontalGrid(
        modifier = modifier
            .height(200.dp)
            .padding(16.dp),
        rows = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        items(categoryList, { it.id }) { item ->
            Column(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.category_item_size))
                    .padding(8.dp)
                    .clickable { onItemClicked.invoke(item) },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = "Backup",
                    tint = Color(ContextCompat.getColor(context, item.color))
                )
                Text(
                    text = item.name,
                    color = Color(ContextCompat.getColor(context, item.color)),
                    overflow = TextOverflow.Ellipsis
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
            transaction = Transaction.EMPTY.copy(
                sum = BigDecimal.TEN,
                usdSum = BigDecimal(5),
                currencyCode = "RUB",
                isRegular = true,
                inStatistics = true,
                description = "description",
                category = mockCategory(2, "Cafe")
            )
        )
    }
}

@Composable
//@Preview
fun PreviewIncomeExpenseScreen() {
    MoneyManagerTheme {
        IncomeExpenseScreen(
            IncomeExpenseUiState.EMPTY.copy(
                incomeUiState = IncomeUiState(
                    incomeCategoryList = listOf(
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
                expenseUiState = ExpenseUiState.EMPTY.copy(
                    expenseInfo = ExpenseInfo(
                        ableToSpendToday = TransactionSpendingTodayPrice.NORMAL(Price("42", "$")),
                        wasSpendToday = Price("43", "$", true),
                        wasSpendInPeriod = Price("44", "$")
                    )
                )
            ), {}, {}, {}, {})
    }
}

private fun mockCategory(id: Long, name: String) = Category.EMPTY_INCOME.copy(
    id = id,
    name = name,
    icon = R.drawable.ic_category_beach,
    color = android.R.color.holo_blue_light,
)
