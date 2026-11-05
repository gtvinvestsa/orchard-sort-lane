package com.orchardlane.sort.domain.repository

import com.orchardlane.sort.domain.model.AppSettings
import com.orchardlane.sort.domain.model.SwipeDistance

interface SettingsRepository {
    suspend fun load(): AppSettings
    suspend fun setSound(enabled: Boolean)
    suspend fun setHaptics(enabled: Boolean)
    suspend fun setSwipeDistance(distance: SwipeDistance)
}
