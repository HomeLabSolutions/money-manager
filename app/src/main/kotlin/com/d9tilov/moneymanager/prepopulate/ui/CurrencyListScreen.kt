package com.d9tilov.moneymanager.prepopulate.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.vm.CurrencyUiState
import kotlinx.coroutines.launch

@Composable
fun CurrencyListScreen(
    currencyUiState: CurrencyUiState,
    modifier: Modifier,
    clickCallback: (currency: DomainCurrency) -> Unit
) {
    if (currencyUiState.isLoading) {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    when (currencyUiState) {
        is CurrencyUiState.NoCurrencies -> {}
        is CurrencyUiState.HasCurrencies -> {
            val currencyList = currencyUiState.currencyList
            val state = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            LazyColumn(contentPadding = PaddingValues(16.dp), modifier = modifier, state = state) {
                items(items = currencyList, key = { item -> item.code }) { item ->
                    CurrencyItem(item, clickCallback)
                }
            }
            LaunchedEffect(true) {
                val checkedIndex = currencyList.indexOfFirst { it.isBase }
                if (checkedIndex == -1) return@LaunchedEffect
                coroutineScope.launch { state.scrollToItem(checkedIndex) }
            }
        }
    }
}

@Composable
fun CurrencyItem(currency: DomainCurrency, clickCallback: (currency: DomainCurrency) -> Unit) {
    Column(Modifier.fillMaxWidth().clickable { clickCallback(currency) }) {
        Row(
            modifier = Modifier.wrapContentHeight(Alignment.CenterVertically).fillMaxWidth()
                .height(60.dp),
        ) {
            Text(
                text = CurrencyUtils.getCurrencyIcon(currency.code),
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 16.dp),
                fontSize = 30.sp
            )
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth(0.85f)
            ) {
                Text(
                    text = CurrencyUtils.getCurrencyFullName(currency.code),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = currency.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically)
                    .alpha(if (currency.isBase) 1f else 0f),
                imageVector = Icons.Default.Check,
                contentDescription = "content description",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier = Modifier.alpha(0.2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewCurrencyItem() {
    CurrencyItem(DomainCurrency.EMPTY, clickCallback = {})
}