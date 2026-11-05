package com.orchardlane.sort.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.orchardlane.sort.R

object AppIcons {
    val PlayArrow: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_play_arrow)

    val Home: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_home)

    val Lock: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_lock)

    val Check: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_check)

    val ArrowBack: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_arrow_back)

    val Info: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_info)

    val Star: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_star)

    val Refresh: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_refresh)

    val Settings: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_settings)

    val Face: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_face)
}
