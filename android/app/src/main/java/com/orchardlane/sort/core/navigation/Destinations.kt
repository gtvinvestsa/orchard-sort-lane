package com.orchardlane.sort.core.navigation

import com.orchardlane.sort.domain.model.LaneResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val orchardIndex: Int, val laneNumber: Int) : Screen
    data class GameOver(val result: LaneResult) : Screen
    data object FruitFaces : Screen
    data object Settings : Screen
    data object Tutorial : Screen
}
