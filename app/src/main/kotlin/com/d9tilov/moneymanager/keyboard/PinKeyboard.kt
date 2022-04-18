package com.d9tilov.moneymanager.keyboard

import android.content.Context
import android.util.AttributeSet
import android.widget.TableLayout

class PinKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TableLayout(context, attrs) {

    var clickPinButton: ClickPinButton? = null

    fun fireClickPinButton(button: PinButton?) {
        clickPinButton?.onPinClick(button)
    }

    interface ClickPinButton {
        fun onPinClick(button: PinButton?)
    }
}
