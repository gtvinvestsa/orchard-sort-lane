package com.orchardlane.sort.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orchardlane.sort.core.config.GameConfig
import com.orchardlane.sort.domain.model.LaneResult
import com.orchardlane.sort.domain.repository.ProgressRepository
import com.orchardlane.sort.domain.usecase.RuleForLaneUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameOverViewModel(
    private val result: LaneResult,
    private val progressRepository: ProgressRepository,
    private val ruleForLane: RuleForLaneUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update {
                it.copy(
                    loading = false,
                    orchardIndex = result.orchardIndex,
                    laneInOrchard = ((result.laneNumber - 1) % GameConfig.LANES_PER_ORCHARD) + 1,
                    sorted = result.sorted,
                    tries = result.tries,
                    lanesDone = result.lanesDone,
                    unlockedFaceSet = result.unlockedFaceSet,
                    nextLaneNumber = progress.currentLane,
                    crateLabels = ruleForLane(result.laneNumber).labels
                )
            }
        }
    }

    fun nextOrchardIndex(): Int =
        ((_state.value.nextLaneNumber - 1) / GameConfig.LANES_PER_ORCHARD)
            .coerceIn(0, GameConfig.ORCHARD_COUNT - 1)
}
