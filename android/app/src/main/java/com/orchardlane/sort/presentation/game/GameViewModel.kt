package com.orchardlane.sort.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orchardlane.sort.core.config.GameConfig
import com.orchardlane.sort.domain.model.Crate
import com.orchardlane.sort.domain.model.CratePosition
import com.orchardlane.sort.domain.model.LaneResult
import com.orchardlane.sort.domain.model.SwipeDirection
import com.orchardlane.sort.domain.model.crateIndexForSwipe
import com.orchardlane.sort.domain.repository.ProgressRepository
import com.orchardlane.sort.domain.repository.SettingsRepository
import com.orchardlane.sort.domain.usecase.EvaluateSortUseCase
import com.orchardlane.sort.domain.usecase.GenerateLaneUseCase
import com.orchardlane.sort.domain.usecase.RuleForLaneUseCase
import com.orchardlane.sort.domain.usecase.UnlockFaceSetUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

sealed interface GameEvent {
    data object Landed : GameEvent
    data class Finished(val result: LaneResult) : GameEvent
}

class GameViewModel(
    private val orchardIndex: Int,
    private val laneNumber: Int,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val ruleForLane: RuleForLaneUseCase,
    private val generateLane: GenerateLaneUseCase,
    private val evaluateSort: EvaluateSortUseCase,
    private val unlockFaceSet: UnlockFaceSetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val events = Channel<GameEvent>(Channel.BUFFERED)
    val eventFlow: Flow<GameEvent> = events.receiveAsFlow()

    private var hapticsEnabled = true
    private var sortJob: Job? = null

    init {
        viewModelScope.launch {
            val settings = settingsRepository.load()
            hapticsEnabled = settings.hapticsEnabled
            val rule = ruleForLane(laneNumber)
            val fruits = generateLane(rule, GameConfig.LANE_FRUIT_COUNT, laneNumber)
            val crates = listOf(
                Crate(0, rule.labels[0], CratePosition.TOP),
                Crate(1, rule.labels[1], CratePosition.BOTTOM_LEFT),
                Crate(2, rule.labels[2], CratePosition.BOTTOM_RIGHT)
            )
            _state.update { current ->
                current.copy(
                    phase = GamePhase.Idle,
                    orchardIndex = orchardIndex,
                    laneNumber = laneNumber,
                    laneInOrchard = ((laneNumber - 1) % GameConfig.LANES_PER_ORCHARD) + 1,
                    rule = rule,
                    crates = crates,
                    fruits = fruits,
                    swipeDistanceDp = settings.swipeDistance.dp
                )
            }
        }
    }

    fun onSwipe(direction: SwipeDirection) {
        onCrateChosen(crateIndexForSwipe(direction))
    }

    fun onCrateChosen(crateIndex: Int) {
        val current = _state.value
        val rule = current.rule ?: return
        val fruit = current.currentFruit ?: return
        if (current.inputLocked) return

        val correctIndex = evaluateSort.correctCrate(fruit, rule)
        val correct = evaluateSort(fruit, crateIndex, rule)

        _state.update { value ->
            value.copy(
                phase = if (correct) GamePhase.Sorting else GamePhase.Bouncing,
                targetCrate = if (correct) crateIndex else -1,
                wrongCrate = if (correct) -1 else crateIndex,
                tries = if (correct) value.tries else value.tries + 1
            )
        }

        sortJob?.cancel()
        sortJob = viewModelScope.launch {
            if (!correct) {
                delay(GameConfig.BOUNCE_BACK_MS.toLong())
                _state.update { value ->
                    value.copy(
                        phase = GamePhase.Sorting,
                        targetCrate = correctIndex,
                        wrongCrate = -1
                    )
                }
            }
            delay(GameConfig.SORT_ARC_MS.toLong())
            if (hapticsEnabled) {
                events.send(GameEvent.Landed)
            }
            completeFruit(correctIndex)
        }
    }

    private suspend fun completeFruit(crateIndex: Int) {
        val updated = _state.updateAndGet { current ->
            val fill = current.crateFill.toMutableList()
            fill[crateIndex] = fill[crateIndex] + 1
            current.copy(
                crateFill = fill,
                sorted = current.sorted + 1,
                currentIndex = current.currentIndex + 1,
                targetCrate = -1,
                wrongCrate = -1
            )
        }
        if (updated.currentIndex >= updated.fruits.size) {
            _state.update { it.copy(phase = GamePhase.LaneComplete) }
            delay(GameConfig.LEAF_BURST_MS.toLong())
            finishLane(updated.sorted, updated.tries)
        } else {
            delay(GameConfig.NEXT_FRUIT_MS.toLong())
            _state.update { it.copy(phase = GamePhase.Idle) }
        }
    }

    private suspend fun finishLane(sorted: Int, tries: Int) {
        val before = progressRepository.load()
        val lanesDoneAfter = maxOf(before.lanesDone, laneNumber)
        val unlocked = unlockFaceSet(lanesDoneAfter, before.unlockedFaceSets)
        val after = progressRepository.saveLaneCompleted(laneNumber, unlocked)
        events.send(
            GameEvent.Finished(
                LaneResult(
                    orchardIndex = orchardIndex,
                    laneNumber = laneNumber,
                    sorted = sorted,
                    tries = tries,
                    lanesDone = after.lanesDone,
                    unlockedFaceSet = unlocked
                )
            )
        )
    }

    fun openRuleCard() {
        _state.update { it.copy(showRuleCard = true) }
    }

    fun dismissRuleCard() {
        _state.update { it.copy(showRuleCard = false) }
    }

    fun openPause() {
        _state.update { it.copy(showPause = true) }
    }

    fun dismissPause() {
        _state.update { it.copy(showPause = false) }
    }

    fun pause() {
        sortJob?.cancel()
        sortJob = null
    }

    override fun onCleared() {
        super.onCleared()
        pause()
    }
}
