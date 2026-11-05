package com.orchardlane.sort.domain.model

data class LaneResult(
    val orchardIndex: Int,
    val laneNumber: Int,
    val sorted: Int,
    val tries: Int,
    val lanesDone: Int,
    val unlockedFaceSet: FaceSet?
)
