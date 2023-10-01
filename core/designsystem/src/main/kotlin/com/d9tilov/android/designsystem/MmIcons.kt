package com.d9tilov.android.designsystem

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object MoneyManagerIcons {
    val FormatList = Icons.Rounded.List
    val Chart = Icons.Rounded.BarChart
    val Profile = Icons.Rounded.Settings
    val ArrowBack = Icons.Rounded.ArrowBack
    val ActionAdd = Icons.Rounded.AddTask
    val Subscription = R.drawable.ic_money_manager_logo
    val Backup = R.drawable.ic_refresh
    val Close = Icons.Rounded.Clear
    val BackSpace = R.drawable.ic_backspace
    val HideKeyboard = R.drawable.ic_keyboard_hide
    val RegularTransaction = R.drawable.ic_repeat
    val InStatisticsTransaction = R.drawable.ic_not_in_statistics
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
