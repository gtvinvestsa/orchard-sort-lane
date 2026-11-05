package com.orchardlane.sort.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orchardlane.sort.domain.model.SwipeDistance
import com.orchardlane.sort.domain.repository.ProgressRepository
import com.orchardlane.sort.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = settingsRepository.load()
            _state.update {
                it.copy(
                    loading = false,
                    soundEnabled = settings.soundEnabled,
                    hapticsEnabled = settings.hapticsEnabled,
                    swipeDistance = settings.swipeDistance
                )
            }
        }
    }

    fun onGateUnlocked() {
        _state.update { it.copy(unlocked = true) }
    }

    fun onSoundChanged(enabled: Boolean) {
        _state.update { it.copy(soundEnabled = enabled) }
        viewModelScope.launch { settingsRepository.setSound(enabled) }
    }

    fun onHapticsChanged(enabled: Boolean) {
        _state.update { it.copy(hapticsEnabled = enabled) }
        viewModelScope.launch { settingsRepository.setHaptics(enabled) }
    }

    fun onSwipeDistanceChanged(distance: SwipeDistance) {
        _state.update { it.copy(swipeDistance = distance) }
        viewModelScope.launch { settingsRepository.setSwipeDistance(distance) }
    }

    fun onResetRequested() {
        _state.update { it.copy(showResetConfirm = true) }
    }

    fun onResetDismissed() {
        _state.update { it.copy(showResetConfirm = false) }
    }

    fun onResetConfirmed() {
        viewModelScope.launch {
            progressRepository.reset()
            _state.update { it.copy(showResetConfirm = false, resetDone = true) }
        }
    }
}
