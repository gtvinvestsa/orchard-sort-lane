package com.orchardlane.sort.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.orchardlane.sort.core.theme.MenuOverlay
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.SkyBlue
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.OrchardTile
import com.orchardlane.sort.core.ui.PrimaryButton
import com.orchardlane.sort.core.ui.ScreenBackground
import com.orchardlane.sort.core.ui.SecondaryButton
import com.orchardlane.sort.core.ui.StatCard
import com.orchardlane.sort.domain.model.OrchardId
import com.orchardlane.sort.core.ui.AppIcons

@Composable
fun MenuRoute(
    onPlay: (Int, Int) -> Unit,
    onFaces: () -> Unit,
    onTutorial: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory.menu())
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = { onPlay(state.currentOrchardIndex, state.currentLaneNumber) },
        onOrchardSelected = { index -> onPlay(index, menuViewModel.laneNumberFor(index)) },
        onFaces = onFaces,
        onTutorial = onTutorial,
        onSettings = onSettings
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOrchardSelected: (Int) -> Unit,
    onFaces: () -> Unit,
    onTutorial: () -> Unit,
    onSettings: () -> Unit
) {
    ScreenBackground(overlay = MenuOverlay) {
        if (state.loading) {
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
                .padding(horizontal = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = stringResource(R.string.cd_home_mark),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.menu_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                }
                IconButton(
                    onClick = onSettings,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.Settings,
                        contentDescription = stringResource(R.string.cd_grown_ups),
                        tint = SageDeep,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                PrimaryButton(
                    text = stringResource(R.string.play),
                    icon = AppIcons.PlayArrow,
                    onClick = onPlay,
                    height = 132.dp,
                    cornerRadius = 30.dp,
                    fontSize = 30,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(
                        R.string.orchard_lane,
                        state.currentOrchardIndex + 1,
                        state.currentLaneInOrchard
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 13.sp,
                    color = Color(0xCCFFFFFF),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                state.orchards.chunked(2).forEach { pair ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        pair.forEach { card ->
                            OrchardTile(
                                name = orchardName(card.orchard.id),
                                spriteIndex = card.orchard.spriteIndex,
                                lanesDone = card.lanesDone,
                                laneCount = card.laneCount,
                                unlocked = card.unlocked,
                                progressLabel = stringResource(
                                    R.string.tile_progress,
                                    card.lanesDone,
                                    card.laneCount
                                ),
                                stateLabel = if (card.unlocked) {
                                    stringResource(R.string.state_open)
                                } else {
                                    stringResource(R.string.state_locked)
                                },
                                onClick = { onOrchardSelected(card.orchard.index) },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (state.lanesDone > 0 && state.facesUnlocked > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_lanes),
                        value = state.lanesDone.toString(),
                        accent = SageDeep,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_faces),
                        value = state.facesUnlocked.toString(),
                        accent = SkyBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.fruit_faces),
                    icon = AppIcons.Face,
                    onClick = onFaces,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.how_to_play),
                    icon = AppIcons.Info,
                    onClick = onTutorial,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        }
    }
}

@Composable
fun orchardName(id: OrchardId): String = when (id) {
    OrchardId.SUNNY_ROW -> stringResource(R.string.orchard_sunny_row)
    OrchardId.MORNING_HILL -> stringResource(R.string.orchard_morning_hill)
    OrchardId.RED_GATE -> stringResource(R.string.orchard_red_gate)
    OrchardId.CREEK_SIDE -> stringResource(R.string.orchard_creek_side)
    OrchardId.OLD_FENCE -> stringResource(R.string.orchard_old_fence)
    OrchardId.HARVEST_TOP -> stringResource(R.string.orchard_harvest_top)
}
