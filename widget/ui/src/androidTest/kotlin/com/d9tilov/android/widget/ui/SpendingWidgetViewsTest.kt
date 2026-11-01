package com.d9tilov.android.widget.ui

import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.LocaleList
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.view.children
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.d9tilov.android.transaction.domain.model.SpendingSummary
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.math.BigDecimal
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class SpendingWidgetViewsTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val size = DpSize(179.dp, 96.dp)
    private val ready =
        SpendingWidgetState.Ready(
            "RUB",
            SpendingSummary(
                TransactionSpendingTodayModel.NORMAL(BigDecimal("125.50")),
                BigDecimal("24.75"),
                BigDecimal("975.25"),
            ),
        )

    @Test
    fun amountsRenderInBothLanguagesAndThemes() =
        runTest {
            for (language in listOf("en", "ru")) {
                for (night in listOf(false, true)) {
                    val context = localizedContext(language, night)
                    val preview =
                        render(context, ready) { view ->
                            assertReadyAmounts(context, view)
                            assertLocalizedLabels(language, view)
                        }
                    val theme = if (night) "dark" else "light"
                    savePreview(context, preview, "widget-$language-$theme.png")
                }
            }
        }

    @Test
    fun overspendingUsesNegativeAmountAndErrorColor() =
        runTest {
            val context = localizedContext("ru", false)
            render(
                context,
                ready.copy(
                    spendingSummary =
                        ready.spendingSummary.copy(
                            availableToday = TransactionSpendingTodayModel.OVERSPENDING(BigDecimal("-12.25")),
                        ),
                ),
            ) { view ->
                assertEquals("перерасход:", text(view, "перерасход:").text.toString())
                assertEquals(context.getColor(R.color.widget_overspending), text(view, "₽-12.25").currentTextColor)
            }
        }

    @Test
    fun signingOutClearsPreviouslyDisplayedAmounts() =
        runTest {
            val context = localizedContext("en", false)
            val (readyViews, signedOutViews) =
                withContext(Dispatchers.Default) {
                    SpendingWidgetViews.create(context, ready, size) to
                        SpendingWidgetViews.create(context, SpendingWidgetState.SignedOut, size)
                }
            instrumentation.runOnMainSync {
                val host = AppWidgetHostView(context)
                host.setAppWidget(0, provider(context))
                host.updateAppWidget(readyViews)
                assertTrue(textViews(host).any { it.text.toString() == "₽125.5" })
                host.updateAppWidget(signedOutViews)
                val contents = textViews(host).map { it.text.toString() }
                assertTrue("Open Money Manager to sign in" in contents)
                assertFalse(contents.any { it in listOf("₽125.5", "₽24.75", "₽975.25") })
            }
        }

    @Test
    fun sizingConstraintsAllowTwoByOneCells() {
        val context = instrumentation.targetContext
        val provider = provider(context)
        val density = context.resources.displayMetrics.density
        val width = (size.width.value * density).toInt()
        val height = (size.height.value * density).toInt()
        assertTrue("Default width must fit two cells", provider.minWidth <= width)
        assertTrue("Default height must fit one cell", provider.minHeight <= height)
        assertTrue("Minimum resize width must fit two cells", provider.minResizeWidth <= width)
        assertTrue("Minimum resize height must fit one cell", provider.minResizeHeight <= height)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            assertEquals(2, provider.targetCellWidth)
            assertEquals(1, provider.targetCellHeight)
        }
    }

    @Test
    fun twoByOneWidgetSizeFitsAmountsWithRequestedFontsAndGap() =
        runTest {
            for (language in listOf("en", "ru")) {
                val context = localizedContext(language, false)
                render(context, ready) { view ->
                    val labels =
                        listOf(
                            R.string.widget_available_today,
                            R.string.widget_spent_today,
                            R.string.widget_spent_period,
                        )
                    val values = listOf("₽125.5", "₽24.75", "₽975.25")
                    for ((labelId, value) in labels.zip(values)) {
                        val label = text(view, context.getString(labelId))
                        val amount = text(view, value)
                        assertTrue("Full home screen labels must fit", label.layout.height <= label.height)
                        for (line in 0 until label.lineCount) {
                            assertEquals(
                                "Home screen labels must not be truncated",
                                0,
                                label.layout.getEllipsisCount(line),
                            )
                        }
                        assertEquals("Amount must fit on one line", 0, amount.layout.getEllipsisCount(0))
                        assertTrue("Amount height must fit", amount.height >= amount.lineHeight)
                        assertEquals(spToPixels(context, 12f), label.textSize, 0.1f)
                        assertEquals(spToPixels(context, 14f), amount.textSize, 0.1f)
                        val labelBounds = bounds(view, label)
                        val amountBounds = bounds(view, amount)
                        assertEquals(
                            4 * context.resources.displayMetrics.density,
                            (amountBounds.left - labelBounds.right).toFloat(),
                            1f,
                        )
                        assertTrue("Amount must fit inside the widget", amountBounds.right <= view.width)
                        assertTrue("Amount must not be clipped vertically", amountBounds.bottom <= view.height)
                    }
                }
            }
        }

    @Test
    fun widerWidgetKeepsAmountsNextToLabels() =
        runTest {
            val context = localizedContext("en", false)
            render(context, ready, DpSize(280.dp, 180.dp)) { view ->
                val label = bounds(view, text(view, context.getString(R.string.widget_spent_today)))
                val amount = bounds(view, text(view, "₽24.75"))
                assertEquals(4 * context.resources.displayMetrics.density, (amount.left - label.right).toFloat(), 1f)
            }
        }

    @UiThread
    private fun assertReadyAmounts(
        context: Context,
        view: View,
    ) {
        for (value in listOf("₽125.5", "₽24.75", "₽975.25")) {
            assertEquals(value, text(view, value).text.toString())
        }
        assertEquals(context.getColor(R.color.widget_accent), text(view, "₽125.5").currentTextColor)
    }

    @UiThread
    private fun assertLocalizedLabels(
        language: String,
        view: View,
    ) {
        val expected =
            if (language == "ru") {
                listOf("потрачено сегодня:", "потрачено за период:", "можно потратить сегодня:")
            } else {
                listOf("spent today:", "spent in period:", "can spend today:")
            }
        for (label in expected) {
            assertEquals(label, text(view, label).text.toString())
        }
    }

    @WorkerThread
    private suspend fun render(
        context: Context,
        state: SpendingWidgetState,
        widgetSize: DpSize = size,
        check: (View) -> Unit,
    ): Bitmap {
        val views = withContext(Dispatchers.Default) { SpendingWidgetViews.create(context, state, widgetSize) }
        val bitmap = AtomicReference<Bitmap>()
        instrumentation.runOnMainSync {
            val view = views.apply(context, FrameLayout(context))
            val density = context.resources.displayMetrics.density
            view.measure(
                View.MeasureSpec.makeMeasureSpec((widgetSize.width.value * density).toInt(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec((widgetSize.height.value * density).toInt(), View.MeasureSpec.EXACTLY),
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            check(view)
            bitmap.set(createBitmap(view.width, view.height).also { view.draw(Canvas(it)) })
        }
        return requireNotNull(bitmap.get())
    }

    private fun textViews(view: View): List<TextView> =
        when (view) {
            is TextView -> listOf(view)
            is ViewGroup -> view.children.flatMap { textViews(it) }.toList()
            else -> emptyList()
        }

    private fun text(
        view: View,
        value: String,
    ): TextView = textViews(view).single { it.text.toString() == value }

    private fun bounds(
        root: View,
        view: View,
    ): Rect =
        Rect(0, 0, view.width, view.height).apply { (root as ViewGroup).offsetDescendantRectToMyCoords(view, this) }

    private fun spToPixels(
        context: Context,
        value: Float,
    ): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.resources.displayMetrics)

    private fun provider(context: Context): AppWidgetProviderInfo {
        val component = ComponentName(context, SpendingWidgetReceiver::class.java)
        return AppWidgetManager.getInstance(context).installedProviders.single { it.provider == component }
    }

    @WorkerThread
    private fun savePreview(
        context: Context,
        bitmap: Bitmap,
        name: String,
    ) {
        val directory = requireNotNull(context.getExternalFilesDir("widget-test-previews"))
        File(directory, name).outputStream().use { stream -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) }
    }

    private fun localizedContext(
        language: String,
        night: Boolean,
    ): Context {
        val configuration =
            Configuration(instrumentation.targetContext.resources.configuration).apply {
                setLocales(LocaleList(Locale.forLanguageTag(language)))
                uiMode = if (night) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
            }
        return instrumentation.targetContext.createConfigurationContext(configuration)
    }
}
