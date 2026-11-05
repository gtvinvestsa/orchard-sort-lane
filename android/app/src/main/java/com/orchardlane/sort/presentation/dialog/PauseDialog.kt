package com.orchardlane.sort.presentation.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orchardlane.sort.R
import com.orchardlane.sort.core.theme.OrchardSurface
import com.orchardlane.sort.core.theme.TextPrimary
import com.orchardlane.sort.core.theme.TextSecondary
import com.orchardlane.sort.core.ui.PrimaryButton
import com.orchardlane.sort.core.ui.SecondaryButton
import com.orchardlane.sort.core.ui.AppIcons

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(30.dp),
        containerColor = OrchardSurface,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(R.string.resume),
                icon = AppIcons.PlayArrow,
                onClick = onResume,
                height = 52.dp
            )
        },
        dismissButton = {
            SecondaryButton(
                text = stringResource(R.string.menu),
                icon = AppIcons.Home,
                onClick = onMenu,
                height = 52.dp
            )
        }
    )
}
