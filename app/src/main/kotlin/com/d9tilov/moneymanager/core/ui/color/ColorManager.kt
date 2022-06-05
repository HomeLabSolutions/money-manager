package com.d9tilov.moneymanager.core.ui.color

import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.toMillis
import java.util.Random

object ColorManager {

    val colorList = listOf(
        R.color.category_yellow,
        R.color.category_light_green,
        R.color.category_green,
        R.color.category_light_blue,
        R.color.category_blue,
        R.color.category_violet,
        R.color.category_dark_red,
        R.color.category_dark_orange,
        R.color.category_orange,
        R.color.category_light_yellow,
        R.color.category_mint,
        R.color.category_grass_green,
        R.color.category_mud_green,
        R.color.category_lollipop,
        R.color.category_navy_blue,
        R.color.category_flower_violet,
        R.color.category_light_violet,
        R.color.category_pink
    ).shuffled(Random(currentDateTime().toMillis()))
}
