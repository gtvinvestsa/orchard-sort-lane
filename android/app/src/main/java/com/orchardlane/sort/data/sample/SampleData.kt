package com.orchardlane.sort.data.sample

import com.orchardlane.sort.domain.model.FaceSet
import com.orchardlane.sort.domain.model.FruitKind
import com.orchardlane.sort.domain.model.Orchard
import com.orchardlane.sort.domain.model.OrchardId

object SampleData {

    val orchards: List<Orchard> = listOf(
        Orchard(0, OrchardId.SUNNY_ROW, 1, 20),
        Orchard(1, OrchardId.MORNING_HILL, 2, 20),
        Orchard(2, OrchardId.RED_GATE, 3, 20),
        Orchard(3, OrchardId.CREEK_SIDE, 4, 20),
        Orchard(4, OrchardId.OLD_FENCE, 5, 20),
        Orchard(5, OrchardId.HARVEST_TOP, 1, 20)
    )

    val fruitKinds: List<FruitKind> = FruitKind.entries.toList()

    val faceSets: List<FaceSet> = FaceSet.entries.toList()

    val tutorialFruits: List<FruitKind> = listOf(FruitKind.APPLE, FruitKind.PEAR, FruitKind.BERRY)
}
