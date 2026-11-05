package com.orchardlane.sort.presentation.faces

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orchardlane.sort.R
import com.orchardlane.sort.core.di.AppViewModelFactory
import com.orchardlane.sort.core.theme.AppleRed
import com.orchardlane.sort.core.theme.OrchardBorder
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.PanelOverlay
import com.orchardlane.sort.core.theme.SageDeep
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.FruitSprite
import com.orchardlane.sort.core.ui.ScreenBackground
import com.orchardlane.sort.core.ui.SecondaryButton
import com.orchardlane.sort.domain.model.FaceSet
import com.orchardlane.sort.core.ui.AppIcons

@Composable
fun FruitFacesRoute(onMenu: () -> Unit) {
    val facesViewModel: FruitFacesViewModel = viewModel(factory = AppViewModelFactory.faces())
    val state by facesViewModel.state.collectAsStateWithLifecycle()
    FruitFacesScreen(
        state = state,
        onFaceSelected = facesViewModel::onFaceSetSelected,
        onMenu = onMenu
    )
}

@Composable
fun FruitFacesScreen(
    state: FruitFacesUiState,
    onFaceSelected: (FaceSet) -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(overlay = PanelOverlay) {
        if (state.loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = AppleRed)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.faces_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.faces_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    state.cards.chunked(2).forEach { pair ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            pair.forEach { card ->
                                FaceTile(
                                    card = card,
                                    onSelected = { onFaceSelected(card.faceSet) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    icon = AppIcons.Home,
                    onClick = onMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun FaceTile(
    card: FaceCard,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(28.dp)
    val name = faceSetName(card.faceSet)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .shadow(10.dp, shape, spotColor = SageDeep.copy(alpha = 0.35f))
            .background(OrchardSurface.copy(alpha = 0.95f), shape)
            .border(
                if (card.selected) 3.dp else 2.dp,
                if (card.selected) AppleRed else OrchardBorder,
                shape
            )
            .clickable(enabled = card.unlocked, onClick = onSelected)
            .semantics {
                contentDescription = name
                stateDescription = name
            }
            .alpha(if (card.unlocked) 1f else 0.5f)
            .padding(10.dp)
    ) {
        FruitSprite(
            spriteIndex = card.faceSet.spriteIndex,
            size = 76.dp,
            description = null,
            idle = card.selected
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary
            )
            if (card.selected) {
                Spacer(modifier = Modifier.size(6.dp))
                Icon(
                    imageVector = AppIcons.Check,
                    contentDescription = null,
                    tint = AppleRed,
                    modifier = Modifier.size(20.dp)
                )
            } else if (!card.unlocked) {
                Spacer(modifier = Modifier.size(6.dp))
                Icon(
                    imageVector = AppIcons.Lock,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        if (!card.unlocked) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.face_unlock_at, card.faceSet.unlockLane),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun faceSetName(faceSet: FaceSet): String = when (faceSet) {
    FaceSet.BLINKY -> stringResource(R.string.face_blinky)
    FaceSet.SLEEPY -> stringResource(R.string.face_sleepy)
    FaceSet.CHEEKY -> stringResource(R.string.face_cheeky)
    FaceSet.STARRY -> stringResource(R.string.face_starry)
}
