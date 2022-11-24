package com.d9tilov.moneymanager.core.designsystem

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object MoneyManagerIcons {
    val FormatList = Icons.Rounded.List
    val Chart = Icons.Rounded.BarChart
    val Profile = Icons.Rounded.Settings
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
