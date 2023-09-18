package com.d9tilov.android.incomeexpense.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.core.constants.DataConstants.TAG
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
        HorizontalPager(
            state = pagerState,
        ) { tabIndex: Int ->
            HomeContentScreen(tabIndex, uiState, Modifier.fillMaxSize())
        }
    }
}

@Composable
fun HomeContentScreen(
    tabIndex: Int,
    uiState: IncomeExpenseUiState,
    modifier: Modifier,
) {
    Column(modifier = modifier.background(Color.Blue)) {
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
@Preview
fun PreviewIncomeExpenseScreen() {
    MoneyManagerTheme {
        IncomeExpenseScreen(IncomeExpenseUiState.EMPTY)
    }
}
