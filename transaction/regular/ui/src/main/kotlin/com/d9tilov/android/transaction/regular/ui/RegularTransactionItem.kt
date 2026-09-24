package com.d9tilov.android.transaction.regular.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.domain.model.WeekDays

@Composable
fun RegularTransactionItem(
    modifier: Modifier = Modifier,
    transaction: RegularTransaction,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val contentHorizontalMargin =
        dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_medium)
    Card(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_extra_small),
                    horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_small),
                ),
    ) {
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.regular_transaction_item_height))
                    .padding(
                        horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                    ),
        ) {
            val (categoryIcon, notificationIcon, details, amount) = createRefs()
            Icon(
                modifier =
                    Modifier
                        .constrainAs(categoryIcon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }.size(
                            dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.category_item_icon_size),
                        ),
                imageVector = ImageVector.vectorResource(id = transaction.category.icon),
                contentDescription = null,
                tint = Color(ContextCompat.getColor(context, transaction.category.color)),
            )
            if (transaction.pushEnabled) {
                Icon(
                    modifier =
                        Modifier
                            .constrainAs(notificationIcon) {
                                top.linkTo(categoryIcon.top)
                                end.linkTo(categoryIcon.start)
                            }.size(
                                dimensionResource(
                                    id = com.d9tilov.android.designsystem.R.dimen.transaction_meta_icon_size,
                                ),
                            ),
                    imageVector = MoneyManagerIcons.Notifications,
                    contentDescription = stringResource(R.string.regular_transaction_notify_on_add),
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
            Column(
                modifier =
                    Modifier.constrainAs(details) {
                        start.linkTo(
                            categoryIcon.end,
                            margin = contentHorizontalMargin,
                        )
                        end.linkTo(
                            amount.start,
                            margin = contentHorizontalMargin,
                        )
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = transaction.category.name,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize =
                        dimensionResource(
                            id = com.d9tilov.android.designsystem.R.dimen.income_expense_name_text_size,
                        ).value.sp,
                    maxLines = 1,
                    color = Color(ContextCompat.getColor(context, transaction.category.color)),
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = transaction.repeatPeriodText(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            ComposeCurrencyView(
                modifier =
                    Modifier.constrainAs(amount) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                value = transaction.sum.reduceScaleStr(),
                valueStyle = MaterialTheme.typography.headlineSmall,
                symbol = transaction.currencyCode.getSymbolByCode(),
                symbolStyle = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun RegularTransaction.repeatPeriodText(): String =
    when (val period = executionPeriod) {
        is ExecutionPeriod.EveryDay -> {
            stringResource(R.string.regular_transaction_repeat_period_day)
        }

        is ExecutionPeriod.EveryWeek -> {
            stringResource(R.string.regular_transaction_repeat_period_week, weekDayText(period.dayOfWeek))
        }

        is ExecutionPeriod.EveryMonth -> {
            stringResource(R.string.regular_transaction_repeat_period_month, period.dayOfMonth.toString())
        }
    }

@Composable
private fun weekDayText(day: Int): String =
    when (day) {
        WeekDays.MONDAY.ordinal -> stringResource(R.string.regular_transaction_repeat_monday)
        WeekDays.TUESDAY.ordinal -> stringResource(R.string.regular_transaction_repeat_tuesday)
        WeekDays.WEDNESDAY.ordinal -> stringResource(R.string.regular_transaction_repeat_wednesday)
        WeekDays.THURSDAY.ordinal -> stringResource(R.string.regular_transaction_repeat_thursday)
        WeekDays.FRIDAY.ordinal -> stringResource(R.string.regular_transaction_repeat_friday)
        WeekDays.SATURDAY.ordinal -> stringResource(R.string.regular_transaction_repeat_saturday)
        WeekDays.SUNDAY.ordinal -> stringResource(R.string.regular_transaction_repeat_sunday)
        else -> throw IllegalArgumentException("Unknown day of week: $day")
    }

@Preview(showBackground = true)
@Composable
fun RegularTransactionItemPreview() {
    MoneyManagerTheme {
        RegularTransactionItem(
            onClick = {},
            transaction =
                RegularTransaction.EMPTY.copy(
                    id = 1L,
                    category =
                        Category.EMPTY_EXPENSE.copy(
                            name = "Cafe",
                            icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe,
                            color = android.R.color.holo_blue_light,
                        ),
                ),
        )
    }
}
