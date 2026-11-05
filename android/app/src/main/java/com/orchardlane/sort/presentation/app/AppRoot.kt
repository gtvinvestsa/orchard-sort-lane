package com.orchardlane.sort.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orchardlane.sort.core.di.AppViewModelFactory
import com.orchardlane.sort.core.navigation.Screen
import com.orchardlane.sort.presentation.faces.FruitFacesRoute
import com.orchardlane.sort.presentation.game.GameRoute
import com.orchardlane.sort.presentation.gameover.GameOverRoute
import com.orchardlane.sort.presentation.menu.MenuRoute
import com.orchardlane.sort.presentation.settings.SettingsRoute
import com.orchardlane.sort.presentation.splash.SplashRoute
import com.orchardlane.sort.presentation.tutorial.TutorialRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory.app())
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val backStack by appViewModel.backStack.collectAsStateWithLifecycle()

    BackHandler(enabled = backStack.isNotEmpty()) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = { fadeIn(tween(420)) togetherWith fadeOut(tween(320)) },
        modifier = Modifier.fillMaxSize()
    ) { target ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (target) {
                Screen.Splash -> SplashRoute(
                    onFinished = { appViewModel.startAtMenu() }
                )

                Screen.Menu -> MenuRoute(
                    onPlay = { orchardIndex, laneNumber ->
                        appViewModel.navigate(Screen.Game(orchardIndex, laneNumber))
                    },
                    onFaces = { appViewModel.navigate(Screen.FruitFaces) },
                    onTutorial = { appViewModel.navigate(Screen.Tutorial) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                is Screen.Game -> GameRoute(
                    orchardIndex = target.orchardIndex,
                    laneNumber = target.laneNumber,
                    onMenu = { appViewModel.backToMenu() },
                    onLaneFinished = { result ->
                        appViewModel.replaceTop(Screen.GameOver(result))
                    }
                )

                is Screen.GameOver -> GameOverRoute(
                    result = target.result,
                    onPlayAgain = { orchardIndex, laneNumber ->
                        appViewModel.replaceTop(Screen.Game(orchardIndex, laneNumber))
                    },
                    onMenu = { appViewModel.backToMenu() }
                )

                Screen.FruitFaces -> FruitFacesRoute(
                    onMenu = { appViewModel.backToMenu() }
                )

                Screen.Settings -> SettingsRoute(
                    onMenu = { appViewModel.backToMenu() }
                )

                Screen.Tutorial -> TutorialRoute(
                    onPlay = { orchardIndex, laneNumber ->
                        appViewModel.replaceTop(Screen.Game(orchardIndex, laneNumber))
                    },
                    onMenu = { appViewModel.backToMenu() }
                )
            }
        }
    }
}
