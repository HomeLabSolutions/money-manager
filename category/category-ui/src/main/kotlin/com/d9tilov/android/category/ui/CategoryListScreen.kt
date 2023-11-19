package com.d9tilov.android.category.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.vm.CategoryListViewModel
import com.d9tilov.android.category.ui.vm.CategoryUiState
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.BottomActionButton
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun CategoryListRoute(
    viewModel: CategoryListViewModel = hiltViewModel(),
    openCategory: (Long, TransactionType) -> Unit,
    clickBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoryListScreen(
        uiState = state,
        onBackClicked = clickBack,
        onCategoryClicked = { category ->
            openCategory.invoke(
                category.id,
                viewModel.transactionType
            )
        },
        onCreateClicked = { openCategory.invoke(NO_ID, viewModel.transactionType) },
        onRemoveClicked = { category -> viewModel.remove(category) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryListScreen(
    uiState: CategoryUiState,
    onBackClicked: () -> Unit,
    onCreateClicked: () -> Unit,
    onCategoryClicked: (Category) -> Unit,
    onRemoveClicked: (Category) -> Unit,
) {
    val context = LocalContext.current
    val shake = remember { Animatable(0f) }
    var isRemoveState by remember { mutableStateOf(false) }
    BackHandler {
        if (isRemoveState) isRemoveState = false
        else onBackClicked.invoke()
    }
    LaunchedEffect(isRemoveState) {
        var i = 0
        while (isRemoveState) {
            when (i % 2) {
                0 -> shake.animateTo(2f, spring(stiffness = 5_000f))
                else -> shake.animateTo(-2f, spring(stiffness = 5_000f))
            }
            ++i
            if (i == 2) i = 0
        }
        shake.animateTo(0f)
    }
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = R.string.title_category,
            onNavigationClick = onBackClicked
        )
    }) { padding ->
        val openAlertDialog = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                columns = GridCells.Fixed(4),
            ) {
                items(uiState.categories, { it.id }) { item ->
                    Box {
                        if (isRemoveState) {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp),
                                imageVector = ImageVector.vectorResource(MoneyManagerIcons.Cross),
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = ""
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .offset {
                                    IntOffset(
                                        x = shake.value.roundToInt() + Random.nextInt(5),
                                        y = shake.value.roundToInt() + Random.nextInt(5)
                                    )
                                }
                                .combinedClickable(
                                    onClick = {
                                        if (isRemoveState) openAlertDialog.value = true
                                        else onCategoryClicked.invoke(item)
                                        isRemoveState = false
                                    },
                                    onLongClick = { isRemoveState = true },
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.category_creation_item_size)),
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
                    SimpleDialog(
                        show = openAlertDialog.value,
                        title = stringResource(R.string.category_delete_title),
                        subtitle = stringResource(R.string.category_delete_subtitle, item.name),
                        dismissButton = stringResource(com.d9tilov.android.common.android.R.string.cancel),
                        confirmButton = stringResource(com.d9tilov.android.common.android.R.string.delete),
                        onConfirm = {
                            openAlertDialog.value = false
                            onRemoveClicked.invoke(item)
                        },
                        onDismiss = { openAlertDialog.value = false }
                    )
                }
            }
            BottomActionButton(
                onClick = onCreateClicked,
                text = stringResource(id = R.string.create)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCategoryListPreview() {
    MoneyManagerTheme {
        CategoryListScreen(
            CategoryUiState(
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
                    mockCategory(16L, "Category15"),
                    mockCategory(17L, "Category15"),
                    mockCategory(18L, "Category15"),
                    mockCategory(19L, "Category15"),
                    mockCategory(20L, "Category15"),
                    mockCategory(21L, "Category15"),
                    mockCategory(22L, "Category15"),
                    mockCategory(23L, "Category15"),
                    mockCategory(24L, "Category15"),
                    mockCategory(25L, "Category15"),
                    mockCategory(26L, "Category15"),
                    mockCategory(27L, "Category15"),
                    mockCategory(28L, "Category15"),
                    mockCategory(29L, "Category15"),
                    mockCategory(30L, "Category15"),
                    mockCategory(31L, "Category15"),
                    mockCategory(32L, "Category15"),
                    mockCategory(33L, "Category15"),
                    mockCategory(34L, "Category15"),
                )
            ), {}, {}, {}, {})
    }
}

private fun mockCategory(id: Long, name: String) = Category.EMPTY_INCOME.copy(
    id = id,
    name = name,
    icon = com.d9tilov.android.category_data_impl.R.drawable.ic_category_beach,
    color = android.R.color.holo_blue_light,
)