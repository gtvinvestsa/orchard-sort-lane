package com.orchardlane.sort.domain.usecase

import com.orchardlane.sort.domain.model.Fruit
import com.orchardlane.sort.domain.model.FruitKind
import com.orchardlane.sort.domain.model.SortRule
import kotlin.random.Random

class GenerateLaneUseCase {

    operator fun invoke(rule: SortRule, count: Int, seed: Int): List<Fruit> {
        val random = Random(seed)
        val buckets = List(3) { crate ->
            FruitKind.entries.filter { rule.crateIndexFor(it) == crate }
        }
        val available = buckets.withIndex().filter { it.value.isNotEmpty() }
        if (available.isEmpty()) {
            return List(count) { Fruit(it, FruitKind.APPLE) }
        }
        val picked = ArrayList<FruitKind>(count)
        var cursor = 0
        while (picked.size < count) {
            val bucket = available[cursor % available.size].value
            picked.add(bucket[random.nextInt(bucket.size)])
            cursor++
        }
        return picked.shuffled(random).mapIndexed { index, kind -> Fruit(index, kind) }
    }
}
