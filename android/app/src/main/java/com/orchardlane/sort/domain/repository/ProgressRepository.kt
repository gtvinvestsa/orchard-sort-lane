package com.orchardlane.sort.domain.repository

import com.orchardlane.sort.domain.model.FaceSet
import com.orchardlane.sort.domain.model.GameProgress

interface ProgressRepository {
    suspend fun load(): GameProgress
    suspend fun saveLaneCompleted(laneNumber: Int, unlocked: FaceSet?): GameProgress
    suspend fun selectFaceSet(faceSet: FaceSet)
    suspend fun reset()
}
