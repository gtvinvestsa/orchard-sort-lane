package com.orchardlane.sort.core.di

import android.content.Context
import com.orchardlane.sort.data.local.OrchardPreferences
import com.orchardlane.sort.data.repository.ProgressRepositoryImpl
import com.orchardlane.sort.data.repository.SettingsRepositoryImpl
import com.orchardlane.sort.domain.repository.ProgressRepository
import com.orchardlane.sort.domain.repository.SettingsRepository
import com.orchardlane.sort.domain.usecase.EvaluateSortUseCase
import com.orchardlane.sort.domain.usecase.GenerateLaneUseCase
import com.orchardlane.sort.domain.usecase.RuleForLaneUseCase
import com.orchardlane.sort.domain.usecase.UnlockFaceSetUseCase

object ServiceLocator {

    private var progress: ProgressRepository? = null
    private var settings: SettingsRepository? = null

    val ruleForLane = RuleForLaneUseCase()
    val generateLane = GenerateLaneUseCase()
    val evaluateSort = EvaluateSortUseCase()
    val unlockFaceSet = UnlockFaceSetUseCase()

    fun init(context: Context) {
        if (progress == null) {
            val preferences = OrchardPreferences(context.applicationContext)
            progress = ProgressRepositoryImpl(preferences)
            settings = SettingsRepositoryImpl(preferences)
        }
    }

    val progressRepository: ProgressRepository
        get() = checkNotNull(progress)

    val settingsRepository: SettingsRepository
        get() = checkNotNull(settings)
}
