package com.d9tilov.android.category.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.vm.CategoryListViewModel
import com.d9tilov.android.category.ui.vm.CategoryUiState
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.designsystem.BottomActionButton
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme

@Composable
fun CategoryListRoute(
    viewModel: CategoryListViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onCreateClicked: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoryListScreen(
        uiState = state, onBackClicked = onBackClicked, onCreateClicked = onCreateClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    uiState: CategoryUiState,
    onBackClicked: () -> Unit,
    onCreateClicked: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = R.string.title_category, onNavigationClick = onBackClicked
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
                verticalArrangement = Arrangement.Top,
            ) {
                items(uiState.categories, { it.id }) { item ->
                    Column(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.category_item_size))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = item.icon),
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
            Spacer(modifier = Modifier.weight(1f))
            BottomActionButton(
                onClick = onCreateClicked, text = stringResource(id = R.string.create)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultSettingsPreview() {
    MoneyManagerTheme {
        CategoryListScreen(CategoryUiState(
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
            )
        ), {}, {})
    }
}

private fun mockCategory(id: Long, name: String) = Category.EMPTY_INCOME.copy(
    id = id,
    name = name,
    icon = com.d9tilov.android.category_data_impl.R.drawable.ic_category_beach,
    color = android.R.color.holo_blue_light,
)