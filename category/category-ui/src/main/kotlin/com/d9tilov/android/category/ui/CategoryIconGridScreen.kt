package com.d9tilov.android.category.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.ui.vm.CategoryIconGridUiState
import com.d9tilov.android.category.ui.vm.CategoryIconGridViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme

@Composable
fun CategoryIconGridRoute(
    viewModel: CategoryIconGridViewModel = hiltViewModel(),
    onIconClick: (Int) -> Unit,
    clickBack: () -> Unit,
) {
    val state: CategoryIconGridUiState by viewModel.uiState.collectAsStateWithLifecycle()
    CategoryIconGridScreen(uiState = state, onBackClicked = clickBack, onIconClicked = onIconClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryIconGridScreen(
    uiState: CategoryIconGridUiState,
    onIconClicked: (Int) -> Unit,
    onBackClicked: () -> Unit,
) {
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = uiState.title,
            onNavigationClick = onBackClicked
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp),
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.Start,
                verticalArrangement = Arrangement.Top
            ) {
                items(uiState.icons, { it }) { id ->
                    Icon(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.category_grid_item_size))
                            .padding(8.dp)
                            .clickable { onIconClicked.invoke(id) },
                        imageVector = ImageVector.vectorResource(id = id),
                        contentDescription = "Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCategoryIconGridPreview() {
    MoneyManagerTheme {
        CategoryIconGridScreen(
            CategoryIconGridUiState(
                R.string.category_group_free,
                listOf(
                    com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity,
                    com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity2,
                    com.d9tilov.android.category_data_impl.R.drawable.ic_category_electricity3,
                    com.d9tilov.android.category_data_impl.R.drawable.ic_category_garbage,
                    com.d9tilov.android.category_data_impl.R.drawable.ic_category_home,
                    com.d9tilov.android.category_data_impl.R.drawable.ic_category_house
                )
            ), {}, {})
    }
}
