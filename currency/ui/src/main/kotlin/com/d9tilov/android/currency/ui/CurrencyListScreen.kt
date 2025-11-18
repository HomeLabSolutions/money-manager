package com.d9tilov.android.currency.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.utils.CurrencyUtils
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun CurrencyListRoute(
    viewModel: CurrencyViewModel = hiltViewModel(),
    clickBack: () -> Unit,
    onChooseCurrency: (currencyCode: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CurrencyListScreen(
        currencyUiState = uiState,
        showToolbar = true,
        onChooseCurrency = { currency ->
            val code = currency.code
            viewModel.changeCurrency(code)
            onChooseCurrency(code)
        },
        onClickBack = clickBack,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSearchActiveChange = viewModel::setSearchActive,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyListScreen(
    currencyUiState: CurrencyUiState,
    modifier: Modifier = Modifier,
    showToolbar: Boolean,
    onChooseCurrency: (currency: DomainCurrency) -> Unit = {},
    onClickBack: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onSearchActiveChange: (Boolean) -> Unit = {},
) {
    val isSearchActive = (currencyUiState as? CurrencyUiState.HasCurrencies)?.isSearchActive ?: false
    val searchQuery = (currencyUiState as? CurrencyUiState.HasCurrencies)?.searchQuery ?: ""

    Scaffold(
        topBar = {
            if (showToolbar) {
                CurrencyTopBar(
                    isSearchActive = isSearchActive,
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    onSearchActiveChange = onSearchActiveChange,
                    onClickBack = onClickBack,
                )
            }
        },
    ) { padding ->
        CurrencyContent(
            currencyUiState = currencyUiState,
            searchQuery = searchQuery,
            modifier = modifier,
            padding = padding,
            onChooseCurrency = onChooseCurrency,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyTopBar(
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit,
    onClickBack: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        AnimatedContent(
            targetState = isSearchActive,
            transitionSpec = { fadeIn().togetherWith(fadeOut()) },
            label = "topbar_animation",
        ) { searching ->
            if (searching) {
                SearchTopBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    onCloseSearch = { onSearchActiveChange(false) },
                )
            } else {
                MmTopAppBar(
                    titleRes = R.string.title_prepopulate_currency,
                    navigationIcon = MoneyManagerIcons.ArrowBack,
                    actionIcon = MoneyManagerIcons.Search,
                    onNavigationClick = onClickBack,
                    onActionClick = { onSearchActiveChange(true) },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                )
            }
        }
    }
}

@Composable
private fun CurrencyContent(
    currencyUiState: CurrencyUiState,
    searchQuery: String,
    modifier: Modifier,
    padding: PaddingValues,
    onChooseCurrency: (currency: DomainCurrency) -> Unit,
) {
    if (currencyUiState.isLoading) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
        }
    }
    when (currencyUiState) {
        is CurrencyUiState.NoCurrencies -> {}
        is CurrencyUiState.HasCurrencies -> {
            val filteredList = currencyUiState.filteredCurrencyList
            val currencyList = currencyUiState.currencyList
            val state = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            LazyColumn(
                contentPadding = padding,
                modifier = modifier.consumeWindowInsets(padding),
                state = state,
            ) {
                items(items = filteredList, key = { item -> item.code }) { item ->
                    CurrencyItem(item, onChooseCurrency)
                }
            }
            LaunchedEffect(searchQuery) {
                if (searchQuery.isEmpty()) {
                    val checkedIndex = currencyList.indexOfFirst { it.isBase }
                    Timber.tag(TAG).d("Position: $checkedIndex")
                    if (checkedIndex > 0) {
                        coroutineScope.launch { state.scrollToItem(checkedIndex - 1) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldValue by remember(searchQuery) {
        mutableStateOf(
            TextFieldValue(
                text = searchQuery,
                selection = TextRange(searchQuery.length),
            ),
        )
    }

    TopAppBar(
        title = {
            TextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue.copy(selection = TextRange(newValue.text.length))
                    onSearchQueryChange(newValue.text)
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_currency_hint),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                        },
                    ),
                textStyle = MaterialTheme.typography.bodyLarge,
            )
        },
        actions = {
            IconButton(onClick = onCloseSearch) {
                Icon(
                    imageVector = MoneyManagerIcons.Close,
                    contentDescription = stringResource(R.string.close_search_description),
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun CurrencyItem(
    currency: DomainCurrency,
    clickCallback: (currency: DomainCurrency) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { clickCallback(currency) },
    ) {
        Row(
            modifier =
                Modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .height(60.dp),
        ) {
            Text(
                text = CurrencyUtils.getCurrencyIcon(currency.code),
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.headlineMedium,
            )
            Column(
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.85f),
            ) {
                Text(
                    text = CurrencyUtils.getCurrencyFullName(currency.code),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = currency.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            Icon(
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically)
                        .alpha(if (currency.isBase) 1f else 0f),
                imageVector = MoneyManagerIcons.Check,
                contentDescription = "content description",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true, name = "Currency List Screen")
@Composable
fun DefaultPreviewCurrencyItem() {
    val sampleCurrencies =
        listOf(
            DomainCurrency(
                code = "USD",
                symbol = "$",
                value = java.math.BigDecimal.ONE,
                isBase = true,
                lastUpdateTime = System.currentTimeMillis(),
            ),
            DomainCurrency(
                code = "EUR",
                symbol = "€",
                value = java.math.BigDecimal("0.85"),
                isBase = false,
                lastUpdateTime = System.currentTimeMillis(),
            ),
            DomainCurrency(
                code = "GBP",
                symbol = "£",
                value = java.math.BigDecimal("0.73"),
                isBase = false,
                lastUpdateTime = System.currentTimeMillis(),
            ),
            DomainCurrency(
                code = "JPY",
                symbol = "¥",
                value = java.math.BigDecimal("110.0"),
                isBase = false,
                lastUpdateTime = System.currentTimeMillis(),
            ),
            DomainCurrency(
                code = "RUB",
                symbol = "₽",
                value = java.math.BigDecimal("75.0"),
                isBase = false,
                lastUpdateTime = System.currentTimeMillis(),
            ),
        )

    CurrencyListScreen(
        currencyUiState =
            CurrencyUiState.HasCurrencies(
                currencyList = sampleCurrencies,
                isLoading = false,
            ),
        showToolbar = true,
    )
}

@Preview(showBackground = true, name = "Search Top Bar")
@Composable
fun SearchTopBarPreview() {
    SearchTopBar(
        searchQuery = "EUR",
        onSearchQueryChange = {},
        onCloseSearch = {},
    )
}
