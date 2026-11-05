package com.orchardlane.sort.presentation.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orchardlane.sort.core.config.GameConfig
import com.orchardlane.sort.data.sample.SampleData
import com.orchardlane.sort.domain.model.Crate
import com.orchardlane.sort.domain.model.CratePosition
import com.orchardlane.sort.domain.model.Fruit
import com.orchardlane.sort.domain.model.SwipeDirection
import com.orchardlane.sort.domain.model.crateIndexForSwipe
import com.orchardlane.sort.domain.usecase.EvaluateSortUseCase
import com.orchardlane.sort.domain.usecase.RuleForLaneUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TutorialViewModel(
    private val ruleForLane: RuleForLaneUseCase,
    private val evaluateSort: EvaluateSortUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TutorialUiState())
    val state: StateFlow<TutorialUiState> = _state.asStateFlow()

    private var bounceJob: Job? = null

    init {
        val rule = ruleForLane(1)
        val crates = listOf(
            Crate(0, rule.labels[0], CratePosition.TOP),
            Crate(1, rule.labels[1], CratePosition.BOTTOM_LEFT),
            Crate(2, rule.labels[2], CratePosition.BOTTOM_RIGHT)
        )
        val fruits = SampleData.tutorialFruits.mapIndexed { index, kind -> Fruit(index, kind) }
        _state.update {
            it.copy(loading = false, rule = rule, crates = crates, fruits = fruits)
        }
    }

    fun onSwipe(direction: SwipeDirection) {
        onCrateChosen(crateIndexForSwipe(direction))
    }

    fun onCrateChosen(crateIndex: Int) {
        val current = _state.value
        val rule = current.rule ?: return
        val fruit = current.currentFruit ?: return
        if (current.completed) return

        if (evaluateSort(fruit, crateIndex, rule)) {
            val nextStep = current.step + 1
            _state.update {
                it.copy(
                    step = nextStep,
                    wrongCrate = -1,
                    completed = nextStep >= it.fruits.size
                )
            }
        } else {
            _state.update { it.copy(wrongCrate = crateIndex) }
            bounceJob?.cancel()
            bounceJob = viewModelScope.launch {
                delay(GameConfig.BOUNCE_BACK_MS.toLong())
                _state.update { it.copy(wrongCrate = -1) }
            }
        }
    }

    fun hintCrateIndex(): Int {
        val current = _state.value
        val rule = current.rule ?: return 0
        val fruit = current.currentFruit ?: return 0
        return evaluateSort.correctCrate(fruit, rule)
    }

    fun pause() {
        bounceJob?.cancel()
        bounceJob = null
    }

    override fun onCleared() {
        super.onCleared()
        pause()
    }
}
