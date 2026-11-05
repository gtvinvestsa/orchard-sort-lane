package com.orchardlane.sort.presentation.faces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orchardlane.sort.data.sample.SampleData
import com.orchardlane.sort.domain.model.FaceSet
import com.orchardlane.sort.domain.model.GameProgress
import com.orchardlane.sort.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FruitFacesViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FruitFacesUiState())
    val state: StateFlow<FruitFacesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch { publish(progressRepository.load()) }
    }

    fun onFaceSetSelected(faceSet: FaceSet) {
        viewModelScope.launch {
            progressRepository.selectFaceSet(faceSet)
            publish(progressRepository.load())
        }
    }

    private fun publish(progress: GameProgress) {
        _state.update {
            it.copy(
                loading = false,
                lanesDone = progress.lanesDone,
                cards = SampleData.faceSets.map { faceSet ->
                    FaceCard(
                        faceSet = faceSet,
                        unlocked = faceSet in progress.unlockedFaceSets,
                        selected = faceSet == progress.selectedFaceSet
                    )
                }
            )
        }
    }
}
