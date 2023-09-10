package com.d9tilov.android.designsystem

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BarChart
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
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
