package com.kidsenglish.app.core.designsystem.dimens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized dimensions and spacing for Kids English.
 * Following 4dp/8dp grid system and Android Accessibility guidelines (>= 48dp touch targets).
 */
object Dimens {
    // Spacing
    val SpaceXXS: Dp = 2.dp
    val SpaceXS: Dp = 4.dp
    val SpaceS: Dp = 8.dp
    val SpaceM: Dp = 16.dp
    val SpaceL: Dp = 24.dp
    val SpaceXL: Dp = 32.dp
    val SpaceXXL: Dp = 48.dp
    val SpaceHero: Dp = 64.dp

    // Corner Radii
    val RadiusXS: Dp = 6.dp
    val RadiusS: Dp = 10.dp
    val RadiusM: Dp = 18.dp
    val RadiusL: Dp = 26.dp
    val RadiusXL: Dp = 34.dp
    val RadiusPill: Dp = 999.dp

    // Component Dimensions
    val TouchTargetMin: Dp = 48.dp
    val ButtonHeight: Dp = 56.dp
    val ButtonSmallHeight: Dp = 44.dp
    val NodeSizeDefault: Dp = 80.dp
    val NodeSizeSpecial: Dp = 92.dp
    val MascotSizeSmall: Dp = 64.dp
    val MascotSizeMedium: Dp = 120.dp
    val MascotSizeLarge: Dp = 180.dp

    // Icons
    val IconSizeXS: Dp = 16.dp
    val IconSizeS: Dp = 20.dp
    val IconSizeM: Dp = 28.dp
    val IconSizeL: Dp = 36.dp
    val IconSizeXL: Dp = 48.dp

    // Elevation & Shadows
    val ElevationNone: Dp = 0.dp
    val ElevationLow: Dp = 3.dp
    val ElevationMedium: Dp = 6.dp
    val ElevationHigh: Dp = 12.dp

    // Border Widths
    val BorderThin: Dp = 1.dp
    val BorderMedium: Dp = 2.dp
    val BorderThick: Dp = 4.dp
}
