package com.orchardlane.sort.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OrchardColorScheme = lightColorScheme(
    primary = AppleRed,
    onPrimary = Color.White,
    primaryContainer = AppleRedLight,
    onPrimaryContainer = Color.White,
    secondary = SkyBlue,
    onSecondary = Color.White,
    secondaryContainer = FreshGreenLight,
    onSecondaryContainer = TextPrimary,
    tertiary = SageDeep,
    onTertiary = Color.White,
    tertiaryContainer = FreshGreen,
    onTertiaryContainer = TextPrimary,
    background = OrchardBackground,
    onBackground = TextPrimary,
    surface = OrchardSurface,
    onSurface = TextPrimary,
    surfaceVariant = OrchardSurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = OrchardBorder,
    outlineVariant = FreshGreen,
    error = Terracotta,
    onError = Color.White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OrchardColorScheme,
        typography = AppTypography,
        content = content
    )
}
