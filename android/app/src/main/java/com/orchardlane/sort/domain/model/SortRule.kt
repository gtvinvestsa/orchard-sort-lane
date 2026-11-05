package com.orchardlane.sort.domain.model

enum class CrateLabel {
    RED,
    GREEN,
    YELLOW,
    ROUND,
    LONG,
    STRIPED,
    PLAIN,
    OTHER,
    RED_ROUND,
    STRIPED_ROUND,
    GREEN_STRIPED,
    YELLOW_LONG,
    RED_STRIPED,
    GREEN_ROUND
}

sealed interface RuleTest {
    data class ByColor(val color: FruitColor) : RuleTest
    data class ByShape(val shape: FruitShape) : RuleTest
    data class ByStripe(val striped: Boolean) : RuleTest
    data class Both(val first: RuleTest, val second: RuleTest) : RuleTest
}

fun RuleTest.matches(kind: FruitKind): Boolean = when (this) {
    is RuleTest.ByColor -> kind.color == color
    is RuleTest.ByShape -> kind.shape == shape
    is RuleTest.ByStripe -> kind.striped == striped
    is RuleTest.Both -> first.matches(kind) && second.matches(kind)
}

data class SortRule(
    val primaryTest: RuleTest,
    val secondaryTest: RuleTest,
    val primaryLabel: CrateLabel,
    val secondaryLabel: CrateLabel
) {
    val labels: List<CrateLabel> = listOf(primaryLabel, secondaryLabel, CrateLabel.OTHER)

    fun crateIndexFor(kind: FruitKind): Int = when {
        primaryTest.matches(kind) -> 0
        secondaryTest.matches(kind) -> 1
        else -> 2
    }
}
