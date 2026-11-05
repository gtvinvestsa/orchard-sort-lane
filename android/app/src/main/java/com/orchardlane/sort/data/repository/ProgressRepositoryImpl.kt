package com.orchardlane.sort.data.repository

import com.orchardlane.sort.data.local.OrchardPreferences
import com.orchardlane.sort.domain.model.FaceSet
import com.orchardlane.sort.domain.model.GameProgress
import com.orchardlane.sort.domain.repository.ProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProgressRepositoryImpl(
    private val preferences: OrchardPreferences
) : ProgressRepository {

    override suspend fun load(): GameProgress = withContext(Dispatchers.IO) { read() }

    override suspend fun saveLaneCompleted(laneNumber: Int, unlocked: FaceSet?): GameProgress =
        withContext(Dispatchers.IO) {
            val done = maxOf(preferences.lanesDone, laneNumber)
            preferences.lanesDone = done
            preferences.currentLane = (laneNumber + 1).coerceAtMost(GameProgress.LANES_IN_ORCHARD * GameProgress.ORCHARDS)
            if (unlocked != null) {
                preferences.unlockedFaces = preferences.unlockedFaces + unlocked.name
            }
            read()
        }

    override suspend fun selectFaceSet(faceSet: FaceSet) = withContext(Dispatchers.IO) {
        preferences.selectedFace = faceSet.name
    }

    override suspend fun reset() = withContext(Dispatchers.IO) {
        preferences.clearProgress()
    }

    private fun read(): GameProgress {
        val unlocked = preferences.unlockedFaces
            .mapNotNull { name -> FaceSet.entries.firstOrNull { it.name == name } }
            .toSet()
            .ifEmpty { setOf(FaceSet.BLINKY) }
        val selected = FaceSet.entries.firstOrNull { it.name == preferences.selectedFace }
            ?: FaceSet.BLINKY
        return GameProgress(
            lanesDone = preferences.lanesDone,
            currentLane = preferences.currentLane.coerceAtLeast(1),
            unlockedFaceSets = unlocked,
            selectedFaceSet = if (selected in unlocked) selected else FaceSet.BLINKY
        )
    }
}
