package com.orchardlane.sort.presentation.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orchardlane.sort.R
import com.orchardlane.sort.core.di.AppViewModelFactory
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.FreshGreen
import com.orchardlane.sort.core.theme.OrchardBackground
import com.orchardlane.sort.core.theme.SplashOverlay
import com.orchardlane.sort.core.ui.ScreenBackground
import com.orchardlane.sort.core.ui.spriteResFor

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory.splash())
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    SplashScreen(finished = finished, onFinished = onFinished)
}

@Composable
fun SplashScreen(
    finished: Boolean,
    onFinished: () -> Unit
) {
    var handled by remember { mutableStateOf(false) }
    LaunchedEffect(finished) {
        if (finished && !handled) {
            handled = true
            onFinished()
        }
    }

    val transition = rememberInfiniteTransition()
    val breathe by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(2600), RepeatMode.Reverse)
    )
    val glow by transition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.32f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse)
    )
    val leafA by transition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(tween(3200), RepeatMode.Reverse)
    )
    val leafB by transition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(tween(3800), RepeatMode.Reverse)
    )
    val leafC by transition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(tween(4400), RepeatMode.Reverse)
    )

    ScreenBackground(overlay = SplashOverlay, showBlobs = false) {
        DriftingLeaf(spriteIndex = 2, offsetY = leafA, alignment = Alignment.TopStart, padStart = 36.dp)
        DriftingLeaf(spriteIndex = 4, offsetY = leafB, alignment = Alignment.TopEnd, padStart = 0.dp)
        DriftingLeaf(spriteIndex = 2, offsetY = leafC, alignment = Alignment.TopCenter, padStart = 0.dp)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .graphicsLayer { alpha = glow }
                        .background(
                            Brush.radialGradient(listOf(FreshGreen, Color.Transparent)),
                            CircleShape
                        )
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(232.dp)
                        .graphicsLayer {
                            scaleX = breathe
                            scaleY = breathe
                        }
                        .shadow(
                            elevation = 28.dp,
                            shape = CircleShape,
                            ambientColor = AppleRed.copy(alpha = 0.4f),
                            spotColor = AppleRed.copy(alpha = 0.4f)
                        )
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0x33FFFFFF), Color(0x14FFFFFF))
                            ),
                            CircleShape
                        )
                        .border(3.dp, Color(0x33FFFFFF), CircleShape)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(180.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 34.sp,
                    lineHeight = 40.sp,
                    color = OrchardBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.splash_subtitle),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp,
                    color = FreshGreen,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            LinearProgressIndicator(
                color = AppleRed,
                trackColor = Color(0x33F4F9EC),
                modifier = Modifier
                    .width(188.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.loading_orchard),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                color = FreshGreen.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DriftingLeaf(
    spriteIndex: Int,
    offsetY: Float,
    alignment: Alignment,
    padStart: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = padStart, end = 24.dp, top = 80.dp)
    ) {
        Image(
            painter = painterResource(spriteResFor(spriteIndex)),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(alignment)
                .size(44.dp)
                .graphicsLayer {
                    translationY = offsetY
                    rotationZ = -12f + offsetY / 7.5f
                    alpha = 0.35f
                }
        )
    }
}
