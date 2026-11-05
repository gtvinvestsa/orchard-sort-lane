package com.orchardlane.sort.core.ui

import androidx.annotation.DrawableRes
import com.orchardlane.sort.R

@DrawableRes
fun spriteResFor(index: Int): Int = when (index) {
    1 -> R.drawable.sprite_1
    2 -> R.drawable.sprite_2
    3 -> R.drawable.sprite_3
    4 -> R.drawable.sprite_4
    else -> R.drawable.sprite_5
}
