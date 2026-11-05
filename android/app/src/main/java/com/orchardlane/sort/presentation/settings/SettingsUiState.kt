package com.orchardlane.sort.presentation.settings

import com.orchardlane.sort.domain.model.SwipeDistance

data class SettingsUiState(
    val loading: Boolean = true,
    val unlocked: Boolean = false,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val swipeDistance: SwipeDistance = SwipeDistance.NORMAL,
    val showResetConfirm: Boolean = false,
    val resetDone: Boolean = false
)
