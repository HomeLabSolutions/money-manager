package com.d9tilov.android.widget.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
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
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import kotlin.math.ceil
import androidx.glance.color.ColorProvider as DayNightColorProvider

private const val LABEL_TEXT_SIZE = 12
private const val AMOUNT_TEXT_SIZE = 14
private const val HORIZONTAL_PADDING = 8
private const val VERTICAL_PADDING = 2
private const val AMOUNT_GAP = 4

class SpendingWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact
    override val stateDefinition: GlanceStateDefinition<*>? = null

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val state = spendingWidgetState(context)
        val initialState = withContext(Dispatchers.IO) { state.first() }
        provideContent {
            val currentState by state.collectAsState(initial = initialState)
            SpendingWidgetContent(currentState)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
internal fun spendingWidgetState(context: Context): Flow<SpendingWidgetState> {
    val dependencies =
        EntryPointAccessors.fromApplication<SpendingWidgetEntryPoint>(context)
    val interactor = dependencies.spendingWidgetInteractor()
    return dependencies
        .preferencesStore()
        .uid
        .flatMapLatest(interactor::observe)
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface SpendingWidgetEntryPoint {
    fun preferencesStore(): PreferencesStore

    fun spendingWidgetInteractor(): SpendingWidgetInteractor
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
                .background(ImageProvider(R.drawable.widget_background))
                .clickable(actionStartActivity(openApp))
                .padding(horizontal = HORIZONTAL_PADDING.dp, vertical = VERTICAL_PADDING.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        if (state is SpendingWidgetState.Ready) {
            SpendingAmounts(state)
        } else {
            Text(
                text = context.getString(messageFor(state)),
                style = TextStyle(color = widgetColor(R.color.widget_text_secondary), fontSize = LABEL_TEXT_SIZE.sp),
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
            currencyCode = state.currencyCode,
            color = widgetColor(if (overspending) R.color.widget_overspending else R.color.widget_accent),
        )
        SpendingRow(
            label = context.getString(R.string.widget_spent_today),
            amount = summary.spentToday,
            currencyCode = state.currencyCode,
        )
        SpendingRow(
            label = context.getString(R.string.widget_spent_period),
            amount = summary.spentInPeriod,
            currencyCode = state.currencyCode,
        )
    }
}

@Composable
private fun ColumnScope.SpendingRow(
    label: String,
    amount: BigDecimal,
    currencyCode: String,
    color: ColorProvider = widgetColor(R.color.widget_text),
) {
    val value = "${currencyCode.getSymbolByCode()}${amount.reduceScaleStr()}"
    val context = LocalContext.current
    // Reserve a pixel-rounding margin when Glance measures the styled amount independently.
    val valueWidth = textWidth(context, value, AMOUNT_TEXT_SIZE.toFloat(), bold = true) + 1.dp
    val availableWidth =
        (LocalSize.current.width - (HORIZONTAL_PADDING * 2 + AMOUNT_GAP).dp - valueWidth).coerceAtLeast(0.dp)
    val labelWidth = minOf(textWidth(context, label, LABEL_TEXT_SIZE.toFloat()), availableWidth)
    Row(
        modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = GlanceModifier.width(labelWidth),
            style = TextStyle(color = widgetColor(R.color.widget_text_secondary), fontSize = LABEL_TEXT_SIZE.sp),
            maxLines = 2,
        )
        Spacer(GlanceModifier.width(AMOUNT_GAP.dp))
        Text(
            text = value,
            style = TextStyle(color = color, fontSize = AMOUNT_TEXT_SIZE.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
        )
    }
}

@Composable
private fun widgetColor(
    @ColorRes resource: Int,
): ColorProvider {
    val context = LocalContext.current

    fun themedColor(nightMode: Int): Color {
        val configuration = Configuration(context.resources.configuration)
        configuration.uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or nightMode
        return Color(context.createConfigurationContext(configuration).getColor(resource))
    }
    return DayNightColorProvider(
        day = themedColor(Configuration.UI_MODE_NIGHT_NO),
        night = themedColor(Configuration.UI_MODE_NIGHT_YES),
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
