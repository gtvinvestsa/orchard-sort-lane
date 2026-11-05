package com.orchardlane.sort.presentation.tutorial

import com.orchardlane.sort.domain.model.Crate
import com.orchardlane.sort.domain.model.Fruit
import com.orchardlane.sort.domain.model.SortRule

data class TutorialUiState(
    val loading: Boolean = true,
    val rule: SortRule? = null,
    val crates: List<Crate> = emptyList(),
    val fruits: List<Fruit> = emptyList(),
    val step: Int = 0,
    val wrongCrate: Int = -1,
    val completed: Boolean = false
) {
    val currentFruit: Fruit?
        get() = fruits.getOrNull(step)
}
