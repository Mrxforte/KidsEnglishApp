package com.kidsenglish.app.core.designsystem.dimensions

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized dimension, spacing, radius, and typography size tokens.
 *
 * Configured for both the Splash Screen and the Horizontal (Landscape) Levels Roadmap Template.
 */
object AppDimensions {

    // =========================================================================
    // Global Spacing Scale
    // =========================================================================

    val SpaceXXS: Dp = 4.dp
    val SpaceXS: Dp = 8.dp
    val SpaceS: Dp = 12.dp
    val SpaceM: Dp = 16.dp
    val SpaceL: Dp = 24.dp
    val SpaceXL: Dp = 32.dp
    val SpaceXXL: Dp = 48.dp

    // =========================================================================
    // Screen Layout & Responsive Constraints
    // =========================================================================

    val ScreenHorizontalPadding: Dp = 20.dp
    val ScreenVerticalPadding: Dp = 12.dp
    val MaxContentWidth: Dp = 720.dp

    // =========================================================================
    // Corner Radii & Borders
    // =========================================================================

    val RadiusSmall: Dp = 6.dp
    val RadiusMedium: Dp = 12.dp
    val RadiusLarge: Dp = 24.dp
    val RadiusExtraLarge: Dp = 36.dp
    val RadiusPill: Dp = 999.dp

    val BorderThin: Dp = 1.5.dp
    val BorderMedium: Dp = 3.dp
    val BorderThick: Dp = 4.5.dp

    // =========================================================================
    // Splash Screen Specific Dimensions (Landscape Optimized)
    // =========================================================================

    val SplashMascotHaloSize: Dp = 176.dp
    val SplashMascotCardSize: Dp = 144.dp
    val SplashMascotImageSize: Dp = 114.dp
    val SplashCornerLogoBadgeSize: Dp = 44.dp
    val SplashCornerLogoIconSize: Dp = 28.dp
    val SplashTitleTopSpacing: Dp = 16.dp
    val SplashSubtitleTopSpacing: Dp = 6.dp
    val SplashBadgeHorizontalPadding: Dp = 16.dp
    val SplashBadgeVerticalPadding: Dp = 6.dp
    val SplashLoadingBarWidth: Dp = 220.dp
    val SplashLoadingBarHeight: Dp = 8.dp
    val SplashSparkleSizeSmall: Dp = 20.dp
    val SplashSparkleSizeMedium: Dp = 28.dp
    val SplashCloudWidth: Dp = 76.dp
    val SplashCloudHeight: Dp = 32.dp
    val SplashFloatOffsetDp: Dp = 6.dp

    // =========================================================================
    // Horizontal Levels Roadmap Template Dimensions
    // =========================================================================

    // Top HUD Bar height and button dimensions
    val RoadmapHudHeight: Dp = 64.dp
    val HudHexButtonSize: Dp = 52.dp
    val HudHexIconSize: Dp = 26.dp
    val HudStickerBannerWidth: Dp = 172.dp
    val HudStickerBannerHeight: Dp = 42.dp
    val HudRibbonHeight: Dp = 48.dp
    val HudRibbonHorizontalPadding: Dp = 22.dp
    val HudMascotHeaderWidth: Dp = 170.dp
    val HudMascotHeaderHeight: Dp = 58.dp

    // 3x6 All-Topics Tilted Card Grid (Screenshot 1)
    val TopicGridCardWidth: Dp = 130.dp
    val TopicGridCardHeight: Dp = 82.dp
    val TopicGridColumnSpacing: Dp = 28.dp
    val TopicGridRowSpacing: Dp = 20.dp
    val TopicGridIconSize: Dp = 32.dp
    val TopicGridIconBadgeSize: Dp = 24.dp
    val TopicGridProgressHeight: Dp = 10.dp
    val CardFloatAmplitude: Dp = 3.dp

    // Horizontal Roadmap Section: Tall Topic Banner Card ("COLOURS", "FREE", "ABC")
    val TopicBannerWidth: Dp = 172.dp
    val TopicBannerHeight: Dp = 232.dp
    val TopicBannerIconSize: Dp = 70.dp
    val TopicBannerProgressHeight: Dp = 16.dp
    val TopicMascotWidth: Dp = 104.dp
    val TopicMascotHeight: Dp = 204.dp
    val MascotToBannerSpacing: Dp = 34.dp

    // Horizontal Roadmap Section: 2-Row Illustrated Activity Card
    val ActivityCardWidth: Dp = 188.dp
    val ActivityCardHeight: Dp = 102.dp
    val ActivityCardRowSpacing: Dp = 26.dp
    val ActivityCardColumnSpacing: Dp = 34.dp
    val ActivityPadlockSize: Dp = 36.dp

    // Giant ABC Tracing Board Card on Roadmap (Screenshot 3 right side)
    val AbcBoardCardWidth: Dp = 268.dp
    val AbcBoardCardHeight: Dp = 230.dp

    // Fine-grained Roadmap Card & HUD internal dimensions
    val CardShadowOffset: Dp = 5.dp
    val CardInnerPadding: Dp = 6.dp
    val CardProgressCornerRadius: Dp = 4.dp
    val HudStickerHexSize: Dp = 48.dp
    val HudStickerRibbonOverlap: Dp = 14.dp
    val HudTopPadding: Dp = 8.dp
    val RoadmapContentVerticalPadding: Dp = 10.dp
    val RoadmapSectionSpacing: Dp = 56.dp
    val FreeStarburstSize: Dp = 96.dp
    val ModalIconSize: Dp = 56.dp
    val ModalPreviewCardHeight: Dp = 136.dp

    // Dialog & Modal Dimensions
    val ModalMaxWidth: Dp = 420.dp
    val MinTouchTargetSize: Dp = 48.dp
    val ButtonHeight: Dp = 52.dp

    // Elevations
    val ElevationNone: Dp = 0.dp
    val ElevationLow: Dp = 4.dp
    val ElevationMedium: Dp = 8.dp
    val ElevationHigh: Dp = 14.dp

    // =========================================================================
    // Typography Font Sizes & Line Heights
    // =========================================================================

    val TextSizeDisplayLarge: TextUnit = 36.sp
    val LineHeightDisplayLarge: TextUnit = 42.sp

    val TextSizeDisplayMedium: TextUnit = 28.sp
    val LineHeightDisplayMedium: TextUnit = 34.sp

    val TextSizeHeadlineLarge: TextUnit = 22.sp
    val LineHeightHeadlineLarge: TextUnit = 28.sp

    val TextSizeTitleLarge: TextUnit = 18.sp
    val LineHeightTitleLarge: TextUnit = 24.sp

    val TextSizeTitleMedium: TextUnit = 14.sp
    val LineHeightTitleMedium: TextUnit = 18.sp

    val TextSizeBodyLarge: TextUnit = 15.sp
    val LineHeightBodyLarge: TextUnit = 20.sp

    val TextSizeBodyMedium: TextUnit = 13.sp
    val LineHeightBodyMedium: TextUnit = 18.sp

    val TextSizeLabelLarge: TextUnit = 15.sp
    val LineHeightLabelLarge: TextUnit = 18.sp
}
