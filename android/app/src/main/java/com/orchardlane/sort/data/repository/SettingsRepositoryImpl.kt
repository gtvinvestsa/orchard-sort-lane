package com.orchardlane.sort.data.repository

import com.orchardlane.sort.data.local.OrchardPreferences
import com.orchardlane.sort.domain.model.AppSettings
import com.orchardlane.sort.domain.model.SwipeDistance
import com.orchardlane.sort.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val preferences: OrchardPreferences
) : SettingsRepository {

    override suspend fun load(): AppSettings = withContext(Dispatchers.IO) {
        AppSettings(
            soundEnabled = preferences.soundEnabled,
            hapticsEnabled = preferences.hapticsEnabled,
            swipeDistance = SwipeDistance.entries
                .firstOrNull { it.name == preferences.swipeDistance } ?: SwipeDistance.NORMAL
        )
    }

    override suspend fun setSound(enabled: Boolean) = withContext(Dispatchers.IO) {
        preferences.soundEnabled = enabled
    }

    override suspend fun setHaptics(enabled: Boolean) = withContext(Dispatchers.IO) {
        preferences.hapticsEnabled = enabled
    }

    override suspend fun setSwipeDistance(distance: SwipeDistance) = withContext(Dispatchers.IO) {
        preferences.swipeDistance = distance.name
    }
}
