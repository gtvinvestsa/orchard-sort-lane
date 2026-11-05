package com.orchardlane.sort.presentation.faces

import com.orchardlane.sort.domain.model.FaceSet

data class FaceCard(
    val faceSet: FaceSet,
    val unlocked: Boolean,
    val selected: Boolean
)

data class FruitFacesUiState(
    val loading: Boolean = true,
    val cards: List<FaceCard> = emptyList(),
    val lanesDone: Int = 0
)
