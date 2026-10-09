package com.d9tilov.android.incomeexpense.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo
import com.d9tilov.android.incomeexpense.ui.vm.ExpenseUiState
import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseUiState
import com.d9tilov.android.incomeexpense.ui.vm.IncomeUiState
import com.d9tilov.android.incomeexpense.ui.vm.Price
import com.d9tilov.android.transaction.ui.TransactionItem
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import java.math.BigDecimal

private const val PREVIEW_USD_AMOUNT = 5
private const val PREVIEW_CATEGORY_COUNT = 15

@Composable
@Preview
fun TransactionListItemPreview() {
    MoneyManagerTheme {
        TransactionItem(
            modifier = Modifier.fillMaxWidth(),
            transaction =
                TransactionUiModel.EMPTY.copy(
                    sum = BigDecimal.TEN,
                    usdSum = BigDecimal(PREVIEW_USD_AMOUNT),
                    currencyCode = "RUB",
                    isRegular = true,
                    inStatistics = true,
                    description = "description",
                    category = mockCategory(2, "Cafe"),
                ),
        )
    }
}

@Composable
@Preview
fun PreviewIncomeExpenseScreen() {
    MoneyManagerTheme {
        IncomeExpenseScreen(
            uiState =
                IncomeExpenseUiState.EMPTY.copy(
                    incomeUiState =
                        IncomeUiState(
                            incomeCategoryList =
                                List(PREVIEW_CATEGORY_COUNT) { index ->
                                    val id = (index + 1).toLong()
                                    mockCategory(id, "Category$id")
                                },
                        ),
                    expenseUiState =
                        ExpenseUiState.EMPTY.copy(
                            expenseInfo =
                                ExpenseInfo(
                                    ableToSpendToday =
                                        Price(
                                            R.string.expense_info_can_spend_today_title,
                                            "$42",
                                        ),
                                    wasSpendToday = Price(R.string.expense_info_today_title, "$43"),
                                    wasSpendInPeriod = Price(R.string.expense_info_period_title, "$44"),
                                ),
                        ),
                ),
            onNumberClicked = {},
            onCategoryClicked = {},
            onEditModeChanged = {},
            onCurrencyClicked = {},
            onAllCategoryClicked = { _, _ -> },
            onTransactionClicked = {},
            onDeleteTransactionConfirmClicked = {},
            onScreenTypeClicked = {},
        )
    }
}

@Composable
@Preview
fun InfoLabelPreview() {
    MoneyManagerTheme {
        ExpenseInfoBlock(
            Modifier,
            ExpenseInfo(
                ableToSpendToday = Price(R.string.expense_info_can_spend_today_title, "$41"),
                wasSpendToday = Price(R.string.expense_info_today_title, "$42"),
                wasSpendInPeriod = Price(R.string.expense_info_period_title, "$43"),
            ),
        )
    }
}

private fun mockCategory(
    id: Long,
    name: String,
) = Category.EMPTY_INCOME.copy(
    id = id,
    name = name,
    icon = R.drawable.ic_category_beach,
    color = android.R.color.holo_blue_light,
)
