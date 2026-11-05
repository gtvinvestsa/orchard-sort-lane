package com.orchardlane.sort.domain.model

enum class CratePosition { TOP, BOTTOM_LEFT, BOTTOM_RIGHT }

data class Crate(
    val index: Int,
    val label: CrateLabel,
    val position: CratePosition
)

enum class SwipeDirection { LEFT, RIGHT, UP }

fun crateIndexForSwipe(direction: SwipeDirection): Int = when (direction) {
    SwipeDirection.UP -> 0
    SwipeDirection.LEFT -> 1
    SwipeDirection.RIGHT -> 2
}
