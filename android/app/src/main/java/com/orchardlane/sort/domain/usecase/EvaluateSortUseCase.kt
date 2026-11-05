package com.orchardlane.sort.domain.usecase

import com.orchardlane.sort.domain.model.Fruit
import com.orchardlane.sort.domain.model.SortRule

class EvaluateSortUseCase {

    operator fun invoke(fruit: Fruit, crateIndex: Int, rule: SortRule): Boolean =
        rule.crateIndexFor(fruit.kind) == crateIndex

    fun correctCrate(fruit: Fruit, rule: SortRule): Int = rule.crateIndexFor(fruit.kind)
}
