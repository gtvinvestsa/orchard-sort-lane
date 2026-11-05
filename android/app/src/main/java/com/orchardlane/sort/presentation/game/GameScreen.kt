package com.orchardlane.sort.presentation.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orchardlane.sort.R
import com.orchardlane.sort.core.config.GameConfig
import com.orchardlane.sort.core.di.AppViewModelFactory
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.FreshGreen
import com.orchardlane.sort.core.theme.GameOverlay
import com.orchardlane.sort.core.theme.OrchardBorder
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.SkyBlue
import com.orchardlane.sort.core.theme.SkyBlueDark
import com.orchardlane.sort.core.theme.Terracotta
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.CrateBox
import com.orchardlane.sort.core.ui.FruitSprite
import com.orchardlane.sort.core.ui.LaneRail
import com.orchardlane.sort.core.ui.PrimaryButton
import com.orchardlane.sort.core.ui.ScreenBackground
import com.orchardlane.sort.core.ui.SecondaryButton
import com.orchardlane.sort.core.ui.crateAccentFor
import com.orchardlane.sort.core.ui.crateGlyphFor
import com.orchardlane.sort.core.ui.crateLabelText
import com.orchardlane.sort.core.ui.spriteResFor
import com.orchardlane.sort.domain.model.CratePosition
import com.orchardlane.sort.domain.model.LaneResult
import com.orchardlane.sort.domain.model.SwipeDirection
import com.orchardlane.sort.presentation.dialog.PauseDialog
import com.orchardlane.sort.presentation.dialog.RuleCardDialog
import kotlin.math.abs
import kotlin.math.sin
import com.orchardlane.sort.core.ui.AppIcons

