package com.d9tilov.moneymanager.core.ui.widget.currencyview

import android.content.Context
import android.util.AttributeSet
import android.widget.TableLayout
import android.widget.TextView

class PinKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TableLayout(context, attrs) {
    private lateinit var clickPinButton: ClickPinButton

    fun setClickPinButton(click: ClickPinButton) {
        clickPinButton = click
    }

    fun fireClickPinButton(button: PinButton) {
        clickPinButton.onPinClick(button)
    }

    interface ClickPinButton {
        fun onPinClick(pinButton: PinButton)
    }
}
