package com.orchardlane.sort.domain.model

enum class OrchardId {
    SUNNY_ROW,
    MORNING_HILL,
    RED_GATE,
    CREEK_SIDE,
    OLD_FENCE,
    HARVEST_TOP
}

data class Orchard(
    val index: Int,
    val id: OrchardId,
    val spriteIndex: Int,
    val laneCount: Int
)
