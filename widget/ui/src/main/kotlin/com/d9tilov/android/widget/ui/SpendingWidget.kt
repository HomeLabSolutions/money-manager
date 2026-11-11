package com.d9tilov.android.widget.ui

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.material3.ColorProviders
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.designsystem.theme.MoneyManagerColorScheme
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import kotlin.math.ceil

private const val LABEL_TEXT_SIZE = 12
private const val AMOUNT_TEXT_SIZE = 14
private const val HORIZONTAL_PADDING = 8
private const val VERTICAL_PADDING = 2
private const val AMOUNT_GAP = 4
private const val MAX_LABEL_WIDTH_FRACTION = 2f / 3f

class SpendingWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact
    override val stateDefinition: GlanceStateDefinition<*>? = null

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val state =
            (context.applicationContext as SpendingWidgetEntryPoint)
                .spendingWidgetInteractor()
                .observe()
        val initialState = state.first()
        provideContent {
            GlanceTheme(
                colors =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        GlanceTheme.colors
                    } else {
                        SpendingWidgetGlanceColorScheme.colors(context)
                    },
            ) {
                val currentState by state.collectAsState(initial = initialState)
                SpendingWidgetContent(currentState)
            }
        }
    }
}

@Composable
internal fun SpendingWidgetContent(state: SpendingWidgetState) {
    val context = LocalContext.current
    val openApp =
        checkNotNull(
            context.packageManager.getLaunchIntentForPackage(context.packageName),
        ) {
            "Money Manager must declare a launcher activity"
        }.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    Box(
        modifier =
            GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.background)
                .clickable(actionStartActivity(openApp))
                .padding(horizontal = HORIZONTAL_PADDING.dp, vertical = VERTICAL_PADDING.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (state is SpendingWidgetState.Ready) {
            SpendingAmounts(state)
        } else {
            Text(
                text = context.getString(messageFor(state)),
                style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = LABEL_TEXT_SIZE.sp),
            )
        }
    }
}

@Composable
private fun SpendingAmounts(state: SpendingWidgetState.Ready) {
    val summary = state.spendingSummary
    val availableToday =
        when (val amount = summary.availableToday) {
            is TransactionSpendingTodayModel.NORMAL -> amount.trSum
            is TransactionSpendingTodayModel.OVERSPENDING -> amount.trSum
        }
    val overspending = availableToday.signum() < 0
    val context = LocalContext.current
    val availableLabel = if (overspending) R.string.widget_overspending else R.string.widget_available_today
    Column(GlanceModifier.fillMaxSize()) {
        SpendingRow(
            label = context.getString(availableLabel),
            amount = availableToday,
            currencyCode = state.spendingSummary.currency.code,
            color = if (overspending) GlanceTheme.colors.error else GlanceTheme.colors.primary,
        )
        SpendingRow(
            label = context.getString(R.string.widget_spent_today),
            amount = summary.spentToday,
            currencyCode = state.spendingSummary.currency.code,
        )
        SpendingRow(
            label = context.getString(R.string.widget_spent_period),
            amount = summary.spentInPeriod,
            currencyCode = state.spendingSummary.currency.code,
        )
    }
}

@Composable
private fun ColumnScope.SpendingRow(
    label: String,
    amount: BigDecimal,
    currencyCode: String,
    color: ColorProvider = GlanceTheme.colors.onSurface,
) {
    val value = "${currencyCode.getSymbolByCode()}${amount.reduceScaleStr()}"
    val context = LocalContext.current
    // Reserve a pixel-rounding margin when Glance measures the styled amount independently.
    val valueWidth = textWidth(context, value, AMOUNT_TEXT_SIZE.toFloat(), bold = true) + 1.dp
    val availableWidth =
        (LocalSize.current.width - (HORIZONTAL_PADDING * 2 + AMOUNT_GAP).dp - valueWidth).coerceAtLeast(0.dp)
    val maxLabelWidth = minOf(LocalSize.current.width * MAX_LABEL_WIDTH_FRACTION, availableWidth)
    val labelWidth = minOf(textWidth(context, label, LABEL_TEXT_SIZE.toFloat()), maxLabelWidth)
    Row(
        modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = GlanceModifier.width(labelWidth),
            style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = LABEL_TEXT_SIZE.sp),
            maxLines = 1,
        )
        Spacer(GlanceModifier.width(AMOUNT_GAP.dp))
        Text(
            text = value,
            style = TextStyle(color = color, fontSize = AMOUNT_TEXT_SIZE.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
        )
    }
}

private object SpendingWidgetGlanceColorScheme {
    fun colors(context: Context) =
        ColorProviders(
            light = MoneyManagerColorScheme.light(context),
            dark = MoneyManagerColorScheme.dark(context),
        )
}

private fun textWidth(
    context: Context,
    text: String,
    textSizeSp: Float,
    bold: Boolean = false,
): Dp {
    val metrics = context.resources.displayMetrics
    val paint =
        Paint().apply {
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeSp, metrics)
            typeface = if (bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
    return (ceil(paint.measureText(text)) / metrics.density).dp
}

private fun messageFor(state: SpendingWidgetState): Int =
    when (state) {
        SpendingWidgetState.SignedOut -> R.string.widget_sign_in
        SpendingWidgetState.SetupRequired -> R.string.widget_setup_required
        SpendingWidgetState.PremiumRequired -> R.string.widget_premium_required
        SpendingWidgetState.Unavailable -> R.string.widget_unavailable
        is SpendingWidgetState.Ready -> error("Ready state displays amounts")
    }
