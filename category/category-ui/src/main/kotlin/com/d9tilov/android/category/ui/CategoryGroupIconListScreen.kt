package com.d9tilov.android.category.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.model.CategoryGroupItem
import com.d9tilov.android.category.ui.vm.CategoryGroupSetViewModel
import com.d9tilov.android.category.ui.vm.CategoryIconListUiState
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme

@Composable
fun CategoryGroupIconListRoute(
    viewModel: CategoryGroupSetViewModel = hiltViewModel(),
    onItemClick: (CategoryGroupItem) -> Unit,
    clickBack: () -> Unit,
) {
    val state: CategoryIconListUiState by viewModel.uiState.collectAsStateWithLifecycle()
    CategoryGroupIconListScreen(state = state, onBackClicked = clickBack, onItemClick = onItemClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryGroupIconListScreen(
    state: CategoryIconListUiState,
    onBackClicked: () -> Unit,
    onItemClick: (CategoryGroupItem) -> Unit,
) {
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = R.string.title_category_group_set,
            onNavigationClick = onBackClicked
        )
    }) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = state.groups, key = { item -> item.groupId }) { item ->
                CategoryIconListItem(item, onItemClick)
            }
        }
    }
}

@Composable
fun CategoryIconListItem(
    group: CategoryGroupItem,
    clickCallback: (group: CategoryGroupItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.category_group_item_height))
            .fillMaxWidth()
            .clickable { clickCallback(group) },
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large)),
            text = stringResource(id = group.name),

            )
        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier = Modifier.alpha(0.2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCategoryIconListPreview() {
    MoneyManagerTheme {
        CategoryGroupIconListScreen(CategoryIconListUiState(), {}, {})
    }
}
