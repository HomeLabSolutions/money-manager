package com.d9tilov.moneymanager.prepopulate.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.d9tilov.moneymanager.currency.vm.CurrencyUiState

@Composable
fun CurrencyListScreen(currencyUiState: CurrencyUiState, modifier: Modifier) {
    when (currencyUiState) {
        is CurrencyUiState.NoCurrencies -> Log.d("moggot", "no list: ")
        is CurrencyUiState.HasCurrencies -> Log.d("moggot", "list: " + currencyUiState.currencyList)
    }
    Box(modifier = modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "currency")
    }
}
