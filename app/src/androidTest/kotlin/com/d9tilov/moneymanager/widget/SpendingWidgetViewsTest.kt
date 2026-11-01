package com.d9tilov.moneymanager.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.LocaleList
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.graphics.createBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.d9tilov.moneymanager.R
import org.junit.Assert.assertEquals
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
    private val ready =
        SpendingWidgetState.Ready("RUB", BigDecimal("125.50"), BigDecimal("24.75"), BigDecimal("975.25"))

    @Test
    fun amountsRenderInBothLanguagesAndThemes() {
        for (language in listOf("en", "ru")) {
            for (night in listOf(false, true)) {
                val context = localizedContext(language, night)
                val preview = AtomicReference<Bitmap>()
                instrumentation.runOnMainSync {
                    val view = SpendingWidgetViews.create(context, ready).apply(context, FrameLayout(context))
                    assertReadyAmounts(context, view)
                    assertLocalizedLabels(language, view)
                    preview.set(renderPreview(context, view))
                }
                val theme = if (night) "dark" else "light"
                savePreview(context, requireNotNull(preview.get()), "widget-$language-$theme.png")
            }
        }
    }

    @Test
    fun overspendingUsesNegativeAmountAndErrorColor() {
        instrumentation.runOnMainSync {
            val context = localizedContext("ru", false)
            val state = ready.copy(availableToday = BigDecimal("-12.25"))
            val view = SpendingWidgetViews.create(context, state).apply(context, FrameLayout(context))
            assertEquals("перерасход:", view.findViewById<TextView>(R.id.widget_available_label).text.toString())
            assertEquals("₽-12.25", view.findViewById<TextView>(R.id.widget_available_value).text.toString())
            assertEquals(
                context.getColor(R.color.widget_overspending),
                view.findViewById<TextView>(R.id.widget_available_value).currentTextColor,
            )
        }
    }

    @Test
    fun signingOutClearsPreviouslyDisplayedAmounts() {
        instrumentation.runOnMainSync {
            val context = localizedContext("en", false)
            val view = SpendingWidgetViews.create(context, ready).apply(context, FrameLayout(context))
            SpendingWidgetViews.create(context, SpendingWidgetState.SignedOut).reapply(context, view)
            assertEquals(View.GONE, view.findViewById<View>(R.id.widget_amounts).visibility)
            assertEquals("—", view.findViewById<TextView>(R.id.widget_available_value).text.toString())
            assertEquals("—", view.findViewById<TextView>(R.id.widget_today_value).text.toString())
            assertEquals("—", view.findViewById<TextView>(R.id.widget_period_value).text.toString())
            assertEquals(
                "Open Money Manager to sign in",
                view.findViewById<TextView>(R.id.widget_message).text.toString(),
            )
        }
    }

    @Test
    fun minimumWidgetSizeFitsAmounts() {
        instrumentation.runOnMainSync {
            val context = localizedContext("ru", false)
            val view = SpendingWidgetViews.create(context, ready).apply(context, FrameLayout(context))
            val density = context.resources.displayMetrics.density
            view.measure(
                View.MeasureSpec.makeMeasureSpec((180 * density).toInt(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec((96 * density).toInt(), View.MeasureSpec.EXACTLY),
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            for (id in listOf(R.id.widget_available_label, R.id.widget_today_label, R.id.widget_period_label)) {
                val label = view.findViewById<TextView>(id)
                assertTrue("Full home screen labels must fit", label.layout.height <= label.height)
                for (line in 0 until label.lineCount) {
                    assertEquals("Home screen labels must not be truncated", 0, label.layout.getEllipsisCount(line))
                }
            }
            val amounts = view.findViewById<View>(R.id.widget_amounts)
            val period = view.findViewById<TextView>(R.id.widget_period_value)
            val periodBounds = Rect(0, 0, period.width, period.height)
            (view as ViewGroup).offsetDescendantRectToMyCoords(period, periodBounds)
            assertTrue("Amounts must be visible at the minimum widget size", amounts.height > 0)
            assertTrue("Period amount must fit inside the widget", period.height >= period.lineHeight)
            assertTrue("Period amount must not be clipped", periodBounds.bottom <= view.height - view.paddingBottom)
            assertTrue("Period amount must fit on one line", period.layout.getEllipsisCount(0) == 0)
        }
    }

    @UiThread
    private fun assertReadyAmounts(
        context: Context,
        view: View,
    ) {
        assertEquals("₽125.5", view.findViewById<TextView>(R.id.widget_available_value).text.toString())
        assertEquals("₽24.75", view.findViewById<TextView>(R.id.widget_today_value).text.toString())
        assertEquals("₽975.25", view.findViewById<TextView>(R.id.widget_period_value).text.toString())
        assertEquals(View.GONE, view.findViewById<View>(R.id.widget_message).visibility)
        assertEquals(
            context.getColor(R.color.widget_accent),
            view.findViewById<TextView>(R.id.widget_available_value).currentTextColor,
        )
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
        val ids =
            listOf(R.id.widget_today_label, R.id.widget_period_label, R.id.widget_available_label)
        for ((id, label) in ids.zip(expected)) {
            assertEquals(label, view.findViewById<TextView>(id).text.toString())
        }
    }

    @UiThread
    private fun renderPreview(
        context: Context,
        view: View,
    ): Bitmap {
        val density = context.resources.displayMetrics.density
        view.measure(
            View.MeasureSpec.makeMeasureSpec((180 * density).toInt(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec((96 * density).toInt(), View.MeasureSpec.EXACTLY),
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = createBitmap(view.width, view.height)
        view.draw(Canvas(bitmap))
        return bitmap
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
        val context = instrumentation.targetContext
        val configuration =
            Configuration(context.resources.configuration).apply {
                setLocales(LocaleList(Locale.forLanguageTag(language)))
                uiMode = if (night) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
            }
        return context.createConfigurationContext(configuration)
    }
}
