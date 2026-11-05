package com.orchardlane.sort.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orchardlane.sort.core.theme.FreshGreen
import com.orchardlane.sort.core.theme.FreshGreenLight
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.SkyBlue
import com.orchardlane.sort.core.theme.Terracotta
import com.orchardlane.sort.core.theme.TopLight

@Composable
fun CrateBox(
    label: String,
    glyph: CrateGlyph,
    accent: Color,
    fillFraction: Float,
    description: String,
    stateLabel: String,
    modifier: Modifier = Modifier,
    shaking: Boolean = false,
    highlighted: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val wobble by animateFloatAsState(
        targetValue = if (shaking) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.3f, stiffness = 900f)
    )
    val lift by animateFloatAsState(
        targetValue = if (highlighted) 1.04f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 320f)
    )
    val shape = RoundedCornerShape(
        topStart = 10.dp,
        topEnd = 10.dp,
        bottomStart = 22.dp,
        bottomEnd = 22.dp
    )
    val clickModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .graphicsLayer {
                rotationZ = wobble * 4f
                scaleX = lift
                scaleY = lift
            }
            .shadow(12.dp, shape, spotColor = SageDeep.copy(alpha = 0.45f))
            .background(
                Brush.verticalGradient(listOf(FreshGreenLight, FreshGreen, SageDeep)),
                shape
            )
            .border(
                if (highlighted) 4.dp else 3.dp,
                if (highlighted) accent else SageDeep,
                shape
            )
            .then(clickModifier)
            .semantics {
                contentDescription = description
                stateDescription = stateLabel
            }
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(SkyBlue, RoundedCornerShape(14.dp))
                .border(2.dp, TopLight, RoundedCornerShape(14.dp))
                .padding(horizontal = 6.dp, vertical = 5.dp)
        ) {
            RuleGlyph(
                glyph = glyph,
                accent = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color(0x337FA46B), RoundedCornerShape(2.dp))
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color(0x337FA46B), RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            MeterBar(
                fraction = fillFraction,
                trackColor = Color(0x3D2B3A1E),
                fillColor = Terracotta,
                height = 10.dp,
                modifier = Modifier
            )
        }
    }
}
