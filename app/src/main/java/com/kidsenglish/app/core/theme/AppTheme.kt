package com.kidsenglish.app.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.kidsenglish.app.core.designsystem.color.AppColors
import com.kidsenglish.app.core.designsystem.shape.AppShapes
import com.kidsenglish.app.core.designsystem.typography.AppTypography

private val LightColorScheme = lightColorScheme(
    primary = AppColors.PrimarySky,
    onPrimary = AppColors.SurfaceWhite,
    primaryContainer = AppColors.SoftSky,
    onPrimaryContainer = AppColors.PrimaryNavy,
    secondary = AppColors.StarGold,
    onSecondary = AppColors.TextPrimary,
    secondaryContainer = AppColors.SoftSunny,
    onSecondaryContainer = AppColors.TextPrimary,
    tertiary = AppColors.StreakFlame,
    onTertiary = AppColors.SurfaceWhite,
    background = AppColors.SoftSky,
    onBackground = AppColors.TextPrimary,
    surface = AppColors.SurfaceWhite,
    onSurface = AppColors.TextPrimary,
    surfaceVariant = AppColors.SoftMint,
    onSurfaceVariant = AppColors.TextSecondary,
    outline = AppColors.CardBorder,
    error = AppColors.ErrorCoral,
    onError = AppColors.SurfaceWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.PrimarySky,
    onPrimary = AppColors.TextPrimary,
    primaryContainer = AppColors.PrimaryNavy,
    onPrimaryContainer = AppColors.SoftSky,
    secondary = AppColors.StarGold,
    onSecondary = AppColors.TextPrimary,
    background = AppColors.TextPrimary,
    onBackground = AppColors.SurfaceWhite,
    surface = AppColors.TextSecondary,
    onSurface = AppColors.SurfaceWhite,
    error = AppColors.ErrorCoral
)

/**
 * Main application theme wrapper for Kids English.
 */
@Composable
fun KidsEnglishAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
