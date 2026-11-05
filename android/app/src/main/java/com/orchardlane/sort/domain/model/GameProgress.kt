package com.orchardlane.sort.domain.model

data class GameProgress(
    val lanesDone: Int,
    val currentLane: Int,
    val unlockedFaceSets: Set<FaceSet>,
    val selectedFaceSet: FaceSet
) {
    val currentOrchardIndex: Int
        get() = ((currentLane - 1) / LANES_IN_ORCHARD).coerceIn(0, ORCHARDS - 1)

    val laneInOrchard: Int
        get() = ((currentLane - 1) % LANES_IN_ORCHARD) + 1

    fun lanesDoneInOrchard(orchardIndex: Int): Int {
        val startLane = orchardIndex * LANES_IN_ORCHARD
        return (lanesDone - startLane).coerceIn(0, LANES_IN_ORCHARD)
    }

    fun isOrchardUnlocked(orchardIndex: Int): Boolean =
        orchardIndex == 0 || lanesDone >= orchardIndex * LANES_IN_ORCHARD

    companion object {
        const val LANES_IN_ORCHARD = 20
        const val ORCHARDS = 6
    }
}
