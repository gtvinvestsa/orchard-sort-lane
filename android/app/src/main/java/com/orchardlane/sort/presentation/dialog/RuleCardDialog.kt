package com.orchardlane.sort.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orchardlane.sort.R
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.FruitSprite
import com.orchardlane.sort.core.ui.PrimaryButton
import com.orchardlane.sort.core.ui.RuleGlyph
import com.orchardlane.sort.core.ui.crateAccentFor
import com.orchardlane.sort.core.ui.crateGlyphFor
import com.orchardlane.sort.core.ui.crateLabelText
import com.orchardlane.sort.domain.model.CrateLabel

@Composable
fun RuleCardDialog(
    label: CrateLabel?,
    spriteIndex: Int,
    onDismiss: () -> Unit
) {
    val target = label ?: CrateLabel.OTHER
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(32.dp),
        containerColor = OrchardSurface,
        title = {
            Text(
                text = stringResource(R.string.rule_card_title),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                FruitSprite(
                    spriteIndex = spriteIndex,
                    size = 120.dp,
                    description = stringResource(R.string.cd_fruit),
                    idle = false
                )
                Spacer(modifier = Modifier.height(12.dp))
                RuleGlyph(
                    glyph = crateGlyphFor(target),
                    accent = crateAccentFor(target),
                    modifier = Modifier.size(46.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.rule_card_text, crateLabelText(target)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onDismiss,
                height = 52.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
