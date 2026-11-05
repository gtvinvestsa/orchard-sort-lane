package com.orchardlane.sort.presentation.app

import androidx.lifecycle.ViewModel
import com.orchardlane.sort.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _backStack = MutableStateFlow<List<Screen>>(emptyList())
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash) {
            _backStack.value = _backStack.value + current
        }
        _screen.value = target
    }

    fun replaceTop(target: Screen) {
        _screen.value = target
    }

    fun startAtMenu() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }

    fun backToMenu() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }

    fun back() {
        val stack = _backStack.value
        val previous = stack.lastOrNull() ?: return
        _backStack.value = stack.dropLast(1)
        _screen.value = previous
    }
}
