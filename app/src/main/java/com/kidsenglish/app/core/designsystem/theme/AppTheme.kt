package com.kidsenglish.app.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.shapes.AppShapes
import com.kidsenglish.app.core.designsystem.typography.AppTypography

/**
 * Default light color scheme crafted for the playful Kids English identity.
 */
private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    primaryContainer = AppColors.PrimaryContainer,
    onPrimaryContainer = AppColors.OnPrimaryContainer,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.OnSecondary,
    secondaryContainer = AppColors.SecondaryContainer,
    onSecondaryContainer = AppColors.OnSecondaryContainer,
    tertiary = AppColors.Tertiary,
    onTertiary = AppColors.OnTertiary,
    background = AppColors.Background,
    onBackground = AppColors.TextPrimary,
    surface = AppColors.Surface,
    onSurface = AppColors.TextPrimary,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.TextSecondary,
    outline = AppColors.Outline,
    error = AppColors.Error,
    onError = AppColors.OnPrimary
)

/**
 * Architectural dark color scheme support for future dark-mode preferences.
 */
private val DarkColorScheme = darkColorScheme(
    primary = AppColors.PrimaryDark,
    onPrimary = AppColors.BackgroundDark,
    secondary = AppColors.SecondaryDark,
    onSecondary = AppColors.BackgroundDark,
    tertiary = AppColors.Tertiary,
    onTertiary = AppColors.OnTertiary,
    background = AppColors.BackgroundDark,
    onBackground = AppColors.TextPrimaryDark,
    surface = AppColors.SurfaceDark,
    onSurface = AppColors.TextPrimaryDark,
    onSurfaceVariant = AppColors.TextSecondaryDark,
    error = AppColors.Error
)

/**
 * Centralized Material 3 Theme wrapper for the Kids English application.
 *
 * Uses the light theme as the initial default while supporting dark theme architecturally.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
