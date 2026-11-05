package com.orchardlane.sort.domain.model

enum class FruitColor { RED, GREEN, YELLOW, PURPLE }

enum class FruitShape { ROUND, LONG }

enum class FruitKind(
    val color: FruitColor,
    val shape: FruitShape,
    val striped: Boolean,
    val spriteIndex: Int
) {
    APPLE(FruitColor.RED, FruitShape.ROUND, false, 1),
    PEAR(FruitColor.GREEN, FruitShape.LONG, false, 2),
    PLUM(FruitColor.PURPLE, FruitShape.ROUND, true, 3),
    LEMON(FruitColor.YELLOW, FruitShape.LONG, false, 4),
    BERRY(FruitColor.RED, FruitShape.ROUND, true, 5)
}
