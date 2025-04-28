package com.d9tilov.android.transaction.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.constants.UiConstants.ALPHA
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.transaction.ui.model.TransactionUiModel

@Composable
fun TransactionItem(
    modifier: Modifier,
    transaction: TransactionUiModel,
) {
    val context = LocalContext.current
    ConstraintLayout(
        modifier =
            modifier
                .height(72.dp)
                .padding(horizontal = 32.dp),
    ) {
        val (idIcon, idTitle, idDescription, idRegularIcon, idInStatisticsIcon, idSum, idDivider) = createRefs()
        Icon(
            modifier =
                Modifier
                    .constrainAs(idIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }.size(dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.category_item_icon_size)),
            imageVector = ImageVector.vectorResource(id = transaction.category.icon),
            contentDescription = "Transaction",
            tint = Color(ContextCompat.getColor(context, transaction.category.color)),
        )
        Text(
            modifier =
                Modifier
                    .padding(start = 16.dp)
                    .constrainAs(idTitle) {
                        top.linkTo(idIcon.top)
                        bottom.linkTo(idIcon.bottom)
                        start.linkTo(idIcon.end)
                    },
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
        if (transaction.description.isNotEmpty()) {
            Text(
                modifier =
                    Modifier
                        .padding(start = 16.dp, bottom = 4.dp)
                        .constrainAs(idDescription) {
                            top.linkTo(idTitle.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(idTitle.start)
                        },
                text = transaction.description,
                style = MaterialTheme.typography.bodySmall,
                fontSize =
                    dimensionResource(
                        id = com.d9tilov.android.designsystem.R.dimen.income_expense_name_description_text_size,
                    ).value.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        if (transaction.isRegular) {
            Icon(
                modifier =
                    Modifier
                        .constrainAs(idRegularIcon) {
                            top.linkTo(idIcon.top)
                            end.linkTo(idIcon.start)
                        }.size(
                            dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.transaction_meta_icon_size),
                        ),
                imageVector = MoneyManagerIcons.Repeat,
                contentDescription = "RegularTransaction",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
        if (!transaction.inStatistics) {
            Icon(
                modifier =
                    Modifier
                        .padding(top = 4.dp)
                        .constrainAs(idInStatisticsIcon) {
                            top.linkTo(idRegularIcon.bottom)
                            end.linkTo(idIcon.start)
                        }.size(
                            dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.transaction_meta_icon_size),
                        ),
                imageVector = ImageVector.vectorResource(MoneyManagerIcons.InStatisticsTransaction),
                contentDescription = "InStatisticsTransaction",
                tint = MaterialTheme.colorScheme.error,
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .constrainAs(idSum) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            ComposeCurrencyView(
                value = transaction.sum.toString(),
                valueStyle = MaterialTheme.typography.headlineSmall,
                symbol = transaction.currencyCode.getSymbolByCode(),
                symbolStyle = MaterialTheme.typography.labelLarge,
            )
            if (transaction.usdSum != null) {
                ComposeCurrencyView(
                    value = transaction.usdSum.toString(),
                    valueStyle = MaterialTheme.typography.bodyLarge,
                    symbol = DEFAULT_CURRENCY_SYMBOL,
                    symbolStyle = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier =
                Modifier
                    .alpha(ALPHA)
                    .constrainAs(idDivider) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
        )
    }
}
