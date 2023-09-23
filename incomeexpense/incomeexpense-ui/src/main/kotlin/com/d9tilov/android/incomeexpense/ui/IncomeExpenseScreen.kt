package com.d9tilov.android.incomeexpense.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseUiState
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseViewModel
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.EXPENSE
import com.d9tilov.android.incomeexpense.ui.vm.ScreenType.INCOME
import com.d9tilov.android.incomeexpense.ui.vm.toScreenType
import com.d9tilov.android.incomeexpense_ui.R
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun IncomeExpenseRoute(
    viewModel: IncomeExpenseViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    IncomeExpenseScreen(uiState)
}

@Composable
fun IncomeExpenseScreen(uiState: IncomeExpenseUiState) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { Timber.tag(TAG).d("Click!") },
                modifier = Modifier
                    .navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add transaction"
                )
            }
        }
    ) { paddingValues ->
        HomeTabs(uiState = uiState, modifier = Modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeTabs(
    uiState: IncomeExpenseUiState,
    modifier: Modifier = Modifier,
) {
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
                            style = MaterialTheme.typography.body2
                        )
                    }
                )
            }
        }
        HorizontalPager(state = pagerState) { tabIndex: Int ->
            HomeContentScreen(tabIndex, uiState, Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.weight(1f))
        KeyBoardLayout(Modifier.padding(bottom = 16.dp), {}, {}, {})
    }
}

@Composable
fun HomeContentScreen(
    tabIndex: Int,
    uiState: IncomeExpenseUiState,
    modifier: Modifier,
) {
    Column {
        val title = when (tabIndex.toScreenType()) {
            EXPENSE -> "Expense"
            INCOME -> "Income"
        }
        Text(text = title, Modifier.background(Color.Red))
    }

}

@Composable
fun HomeTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface,
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
    onNumberClicked: (Int) -> Unit,
    onDotClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    val data = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "del")
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(data) { item ->
            if (item != "del") {
                ClickableText(
                    text = AnnotatedString(item),
                    style = TextStyle.Default.copy(
                        color = MaterialTheme.colors.primary,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    ),
                    onClick = {
                        if (item == ".") onDotClicked.invoke() else onNumberClicked.invoke(it)
                    }
                )
            } else {
                IconButton(onClick = onDeleteClicked) {
                    Icon(
                        painter = painterResource(id = MoneyManagerIcons.BackSpace),
                        contentDescription = "Backup",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
//@Preview
fun PreviewKeyboardLayout() {
    MoneyManagerTheme {
        KeyBoardLayout(Modifier, {}, {}, {})
    }
}


@Composable
@Preview
fun PreviewIncomeExpenseScreen() {
    MoneyManagerTheme {
        IncomeExpenseScreen(IncomeExpenseUiState.EMPTY)
    }
}
