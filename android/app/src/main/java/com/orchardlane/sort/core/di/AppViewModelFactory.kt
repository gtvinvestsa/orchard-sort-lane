package com.orchardlane.sort.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orchardlane.sort.domain.model.LaneResult
import com.orchardlane.sort.presentation.app.AppViewModel
import com.orchardlane.sort.presentation.faces.FruitFacesViewModel
import com.orchardlane.sort.presentation.game.GameViewModel
import com.orchardlane.sort.presentation.gameover.GameOverViewModel
import com.orchardlane.sort.presentation.menu.MenuViewModel
import com.orchardlane.sort.presentation.settings.SettingsViewModel
import com.orchardlane.sort.presentation.splash.SplashViewModel
import com.orchardlane.sort.presentation.tutorial.TutorialViewModel

class AppViewModelFactory(
    private val creator: () -> ViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T

    companion object {

        fun app() = AppViewModelFactory { AppViewModel() }

        fun splash() = AppViewModelFactory { SplashViewModel() }

        fun menu() = AppViewModelFactory { MenuViewModel(ServiceLocator.progressRepository) }

        fun game(orchardIndex: Int, laneNumber: Int) = AppViewModelFactory {
            GameViewModel(
                orchardIndex = orchardIndex,
                laneNumber = laneNumber,
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                ruleForLane = ServiceLocator.ruleForLane,
                generateLane = ServiceLocator.generateLane,
                evaluateSort = ServiceLocator.evaluateSort,
                unlockFaceSet = ServiceLocator.unlockFaceSet
            )
        }

        fun gameOver(result: LaneResult) = AppViewModelFactory {
            GameOverViewModel(
                result = result,
                progressRepository = ServiceLocator.progressRepository,
                ruleForLane = ServiceLocator.ruleForLane
            )
        }

        fun faces() = AppViewModelFactory {
            FruitFacesViewModel(ServiceLocator.progressRepository)
        }

        fun settings() = AppViewModelFactory {
            SettingsViewModel(
                settingsRepository = ServiceLocator.settingsRepository,
                progressRepository = ServiceLocator.progressRepository
            )
        }

        fun tutorial() = AppViewModelFactory {
            TutorialViewModel(
                ruleForLane = ServiceLocator.ruleForLane,
                evaluateSort = ServiceLocator.evaluateSort
            )
        }
    }
}
