package com.orchardlane.sort.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orchardlane.sort.core.config.GameConfig
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.AppleRedDark
import com.orchardlane.sort.core.theme.AppleRedLight
import com.orchardlane.sort.core.theme.SkyBlue
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.theme.TopLight
import kotlinx.coroutines.launch

@Composable
fun HoldToEnterGate(
    holdLabel: String,
    hint: String,
    onUnlocked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val unlocked = rememberUpdatedState(onUnlocked)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .shadow(18.dp, CircleShape, spotColor = AppleRed.copy(alpha = 0.5f))
                .background(
                    Brush.linearGradient(listOf(AppleRedLight, AppleRed, AppleRedDark)),
                    CircleShape
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            val job = scope.launch {
                                progress.animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(GameConfig.HOLD_GATE_MS.toInt())
                                )
                                unlocked.value()
                            }
                            tryAwaitRelease()
                            job.cancel()
                            scope.launch {
                                progress.animateTo(0f, tween(220))
                            }
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 8.dp.toPx()
                drawArc(
                    color = TopLight,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                    topLeft = androidx.compose.ui.geometry.Offset(stroke / 2f, stroke / 2f),
                    size = androidx.compose.ui.geometry.Size(
                        size.width - stroke,
                        size.height - stroke
                    )
                )
                drawArc(
                    color = Color.White,
                    startAngle = -90f,
                    sweepAngle = 360f * progress.value,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                    topLeft = androidx.compose.ui.geometry.Offset(stroke / 2f, stroke / 2f),
                    size = androidx.compose.ui.geometry.Size(
                        size.width - stroke,
                        size.height - stroke
                    )
                )
            }
            Text(
                text = holdLabel,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 18.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = hint,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(width = 64.dp, height = 4.dp)
                .background(SkyBlue.copy(alpha = 0.5f), CircleShape)
        )
    }
}
