package com.orchardlane.sort.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orchardlane.sort.data.sample.SampleData
import com.orchardlane.sort.domain.model.GameProgress
import com.orchardlane.sort.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update { current -> current.copy(loading = false).withProgress(progress) }
        }
    }

    fun laneNumberFor(orchardIndex: Int): Int {
        val current = _state.value
        return if (orchardIndex == current.currentOrchardIndex) {
            current.currentLaneNumber
        } else {
            orchardIndex * GameProgress.LANES_IN_ORCHARD + 1
        }
    }

    private fun MenuUiState.withProgress(progress: GameProgress): MenuUiState = copy(
        orchards = SampleData.orchards.map { orchard ->
            OrchardCard(
                orchard = orchard,
                lanesDone = progress.lanesDoneInOrchard(orchard.index),
                laneCount = orchard.laneCount,
                unlocked = progress.isOrchardUnlocked(orchard.index)
            )
        },
        currentOrchardIndex = progress.currentOrchardIndex,
        currentLaneInOrchard = progress.laneInOrchard,
        currentLaneNumber = progress.currentLane,
        lanesDone = progress.lanesDone,
        facesUnlocked = progress.unlockedFaceSets.size
    )
}
