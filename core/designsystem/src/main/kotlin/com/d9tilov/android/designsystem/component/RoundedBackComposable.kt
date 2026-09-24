package com.d9tilov.android.designsystem.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/** Register screens here to clip their corners while NavHost seeks through a back transition. */
fun NavGraphBuilder.roundedBackComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(route = route, arguments = arguments) { entry ->
        val destinationScope = this
        val cornerRadius by transition.animateDp(label = "back corner radius") { state ->
            if (state == EnterExitState.PostExit) BACK_CORNER_RADIUS else 0.dp
        }
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        shape = RoundedCornerShape(cornerRadius)
                        clip = true
                    },
        ) {
            destinationScope.content(entry)
        }
    }
}

private val BACK_CORNER_RADIUS = 32.dp
