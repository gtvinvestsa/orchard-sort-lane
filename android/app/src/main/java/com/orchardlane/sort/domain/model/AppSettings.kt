package com.orchardlane.sort.domain.model

enum class SwipeDistance(val dp: Int) {
    SHORT(40),
    NORMAL(56),
    LONG(76)
}

data class AppSettings(
    val soundEnabled: Boolean,
    val hapticsEnabled: Boolean,
    val swipeDistance: SwipeDistance
)
