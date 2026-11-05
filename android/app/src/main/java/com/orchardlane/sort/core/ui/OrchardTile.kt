package com.orchardlane.sort.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orchardlane.sort.core.theme.FreshGreen
import com.orchardlane.sort.core.theme.OrchardBorder
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary

@Composable
fun OrchardTile(
    name: String,
    spriteIndex: Int,
    lanesDone: Int,
    laneCount: Int,
    unlocked: Boolean,
    progressLabel: String,
    stateLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && unlocked) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 420f)
    )
    val shape = RoundedCornerShape(24.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(8.dp, shape, spotColor = SageDeep.copy(alpha = 0.35f))
            .background(OrchardSurface.copy(alpha = 0.92f), shape)
            .border(2.dp, OrchardBorder, shape)
            .clickable(
                enabled = unlocked,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .semantics {
                contentDescription = name
                stateDescription = stateLabel
            }
            .alpha(if (unlocked) 1f else 0.55f)
            .heightIn(min = 78.dp)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(spriteResFor(spriteIndex)),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 14.sp,
                lineHeight = 17.sp,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            MeterBar(
                fraction = if (laneCount == 0) 0f else lanesDone.toFloat() / laneCount,
                trackColor = FreshGreen,
                fillColor = SageDeep,
                height = 6.dp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = progressLabel,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
        if (!unlocked) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = AppIcons.Lock,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