@Composable
fun GameRoute(
    orchardIndex: Int,
    laneNumber: Int,
    onMenu: () -> Unit,
    onLaneFinished: (LaneResult) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        key = "game-$orchardIndex-$laneNumber",
        factory = AppViewModelFactory.game(orchardIndex, laneNumber)
    )
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        gameViewModel.eventFlow.collect { event ->
            when (event) {
                GameEvent.Landed -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                is GameEvent.Finished -> onLaneFinished(event.result)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameScreen(
        state = state,
        onCrateChosen = gameViewModel::onCrateChosen,
        onSwipe = gameViewModel::onSwipe,
        onShowRule = gameViewModel::openRuleCard,
        onDismissRule = gameViewModel::dismissRuleCard,
        onPause = gameViewModel::openPause,
        onResume = gameViewModel::dismissPause,
        onMenu = onMenu
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onCrateChosen: (Int) -> Unit,
    onSwipe: (SwipeDirection) -> Unit,
    onShowRule: () -> Unit,
    onDismissRule: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(overlay = GameOverlay, showBlobs = false) {
        if (state.phase == GamePhase.Loading || state.rule == null) {
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
            ) {
                GameHeader(state = state, onMenu = onMenu)

                LaneArea(
                    state = state,
                    onCrateChosen = onCrateChosen,
                    onSwipe = onSwipe,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                GamePanel(
                    state = state,
                    onShowRule = onShowRule,
                    onPause = onPause,
                    onMenu = onMenu
                )
            }
        }

        if (state.showRuleCard && state.rule != null) {
            RuleCardDialog(
                label = state.crates.firstOrNull()?.label,
                spriteIndex = state.currentFruit?.kind?.spriteIndex ?: 1,
                onDismiss = onDismissRule
            )
        }

        if (state.showPause) {
            PauseDialog(onResume = onResume, onMenu = onMenu)
        }
    }
}

@Composable
private fun GameHeader(state: GameUiState, onMenu: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp)
    ) {
        IconButton(onClick = onMenu, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector = AppIcons.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = SageDeep,
                modifier = Modifier.size(26.dp)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(
                    R.string.orchard_lane,
                    state.orchardIndex + 1,
                    state.laneInOrchard
                ),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(
                    R.string.rule_caption,
                    crateLabelText(state.crates[0].label)
                ),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 12.sp,
                color = AppleRed,
                textAlign = TextAlign.Center
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(60.dp)
                .height(34.dp)
                .background(FreshGreen, RoundedCornerShape(18.dp))
                .border(2.dp, SageDeep, RoundedCornerShape(18.dp))
                .semantics { liveRegion = LiveRegionMode.Polite }
        ) {
            Text(
                text = stringResource(R.string.sorted_counter, state.sorted, state.total),
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun LaneArea(
    state: GameUiState,
    onCrateChosen: (Int) -> Unit,
    onSwipe: (SwipeDirection) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = androidx.compose.ui.platform.LocalDensity.current
    val threshold = remember(state.swipeDistanceDp) {
        with(density) { state.swipeDistanceDp.dp.toPx() }
    }

    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .pointerInput(state.currentIndex, state.inputLocked, threshold) {
                var dx = 0f
                var dy = 0f
                detectDragGestures(
                    onDragStart = {
                        dx = 0f
                        dy = 0f
                    },
                    onDragEnd = {
                        val absX = abs(dx)
                        val absY = abs(dy)
                        if (absX > absY && absX > threshold) {
                            onSwipe(if (dx < 0f) SwipeDirection.LEFT else SwipeDirection.RIGHT)
                        } else if (absY > absX && absY > threshold && dy < 0f) {
                            onSwipe(SwipeDirection.UP)
                        }
                    }
                ) { change, drag ->
                    dx += drag.x
                    dy += drag.y
                    change.consume()
                }
            }
    ) {
        val areaWidth = maxWidth
        val areaHeight = maxHeight
        val crateWidth = (areaWidth - 16.dp) / 2
        val crateHeight = 104.dp

        LaneRail(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .padding(vertical = crateHeight + 4.dp)
        )

        state.crates.forEach { crate ->
            val alignment = when (crate.position) {
                CratePosition.TOP -> Alignment.TopCenter
                CratePosition.BOTTOM_LEFT -> Alignment.BottomStart
                CratePosition.BOTTOM_RIGHT -> Alignment.BottomEnd
            }
            CrateBox(
                label = crateLabelText(crate.label),
                glyph = crateGlyphFor(crate.label),
                accent = crateAccentFor(crate.label),
                fillFraction = if (state.total == 0) {
                    0f
                } else {
                    state.crateFill[crate.index].toFloat() / state.total
                },
                description = crateLabelText(crate.label),
                stateLabel = stringResource(
                    R.string.crate_state,
                    state.crateFill[crate.index]
                ),
                shaking = state.wrongCrate == crate.index,
                highlighted = state.targetCrate == crate.index,
                onClick = { onCrateChosen(crate.index) },
                modifier = Modifier
                    .align(alignment)
                    .width(crateWidth)
                    .height(crateHeight)
            )
        }

        val fruit = state.currentFruit
        if (fruit != null && state.phase != GamePhase.LaneComplete) {
            key(state.currentIndex) {
                val travel by animateFloatAsState(
                    targetValue = if (state.phase == GamePhase.Sorting) 1f else 0f,
                    animationSpec = tween(GameConfig.SORT_ARC_MS, easing = FastOutSlowInEasing)
                )
                val bounce by animateFloatAsState(
                    targetValue = if (state.phase == GamePhase.Bouncing) 1f else 0f,
                    animationSpec = spring(dampingRatio = 0.4f, stiffness = 500f)
                )
                val targetIndex = if (state.targetCrate >= 0) {
                    state.targetCrate
                } else {
                    state.wrongCrate
                }
                val destX = when (targetIndex) {
                    1 -> -(areaWidth - crateWidth) / 2
                    2 -> (areaWidth - crateWidth) / 2
                    else -> 0.dp
                }
                val destY = when (targetIndex) {
                    0 -> -(areaHeight - crateHeight) / 2
                    1, 2 -> (areaHeight - crateHeight) / 2
                    else -> 0.dp
                }
                val blend = if (state.phase == GamePhase.Bouncing) bounce * 0.45f else travel

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(GameConfig.MIN_FRUIT_SIZE_DP.dp)
                        .graphicsLayer {
                            translationX = destX.toPx() * blend
                            translationY = destY.toPx() * blend -
                                sin(blend * Math.PI.toFloat()) * 40.dp.toPx()
                            rotationZ = travel * 220f
                            val shrink = 1f - travel * 0.45f
                            scaleX = shrink
                            scaleY = shrink
                            alpha = 1f - travel * 0.25f
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .width(64.dp)
                            .height(12.dp)
                            .background(Color(0x1F2B3A1E), CircleShape)
                    )
                    FruitSprite(
                        spriteIndex = fruit.kind.spriteIndex,
                        size = GameConfig.MIN_FRUIT_SIZE_DP.dp,
                        description = stringResource(R.string.cd_fruit),
                        idle = state.phase == GamePhase.Idle
                    )
                }
            }
        }

        if (state.phase == GamePhase.LaneComplete) {
            LeafBurst(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun LeafBurst(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(GameConfig.LEAF_BURST_MS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val seeds = remember {
        List(14) { index ->
            val angle = index * 25.7f
            Triple(angle, 28 + (index % 3) * 9, if (index % 2 == 0) 2 else 4)
        }
    }
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        seeds.forEach { seed ->
            val radians = Math.toRadians(seed.first.toDouble())
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(spriteResFor(seed.third)),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                modifier = Modifier
                    .size(seed.second.dp)
                    .graphicsLayer {
                        val distance = 230.dp.toPx() * progress
                        translationX = (kotlin.math.cos(radians) * distance).toFloat()
                        translationY = (sin(radians) * distance).toFloat()
                        rotationZ = progress * 240f
                        alpha = 1f - progress
                    }
            )
        }
    }
}

@Composable
private fun GamePanel(
    state: GameUiState,
    onShowRule: () -> Unit,
    onPause: () -> Unit,
    onMenu: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(OrchardSurface.copy(alpha = 0.94f), shape)
            .border(2.dp, OrchardBorder, shape)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            InlineStat(
                label = stringResource(R.string.stat_sorted),
                value = if (state.sorted == 0) {
                    stringResource(R.string.empty_value)
                } else {
                    state.sorted.toString()
                },
                accent = SageDeep
            )
            InlineStat(
                label = stringResource(R.string.stat_tries),
                value = if (state.tries == 0) {
                    stringResource(R.string.empty_value)
                } else {
                    state.tries.toString()
                },
                accent = Terracotta
            )
            InlineStat(
                label = stringResource(R.string.stat_lane),
                value = stringResource(
                    R.string.lane_progress,
                    state.laneInOrchard,
                    GameConfig.LANES_PER_ORCHARD
                ),
                accent = SkyBlue
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        PrimaryButton(
            text = stringResource(R.string.show_rule),
            icon = AppIcons.Info,
            onClick = onShowRule,
            height = 60.dp,
            gradient = listOf(SkyBlue, SkyBlue, SkyBlueDark),
            glowColor = SkyBlue,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        ) {
            SecondaryButton(
                text = stringResource(R.string.menu),
                icon = AppIcons.Home,
                onClick = onMenu,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.pause),
                painter = androidx.compose.ui.res.painterResource(R.drawable.icon_pause),
                onClick = onPause,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun InlineStat(label: String, value: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 12.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 17.sp,
            color = accent
        )
    }
}
