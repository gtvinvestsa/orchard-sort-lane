package com.orchardlane.sort.presentation.gameover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orchardlane.sort.R
import com.orchardlane.sort.core.di.AppViewModelFactory
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.ResultOverlay
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.SkyBlue
import com.orchardlane.sort.core.theme.Terracotta
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.CrateBox
import com.orchardlane.sort.core.ui.FruitSprite
import com.orchardlane.sort.core.ui.PrimaryButton
import com.orchardlane.sort.core.ui.ScreenBackground
import com.orchardlane.sort.core.ui.SecondaryButton
import com.orchardlane.sort.core.ui.StatCard
import com.orchardlane.sort.core.ui.crateAccentFor
import com.orchardlane.sort.core.ui.crateGlyphFor
import com.orchardlane.sort.core.ui.crateLabelText
import com.orchardlane.sort.domain.model.LaneResult
import com.orchardlane.sort.core.ui.AppIcons

@Composable
fun GameOverRoute(
    result: LaneResult,
    onPlayAgain: (Int, Int) -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(
        key = "gameover-${result.laneNumber}",
        factory = AppViewModelFactory.gameOver(result)
    )
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    GameOverScreen(
        state = state,
        onPlayAgain = {
            onPlayAgain(gameOverViewModel.nextOrchardIndex(), state.nextLaneNumber)
        },
        onMenu = onMenu
    )
}

@Composable
fun GameOverScreen(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(overlay = ResultOverlay) {
        if (state.loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = AppleRed)
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(horizontal = 22.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    state.crateLabels.forEach { label ->
                        CrateBox(
                            label = crateLabelText(label),
                            glyph = crateGlyphFor(label),
                            accent = crateAccentFor(label),
                            fillFraction = 1f,
                            description = crateLabelText(label),
                            stateLabel = stringResource(R.string.state_full),
                            modifier = Modifier
                                .weight(1f)
                                .height(92.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = stringResource(R.string.crates_full),
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(
                        R.string.orchard_lane,
                        state.orchardIndex + 1,
                        state.laneInOrchard
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_sorted),
                        value = state.sorted.toString(),
                        accent = SageDeep,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_tries),
                        value = if (state.tries == 0) {
                            stringResource(R.string.empty_value)
                        } else {
                            state.tries.toString()
                        },
                        accent = Terracotta,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_lanes_done),
                        value = state.lanesDone.toString(),
                        accent = SkyBlue,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (state.unlockedFaceSet != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val shape = RoundedCornerShape(26.dp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(10.dp, shape, spotColor = SkyBlue.copy(alpha = 0.4f))
                            .background(OrchardSurface.copy(alpha = 0.95f), shape)
                            .border(2.dp, SkyBlue, shape)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        FruitSprite(
                            spriteIndex = state.unlockedFaceSet.spriteIndex,
                            size = 60.dp,
                            description = null,
                            idle = false
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(R.string.new_fruit_faces),
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = AppIcons.Star,
                            contentDescription = null,
                            tint = Terracotta,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.next_lane_ready),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                PrimaryButton(
                    text = stringResource(R.string.play_again),
                    icon = AppIcons.Refresh,
                    onClick = onPlayAgain,
                    height = 64.dp,
                    fontSize = 19,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                SecondaryButton(
                    text = stringResource(R.string.menu),
                    icon = AppIcons.Home,
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
