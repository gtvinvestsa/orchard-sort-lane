package com.orchardlane.sort.presentation.gameover

import com.orchardlane.sort.domain.model.CrateLabel
import com.orchardlane.sort.domain.model.FaceSet

data class GameOverUiState(
    val loading: Boolean = true,
    val orchardIndex: Int = 0,
    val laneInOrchard: Int = 1,
    val sorted: Int = 0,
    val tries: Int = 0,
    val lanesDone: Int = 0,
    val unlockedFaceSet: FaceSet? = null,
    val nextLaneNumber: Int = 1,
    val crateLabels: List<CrateLabel> = emptyList()
)
