package com.orchardlane.sort.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.orchardlane.sort.R

@Composable
fun ScreenBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    showBlobs: Boolean = true,
    blobColor: Color = Color(0x1F7FA46B),
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
        if (showBlobs) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-70).dp, y = 90.dp)
                    .size(240.dp)
                    .background(
                        Brush.radialGradient(listOf(blobColor, Color.Transparent)),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 80.dp, y = 320.dp)
                    .size(200.dp)
                    .background(
                        Brush.radialGradient(listOf(blobColor, Color.Transparent)),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-40).dp, y = 60.dp)
                    .size(260.dp)
                    .background(
                        Brush.radialGradient(listOf(blobColor, Color.Transparent)),
                        CircleShape
                    )
            )
        }
        content()
    }
}
