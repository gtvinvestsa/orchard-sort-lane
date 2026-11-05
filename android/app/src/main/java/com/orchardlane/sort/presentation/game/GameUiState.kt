package com.orchardlane.sort.presentation.game

import com.orchardlane.sort.domain.model.Crate
import com.orchardlane.sort.domain.model.Fruit
import com.orchardlane.sort.domain.model.SortRule

enum class GamePhase { Loading, Idle, Bouncing, Sorting, LaneComplete }

data class GameUiState(
    val phase: GamePhase = GamePhase.Loading,
    val orchardIndex: Int = 0,
    val laneNumber: Int = 1,
    val laneInOrchard: Int = 1,
    val rule: SortRule? = null,
    val crates: List<Crate> = emptyList(),
    val fruits: List<Fruit> = emptyList(),
    val currentIndex: Int = 0,
    val sorted: Int = 0,
    val tries: Int = 0,
    val crateFill: List<Int> = listOf(0, 0, 0),
    val targetCrate: Int = -1,
    val wrongCrate: Int = -1,
    val showRuleCard: Boolean = false,
    val showPause: Boolean = false,
    val swipeDistanceDp: Int = 56
) {
    val currentFruit: Fruit?
        get() = fruits.getOrNull(currentIndex)

    val inputLocked: Boolean
        get() = phase != GamePhase.Idle

    val total: Int
        get() = fruits.size
}
