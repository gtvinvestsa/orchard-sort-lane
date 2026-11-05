package com.orchardlane.sort.core.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun FruitSprite(
    spriteIndex: Int,
    size: Dp,
    description: String?,
    modifier: Modifier = Modifier,
    idle: Boolean = true
) {
    val transition = rememberInfiniteTransition()
    val bob by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200),
            repeatMode = RepeatMode.Reverse
        )
    )
    val gloss by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(3400),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .graphicsLayer { translationY = if (idle) bob else 0f }
    ) {
        Image(
            painter = painterResource(spriteResFor(spriteIndex)),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .size(size * 0.3f)
                .graphicsLayer {
                    translationX = -size.toPx() * 0.17f
                    translationY = -size.toPx() * 0.2f
                    alpha = if (idle) gloss else 0.3f
                }
                .background(
                    Brush.radialGradient(listOf(Color(0x8CFFFFFF), Color.Transparent)),
                    CircleShape
                )
        )
    }
}
