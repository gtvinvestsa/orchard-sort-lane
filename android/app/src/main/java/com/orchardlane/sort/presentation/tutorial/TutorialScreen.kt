package com.orchardlane.sort.presentation.tutorial

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orchardlane.sort.R
import com.orchardlane.sort.core.di.AppViewModelFactory
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.GameOverlay
import com.orchardlane.sort.core.theme.OrchardBorder
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.CrateBox
import com.orchardlane.sort.core.ui.FruitSprite
import com.orchardlane.sort.core.ui.PrimaryButton
import com.orchardlane.sort.core.ui.ScreenBackground
import com.orchardlane.sort.core.ui.SecondaryButton
import com.orchardlane.sort.core.ui.crateAccentFor
import com.orchardlane.sort.core.ui.crateGlyphFor
import com.orchardlane.sort.core.ui.crateLabelText
import com.orchardlane.sort.domain.model.CratePosition
import com.orchardlane.sort.core.ui.AppIcons

@Composable
fun TutorialRoute(
    onPlay: (Int, Int) -> Unit,
    onMenu: () -> Unit
) {
    val tutorialViewModel: TutorialViewModel = viewModel(factory = AppViewModelFactory.tutorial())
    val state by tutorialViewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose { tutorialViewModel.pause() }
    }

    TutorialScreen(
        state = state,
        hintCrate = tutorialViewModel.hintCrateIndex(),
        onCrateChosen = tutorialViewModel::onCrateChosen,
        onPlay = { onPlay(0, 1) },
        onMenu = onMenu
    )
}

@Composable
fun TutorialScreen(
    state: TutorialUiState,
    hintCrate: Int,
    onCrateChosen: (Int) -> Unit,
    onPlay: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(overlay = GameOverlay, showBlobs = false) {
        if (state.loading || state.rule == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = AppleRed)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.tutorial_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (state.completed) {
                        stringResource(R.string.tutorial_done)
                    } else {
                        stringResource(R.string.tutorial_coach)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                TutorialBoard(
                    state = state,
                    hintCrate = hintCrate,
                    onCrateChosen = onCrateChosen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            OrchardSurface.copy(alpha = 0.9f),
                            RoundedCornerShape(20.dp)
                        )
                        .border(2.dp, OrchardBorder, RoundedCornerShape(20.dp))
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.tutorial_step,
                            state.step.coerceAtMost(state.fruits.size),
                            state.fruits.size
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                PrimaryButton(
                    text = stringResource(R.string.play),
                    icon = AppIcons.PlayArrow,
                    onClick = onPlay,
                    height = 60.dp,
                    enabled = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    SecondaryButton(
                        text = stringResource(R.string.menu),
                        icon = AppIcons.Home,
                        onClick = onMenu,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = onPlay,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.skip),
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TutorialBoard(
    state: TutorialUiState,
    hintCrate: Int,
    onCrateChosen: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pulse by rememberInfiniteTransition().animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse)
    )

    BoxWithConstraints(modifier = modifier) {
        val crateWidth = (maxWidth - 16.dp) / 2
        val crateHeight = 100.dp

        state.crates.forEach { crate ->
            val alignment = when (crate.position) {
                CratePosition.TOP -> Alignment.TopCenter
                CratePosition.BOTTOM_LEFT -> Alignment.BottomStart
                CratePosition.BOTTOM_RIGHT -> Alignment.BottomEnd
            }
            val isHint = !state.completed && crate.index == hintCrate
            CrateBox(
                label = crateLabelText(crate.label),
                glyph = crateGlyphFor(crate.label),
                accent = crateAccentFor(crate.label),
                fillFraction = if (state.fruits.isEmpty()) {
                    0f
                } else {
                    state.step.toFloat() / state.fruits.size
                },
                description = crateLabelText(crate.label),
                stateLabel = crateLabelText(crate.label),
                shaking = state.wrongCrate == crate.index,
                highlighted = isHint,
                onClick = { onCrateChosen(crate.index) },
                modifier = Modifier
                    .align(alignment)
                    .width(crateWidth)
                    .height(crateHeight)
                    .graphicsLayer { alpha = if (isHint) pulse else 1f }
            )
        }

        val fruit = state.currentFruit
        if (fruit != null) {
            FruitSprite(
                spriteIndex = fruit.kind.spriteIndex,
                size = 104.dp,
                description = stringResource(R.string.cd_fruit),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
