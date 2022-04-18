package com.d9tilov.moneymanager.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout

class PinButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        val pinButton = this
        setOnClickListener {
            val group = parent as ViewGroup
            val keyboard = group.parent as PinKeyboard
            keyboard.fireClickPinButton(pinButton)
        }
    }
}
