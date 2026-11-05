package com.orchardlane.sort.presentation.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orchardlane.sort.R
import com.orchardlane.sort.core.di.AppViewModelFactory
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.OrchardBorder
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.PanelOverlay
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.SkyBlue
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.HoldToEnterGate
import com.orchardlane.sort.core.ui.ScreenBackground
import com.orchardlane.sort.core.ui.SecondaryButton
import com.orchardlane.sort.domain.model.SwipeDistance
import com.orchardlane.sort.presentation.dialog.ConfirmResetDialog
import com.orchardlane.sort.core.ui.AppIcons

@Composable
fun SettingsRoute(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory.settings())
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onGateUnlocked = settingsViewModel::onGateUnlocked,
        onSoundChanged = settingsViewModel::onSoundChanged,
        onHapticsChanged = settingsViewModel::onHapticsChanged,
        onSwipeDistanceChanged = settingsViewModel::onSwipeDistanceChanged,
        onResetRequested = settingsViewModel::onResetRequested,
        onResetDismissed = settingsViewModel::onResetDismissed,
        onResetConfirmed = settingsViewModel::onResetConfirmed,
        onMenu = onMenu
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onGateUnlocked: () -> Unit,
    onSoundChanged: (Boolean) -> Unit,
    onHapticsChanged: (Boolean) -> Unit,
    onSwipeDistanceChanged: (SwipeDistance) -> Unit,
    onResetRequested: () -> Unit,
    onResetDismissed: () -> Unit,
    onResetConfirmed: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(overlay = PanelOverlay) {
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.grown_ups),
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedContent(
                    targetState = state.unlocked,
                    transitionSpec = { fadeIn(tween(320)) togetherWith fadeOut(tween(220)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { unlocked ->
                    if (!unlocked) {
                        HoldToEnterGate(
                            holdLabel = stringResource(R.string.hold),
                            hint = stringResource(R.string.hold_hint),
                            onUnlocked = onGateUnlocked,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 18.dp)
                        ) {
                            SettingRow(label = stringResource(R.string.sound)) {
                                Switch(
                                    checked = state.soundEnabled,
                                    onCheckedChange = onSoundChanged,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = SageDeep,
                                        uncheckedTrackColor = OrchardBorder
                                    )
                                )
                            }
                            SettingRow(label = stringResource(R.string.haptics)) {
                                Switch(
                                    checked = state.hapticsEnabled,
                                    onCheckedChange = onHapticsChanged,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = SageDeep,
                                        uncheckedTrackColor = OrchardBorder
                                    )
                                )
                            }
                            SettingBlock(label = stringResource(R.string.swipe_distance)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    SwipeChip(
                                        label = stringResource(R.string.swipe_short),
                                        selected = state.swipeDistance == SwipeDistance.SHORT,
                                        onClick = { onSwipeDistanceChanged(SwipeDistance.SHORT) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    SwipeChip(
                                        label = stringResource(R.string.swipe_normal),
                                        selected = state.swipeDistance == SwipeDistance.NORMAL,
                                        onClick = { onSwipeDistanceChanged(SwipeDistance.NORMAL) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    SwipeChip(
                                        label = stringResource(R.string.swipe_long),
                                        selected = state.swipeDistance == SwipeDistance.LONG,
                                        onClick = { onSwipeDistanceChanged(SwipeDistance.LONG) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            SettingRow(
                                label = if (state.resetDone) {
                                    stringResource(R.string.reset_done)
                                } else {
                                    stringResource(R.string.reset_progress)
                                }
                            ) {
                                SecondaryButton(
                                    text = stringResource(R.string.reset),
                                    onClick = onResetRequested,
                                    height = 48.dp,
                                    accent = AppleRed
                                )
                            }
                        }
                    }
                }

                SecondaryButton(
                    text = stringResource(R.string.menu),
                    icon = AppIcons.Home,
                    onClick = onMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }

        if (state.showResetConfirm) {
            ConfirmResetDialog(
                onConfirm = onResetConfirmed,
                onCancel = onResetDismissed
            )
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    control: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .shadow(8.dp, shape, spotColor = SageDeep.copy(alpha = 0.3f))
            .background(OrchardSurface.copy(alpha = 0.95f), shape)
            .border(2.dp, OrchardBorder, shape)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary
        )
        control()
    }
}

@Composable
private fun SettingBlock(
    label: String,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape, spotColor = SageDeep.copy(alpha = 0.3f))
            .background(OrchardSurface.copy(alpha = 0.95f), shape)
            .border(2.dp, OrchardBorder, shape)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun SwipeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(min = 48.dp)
            .background(if (selected) SkyBlue else SkyBlue.copy(alpha = 0.12f), shape)
            .border(2.dp, SkyBlue, shape)
            .clickable(onClick = onClick)
            .semantics { stateDescription = label }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) Color.White else TextSecondary
        )
    }
}
