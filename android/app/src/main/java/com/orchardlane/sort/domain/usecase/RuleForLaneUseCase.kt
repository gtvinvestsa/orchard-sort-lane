package com.orchardlane.sort.domain.usecase

import com.orchardlane.sort.domain.model.CrateLabel
import com.orchardlane.sort.domain.model.FruitColor
import com.orchardlane.sort.domain.model.FruitShape
import com.orchardlane.sort.domain.model.RuleTest
import com.orchardlane.sort.domain.model.SortRule

class RuleForLaneUseCase {

    operator fun invoke(laneNumber: Int): SortRule {
        val block = ((laneNumber - 1) / 10).coerceIn(0, TABLE.lastIndex)
        return TABLE[block]
    }

    private companion object {
        val TABLE: List<SortRule> = listOf(
            SortRule(
                RuleTest.ByColor(FruitColor.RED),
                RuleTest.ByColor(FruitColor.GREEN),
                CrateLabel.RED,
                CrateLabel.GREEN
            ),
            SortRule(
                RuleTest.ByColor(FruitColor.GREEN),
                RuleTest.ByColor(FruitColor.YELLOW),
                CrateLabel.GREEN,
                CrateLabel.YELLOW
            ),
            SortRule(
                RuleTest.ByShape(FruitShape.ROUND),
                RuleTest.ByShape(FruitShape.LONG),
                CrateLabel.ROUND,
                CrateLabel.LONG
            ),
            SortRule(
                RuleTest.ByStripe(true),
                RuleTest.ByStripe(false),
                CrateLabel.STRIPED,
                CrateLabel.PLAIN
            ),
            SortRule(
                RuleTest.ByColor(FruitColor.YELLOW),
                RuleTest.ByColor(FruitColor.RED),
                CrateLabel.YELLOW,
                CrateLabel.RED
            ),
            SortRule(
                RuleTest.ByShape(FruitShape.LONG),
                RuleTest.ByShape(FruitShape.ROUND),
                CrateLabel.LONG,
                CrateLabel.ROUND
            ),
            SortRule(
                RuleTest.Both(RuleTest.ByStripe(true), RuleTest.ByShape(FruitShape.ROUND)),
                RuleTest.ByStripe(true),
                CrateLabel.STRIPED_ROUND,
                CrateLabel.STRIPED
            ),
            SortRule(
                RuleTest.Both(RuleTest.ByColor(FruitColor.RED), RuleTest.ByShape(FruitShape.ROUND)),
                RuleTest.ByColor(FruitColor.RED),
                CrateLabel.RED_ROUND,
                CrateLabel.RED
            ),
            SortRule(
                RuleTest.Both(RuleTest.ByColor(FruitColor.GREEN), RuleTest.ByStripe(true)),
                RuleTest.ByColor(FruitColor.GREEN),
                CrateLabel.GREEN_STRIPED,
                CrateLabel.GREEN
            ),
            SortRule(
                RuleTest.Both(RuleTest.ByColor(FruitColor.YELLOW), RuleTest.ByShape(FruitShape.LONG)),
                RuleTest.ByColor(FruitColor.YELLOW),
                CrateLabel.YELLOW_LONG,
                CrateLabel.YELLOW
            ),
            SortRule(
                RuleTest.Both(RuleTest.ByColor(FruitColor.RED), RuleTest.ByStripe(true)),
                RuleTest.ByStripe(true),
                CrateLabel.RED_STRIPED,
                CrateLabel.STRIPED
            ),
            SortRule(
                RuleTest.Both(RuleTest.ByColor(FruitColor.GREEN), RuleTest.ByShape(FruitShape.ROUND)),
                RuleTest.ByShape(FruitShape.ROUND),
                CrateLabel.GREEN_ROUND,
                CrateLabel.ROUND
            )
        )
    }
}
