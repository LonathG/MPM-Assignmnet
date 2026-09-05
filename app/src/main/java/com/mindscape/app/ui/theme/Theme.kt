package com.mindscape.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = MindPrimaryAccent,
    onPrimary = MindCardSurface,
    primaryContainer = MindPrimaryAccentLight,
    onPrimaryContainer = MindPrimaryAccent,
    secondary = MindLimeAccent,
    onSecondary = MindLimeAccentDark,
    background = MindBackground,
    onBackground = MindTextPrimary,
    surface = MindCardSurface,
    onSurface = MindTextPrimary,
    surfaceVariant = MindSubtleContainer,
    onSurfaceVariant = MindTextSecondary,
    outline = MindBorderColor
)

@Composable
fun MindScapeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
