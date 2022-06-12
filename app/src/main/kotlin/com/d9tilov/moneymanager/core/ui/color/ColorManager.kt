package com.d9tilov.moneymanager.core.ui.color

import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.toMillis
import java.util.Random

object ColorManager {

    val colorList = listOf(
        R.color.category_light_yellow,
        R.color.category_yellow_dark,
        R.color.category_yellow,
        R.color.category_orange,
        R.color.category_orange_light,
        R.color.category_dark_orange,
        R.color.category_deep_orange,
        R.color.category_deep_orange_dark,
        R.color.category_deep_orange_bright,
        R.color.category_light_blue,
        R.color.category_grey_blue,
        R.color.category_flower_violet,
        R.color.category_blue,
        R.color.category_navy_blue,
        R.color.category_violet,
        R.color.category_purple_light,
        R.color.category_purple_dark,
        R.color.category_light_green,
        R.color.category_grass_green,
        R.color.category_lollipop,
        R.color.category_mud_green,
        R.color.category_mint,
        R.color.category_green,
        R.color.category_green_dark,
        R.color.category_light_violet,
        R.color.category_red,
        R.color.category_dark_red,
        R.color.category_dark_bright,
        R.color.category_pink,
        R.color.category_pink_dark,
        R.color.category_pink_bright,
        R.color.category_purple,
        R.color.category_deep_purple,
        R.color.category_deep_purple_bright,
        R.color.category_deep_purple_dark,
        R.color.category_indigo,
        R.color.category_indigo_dark,
        R.color.category_indigo_bright,
        R.color.category_cyan,
        R.color.category_cyan_dark,
        R.color.category_cyan_bright,
        R.color.category_teal,
        R.color.category_teal_dark,
        R.color.category_teal_bright,
        R.color.category_lime,
        R.color.category_lime_dark,
        R.color.category_lime_bright,
        R.color.category_brown,
        R.color.category_brown_dark,
        R.color.category_brown_bright
    ).shuffled(Random(currentDateTime().toMillis()))
}
