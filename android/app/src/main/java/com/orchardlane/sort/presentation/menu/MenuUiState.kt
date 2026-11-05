package com.orchardlane.sort.presentation.menu

import com.orchardlane.sort.domain.model.Orchard

data class OrchardCard(
    val orchard: Orchard,
    val lanesDone: Int,
    val laneCount: Int,
    val unlocked: Boolean
)

data class MenuUiState(
    val loading: Boolean = true,
    val orchards: List<OrchardCard> = emptyList(),
    val currentOrchardIndex: Int = 0,
    val currentLaneInOrchard: Int = 1,
    val currentLaneNumber: Int = 1,
    val lanesDone: Int = 0,
    val facesUnlocked: Int = 0
)
