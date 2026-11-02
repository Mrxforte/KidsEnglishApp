package com.kidsenglish.app.core.designsystem.color

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Centralized semantic color tokens for Kids English.
 * Tailored for high-engagement, child-friendly contrast and soft pastel aesthetics.
 */
object AppColors {
    // Brand Primaries
    val PrimarySky = Color(0xFF4FC3F7)
    val PrimaryNavy = Color(0xFF1565C0)
    val PrimaryDeep = Color(0xFF0D47A1)

    // Soft Pastels for Backgrounds & Gradients
    val SoftSky = Color(0xFFE1F5FE)
    val SoftMint = Color(0xFFE8F5E9)
    val SoftSunny = Color(0xFFFFF9C4)
    val SoftPeach = Color(0xFFFFE0B2)
    val SoftLavender = Color(0xFFEDE7F6)

    // Progression & Rewards
    val StarGold = Color(0xFFFFC107)
    val StarGoldDeep = Color(0xFFFFA000)
    val StreakFlame = Color(0xFFFF5722)
    val CoinAmber = Color(0xFFFFB300)

    // Functional State Colors
    val SuccessGreen = Color(0xFF4CAF50)
    val SuccessGreenDeep = Color(0xFF2E7D32)
    val ErrorCoral = Color(0xFFFF5252)
    val ErrorDeep = Color(0xFFD32F2F)
    val WarningOrange = Color(0xFFFF9800)
    val InfoBlue = Color(0xFF29B6F6)
    val LockedGray = Color(0xFFB0BEC5)
    val LockedDark = Color(0xFF78909C)

    // Text & Surfaces
    val TextPrimary = Color(0xFF263238)
    val TextSecondary = Color(0xFF546E7A)
    val TextMuted = Color(0xFF90A4AE)
    val SurfaceWhite = Color(0xFFFFFFFF)
    val SurfaceCard = Color(0xFFFCFDFE)
    val CardBorder = Color(0xFFE2E8F0)

    // Gradients
    val SkyToGrassGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF87CEEB), // Clear Sky
            Color(0xFFA5D6A7), // Soft Meadow
            Color(0xFF81C784)  // Lush Green
        )
    )

    val GoldBannerGradient = Brush.horizontalGradient(
        listOf(Color(0xFFFFD54F), Color(0xFFFFB300))
    )

    val CoralBannerGradient = Brush.horizontalGradient(
        listOf(Color(0xFFFF8A65), Color(0xFFFF5722))
    )

    val RainbowProgressGradient = Brush.horizontalGradient(
        listOf(Color(0xFF42A5F5), Color(0xFF66BB6A), Color(0xFFFFCA28))
    )
}

/**
 * Topic & Lesson specific colorful accents
 */
object LessonColors {
    val AccentApple = Color(0xFFEF5350)
    val AccentBall = Color(0xFF42A5F5)
    val AccentCat = Color(0xFFFFB74D)
    val AccentDog = Color(0xFFAB47BC)
    val AccentNature = Color(0xFF66BB6A)
    val AccentNumbers = Color(0xFF26C6DA)
}
