package com.orchardlane.sort.domain.usecase

import com.orchardlane.sort.domain.model.FaceSet

class UnlockFaceSetUseCase {

    operator fun invoke(lanesDone: Int, alreadyUnlocked: Set<FaceSet>): FaceSet? =
        FaceSet.entries.firstOrNull { it.unlockLane <= lanesDone && it !in alreadyUnlocked }
}
