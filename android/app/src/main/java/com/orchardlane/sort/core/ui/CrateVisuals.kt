package com.orchardlane.sort.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.orchardlane.sort.R
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.FreshGreen
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.Terracotta
import com.orchardlane.sort.domain.model.CrateLabel

enum class CrateGlyph { CIRCLE, PILL, STRIPES, CHIP, MIXED }

fun crateGlyphFor(label: CrateLabel): CrateGlyph = when (label) {
    CrateLabel.RED, CrateLabel.GREEN, CrateLabel.YELLOW, CrateLabel.PLAIN -> CrateGlyph.CHIP
    CrateLabel.ROUND, CrateLabel.RED_ROUND, CrateLabel.GREEN_ROUND -> CrateGlyph.CIRCLE
    CrateLabel.LONG, CrateLabel.YELLOW_LONG -> CrateGlyph.PILL
    CrateLabel.STRIPED, CrateLabel.RED_STRIPED, CrateLabel.GREEN_STRIPED -> CrateGlyph.STRIPES
    CrateLabel.STRIPED_ROUND -> CrateGlyph.MIXED
    CrateLabel.OTHER -> CrateGlyph.MIXED
}

fun crateAccentFor(label: CrateLabel): Color = when (label) {
    CrateLabel.RED, CrateLabel.RED_ROUND, CrateLabel.RED_STRIPED -> AppleRed
    CrateLabel.GREEN, CrateLabel.GREEN_ROUND, CrateLabel.GREEN_STRIPED -> SageDeep
    CrateLabel.YELLOW, CrateLabel.YELLOW_LONG -> Terracotta
    else -> FreshGreen
}

@Composable
fun crateLabelText(label: CrateLabel): String = when (label) {
    CrateLabel.RED -> stringResource(R.string.crate_red)
    CrateLabel.GREEN -> stringResource(R.string.crate_green)
    CrateLabel.YELLOW -> stringResource(R.string.crate_yellow)
    CrateLabel.ROUND -> stringResource(R.string.crate_round)
    CrateLabel.LONG -> stringResource(R.string.crate_long)
    CrateLabel.STRIPED -> stringResource(R.string.crate_striped)
    CrateLabel.PLAIN -> stringResource(R.string.crate_plain)
    CrateLabel.OTHER -> stringResource(R.string.crate_other)
    CrateLabel.RED_ROUND -> stringResource(R.string.crate_red_round)
    CrateLabel.STRIPED_ROUND -> stringResource(R.string.crate_striped_round)
    CrateLabel.GREEN_STRIPED -> stringResource(R.string.crate_green_striped)
    CrateLabel.YELLOW_LONG -> stringResource(R.string.crate_yellow_long)
    CrateLabel.RED_STRIPED -> stringResource(R.string.crate_red_striped)
    CrateLabel.GREEN_ROUND -> stringResource(R.string.crate_green_round)
}

@Composable
fun RuleGlyph(
    glyph: CrateGlyph,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val side = minOf(size.width, size.height)
        val box = side * 0.72f
        val origin = Offset((size.width - box) / 2f, (size.height - box) / 2f)
        when (glyph) {
            CrateGlyph.CIRCLE -> drawCircle(
                color = accent,
                radius = box / 2f,
                center = Offset(size.width / 2f, size.height / 2f)
            )

            CrateGlyph.PILL -> drawRoundRect(
                color = accent,
                topLeft = Offset(origin.x + box * 0.26f, origin.y),
                size = Size(box * 0.48f, box),
                cornerRadius = CornerRadius(box * 0.24f, box * 0.24f)
            )

            CrateGlyph.STRIPES -> {
                val bar = box / 5f
                for (index in 0 until 3) {
                    drawRoundRect(
                        color = if (index % 2 == 0) accent else accent.copy(alpha = 0.35f),
                        topLeft = Offset(origin.x, origin.y + index * bar * 1.7f),
                        size = Size(box, bar),
                        cornerRadius = CornerRadius(bar / 2f, bar / 2f)
                    )
                }
            }

            CrateGlyph.CHIP -> drawRoundRect(
                color = accent,
                topLeft = origin,
                size = Size(box, box),
                cornerRadius = CornerRadius(box * 0.3f, box * 0.3f)
            )

            CrateGlyph.MIXED -> {
                drawCircle(
                    color = accent,
                    radius = box * 0.26f,
                    center = Offset(origin.x + box * 0.28f, origin.y + box * 0.28f)
                )
                drawRoundRect(
                    color = accent.copy(alpha = 0.55f),
                    topLeft = Offset(origin.x + box * 0.4f, origin.y + box * 0.44f),
                    size = Size(box * 0.52f, box * 0.52f),
                    cornerRadius = CornerRadius(box * 0.16f, box * 0.16f)
                )
            }
        }
    }
}
