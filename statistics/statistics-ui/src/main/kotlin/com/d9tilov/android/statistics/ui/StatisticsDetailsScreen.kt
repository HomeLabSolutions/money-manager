package com.d9tilov.android.statistics.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsUiState
import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsViewModel

@Composable
fun StatisticsDetailsRoute(viewModel: StatisticsDetailsViewModel = hiltViewModel()) {
    val uiState: StatisticsDetailsUiState by viewModel.uiState.collectAsState(StatisticsDetailsUiState())
    Scaffold { paddingValues ->
        StatisticsDetailsScreen(
            modifier = Modifier.padding(paddingValues),
            state = uiState,
        )
    }
}

@Composable
fun StatisticsDetailsScreen(
    modifier: Modifier,
    state: StatisticsDetailsUiState,
) {
    System.out.println("moggot state: $state")
    Text(modifier = modifier, text = "hello")
}
