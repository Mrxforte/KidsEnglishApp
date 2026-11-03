package com.kidsenglish.app.core.designsystem.colors

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Centralized color palette for the Kids English Design System.
 *
 * All UI components and screens must reference semantic or feature-specific tokens
 * from [AppColors] or `MaterialTheme.colorScheme` rather than defining inline hex values.
 */
object AppColors {

    // =========================================================================
    // Splash Screen Colors
    // =========================================================================

    // Splash Screen: Top sky-blue gradient stop for full-screen background
    val SplashBackgroundTop = Color(0xFF0099E5)

    // Splash Screen: Middle azure-blue gradient stop for full-screen background
    val SplashBackgroundMiddle = Color(0xFF2874C5)

    // Splash Screen: Bottom deep indigo-purple gradient stop matching horizontal roadmap
    val SplashBackgroundBottom = Color(0xFF5352A8)

    // Splash Screen: Radiant golden halo behind the central mascot illustration
    val SplashMascotHalo = Color(0x33FFD54F)

    // Splash Screen: Pure white surface for the central mascot medallion card
    val SplashMascotCardSurface = Color(0xFFFFFFFF)

    // Splash Screen: Soft sunny border ring around the central mascot medallion
    val SplashMascotCardBorder = Color(0xFFFFD54F)

    // Splash Screen: Crisp white color for the main "Kids English" brand title on blue sky
    val SplashTitle = Color(0xFFFFFFFF)

    // Splash Screen: Soft cream-sky color for the friendly educational subtitle
    val SplashSubtitle = Color(0xFFE1F5FE)

    // Splash Screen: Soft golden-amber background for the top welcome pill badge
    val SplashBadgeBackground = Color(0xFFFFF8E1)

    // Splash Screen: Warm amber border for the top welcome pill badge
    val SplashBadgeBorder = Color(0xFFFFCA28)

    // Splash Screen: Rich cocoa-brown text color inside the top welcome pill badge
    val SplashBadgeText = Color(0xFF5D4037)

    // Splash Screen: Floating decorative star and sparkle tint
    val SplashSparkleGold = Color(0xFFFFD54F)

    // Splash Screen: Floating decorative cloud bubble tint
    val SplashCloudWhite = Color(0x33FFFFFF)

    // Splash Screen: Subtle progress track background on the bottom loading card
    val SplashLoadingTrack = Color(0x4DFFFFFF)

    // Splash Screen: Active progress bar indicator color on the bottom loading card
    val SplashLoadingIndicator = Color(0xFFFFD54F)

    // =========================================================================
    // Horizontal Levels Roadmap Template Colors
    // =========================================================================

    // Roadmap Background: Top vibrant cyan-blue gradient stop
    val RoadmapBgTop = Color(0xFF009BE8)

    // Roadmap Background: Bottom lavender-indigo gradient stop
    val RoadmapBgBottom = Color(0xFF5C58A6)

    // Roadmap Background: Subtle watermarked alphabet letters overlay color
    val RoadmapWatermarkLetter = Color(0x18002B5E)

    // Roadmap HUD: Blue hexagonal grid toggle button fill
    val HudHexBlueFill = Color(0xFF1967D2)
    val HudHexBlueBorder = Color(0xFF4285F4)
    val HudHexBlueShadow = Color(0xFF0D3B82)

    // Roadmap HUD: Grey hexagonal settings button fill
    val HudHexGreyFill = Color(0xFF8D949E)
    val HudHexGreyBorder = Color(0xFFB0B7C0)
    val HudHexGreyShadow = Color(0xFF5A6068)

    // Roadmap HUD: Purple hexagonal puzzle sticker badge fill
    val HudStickerHexFill = Color(0xFF9C27B0)
    val HudStickerHexBorder = Color(0xFFCE93D8)
    val HudStickerBannerBg = Color(0x8815489C)
    val HudStickerRibbonTail = Color(0xFF1A4594)

    // Roadmap HUD: Top-right crimson-red mode/reward ribbon banner
    val HudCrimsonRibbonTop = Color(0xFFE91E63)
    val HudCrimsonRibbonBottom = Color(0xFFC2185B)
    val HudCrimsonRibbonShadow = Color(0xFF880E4F)

    // Roadmap 3x6 Topic Grid Card Palette (matching reference Screenshot 1)
    val TopicCardPurple = Color(0xFF6A2C70)
    val TopicCardRoyalBlue = Color(0xFF2B449E)
    val TopicCardGreen = Color(0xFF1B8A2A)
    val TopicCardOrange = Color(0xFFDF6C00)
    val TopicCardMagenta = Color(0xFF9C1F61)
    val TopicCardLavender = Color(0xFF6B52A3)

    // Roadmap Card Framing: Thick white polaroid border and dark drop shadow
    val CardWhiteFrame = Color(0xFFFFFFFF)
    val CardDropShadow = Color(0x400A1931)
    val CardProgressTrack = Color(0x55000000)
    val CardProgressFill = Color(0xFFFFFFFF)

    // Roadmap Tall Topic Banner Card ("COLOURS", "FREE", "ABC")
    val TopicBannerBlueTop = Color(0xFF2845A8)
    val TopicBannerBlueBottom = Color(0xFF3355C4)

    // Roadmap Activity Card Illustration Scene Palettes
    val SceneIceCreamBg = Color(0xFFEAF7FE)
    val SceneShopBg = Color(0xFFD6EEF8)
    val SceneShopShelf = Color(0xFFD2691E)
    val SceneShopCounter = Color(0xFF2A7B88)
    val SceneCowMeadowBg = Color(0xFF68B92E)
    val SceneCowSilhouette = Color(0xFF4A2508)
    val SceneMonsterBg = Color(0xFFFFFFFF)
    val SceneMonsterPurple = Color(0xFFB446C8)
    val SceneMonsterCyan = Color(0xFF22A7D0)
    val SceneTableBg = Color(0xFF5386A6)
    val SceneSupermarketBg = Color(0xFFDCEBF7)
    val SceneKitchenWood = Color(0xFFC86428)
    val SceneDogVoiceBg = Color(0xFF0077C8)
    val SceneDogOrange = Color(0xFFE67E22)
    val SceneAbcChalkboard = Color(0xFF093A3E)
    val SceneLockedTint = Color(0x9937474F)

    // Roadmap Padlock Badge Colors (matching Screenshot 4 golden padlock)
    val PadlockGoldBody = Color(0xFFFFB74D)
    val PadlockGoldShackle = Color(0xFFFFA726)
    val PadlockGoldBorder = Color(0xFFE65100)
    val PadlockKeyhole = Color(0xFF4E342E)

    // Roadmap Mascot & Illustration Accent Colors
    val MascotOrangeFur = Color(0xFFF46524)
    val MascotOrangeStripe = Color(0xFFD84315)
    val MascotBlueBody = Color(0xFF64B5F6)
    val MascotGreenBody = Color(0xFF7CB342)
    val MascotPinkBody = Color(0xFFEC407A)
    val MascotSkinPeach = Color(0xFFFFCCBC)
    val MascotHairPink = Color(0xFFFF6090)
    val MascotHairYellow = Color(0xFFFFD54F)
    val MascotSuitOrange = Color(0xFFFF9800)
    val MascotBlueGlow = Color(0xFF4FC3F7)
    val MascotBlueSuit = Color(0xFF1E88E5)
    val SceneRedAccent = Color(0xFFE53935)
    val SceneYellowAccent = Color(0xFFFFCA28)
    val SceneGreenAccent = Color(0xFF43A047)
    val ScenePinkAccent = Color(0xFFF06292)
    val SceneBrownWood = Color(0xFF8D6E63)
    val SceneDarkInk = Color(0xFF263238)
    val SceneGreyMetal = Color(0xFF90A4AE)
    val SceneLightGrey = Color(0xFFECEFF1)
    val ModalScrimOverlay = Color(0x9908142C)

    // =========================================================================
    // Core Brand & Semantic Light Theme Colors
    // =========================================================================

    val Primary = Color(0xFF0288D1)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFE1F5FE)
    val OnPrimaryContainer = Color(0xFF01579B)
    val Secondary = Color(0xFFFFB300)
    val OnSecondary = Color(0xFF263238)
    val SecondaryContainer = Color(0xFFFFF8E1)
    val OnSecondaryContainer = Color(0xFF5D4037)
    val Tertiary = Color(0xFFFF7043)
    val OnTertiary = Color(0xFFFFFFFF)
    val Background = Color(0xFF009BE8)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF1F5F9)
    val TextPrimary = Color(0xFF1E293B)
    val TextSecondary = Color(0xFF475569)
    val Outline = Color(0xFFCBD5E1)
    val Success = Color(0xFF2E7D32)
    val Error = Color(0xFFD32F2F)

    // =========================================================================
    // Architectural Dark Theme Tokens
    // =========================================================================

    val PrimaryDark = Color(0xFF4FC3F7)
    val SecondaryDark = Color(0xFFFFD54F)
    val BackgroundDark = Color(0xFF0F172A)
    val SurfaceDark = Color(0xFF1E293B)
    val TextPrimaryDark = Color(0xFFF8FAFC)
    val TextSecondaryDark = Color(0xFF94A3B8)

    // =========================================================================
    // Reusable Brushes
    // =========================================================================

    val SplashBackgroundBrush: Brush = Brush.verticalGradient(
        colors = listOf(
            SplashBackgroundTop,
            SplashBackgroundMiddle,
            SplashBackgroundBottom
        )
    )

    val RoadmapBackgroundBrush: Brush = Brush.verticalGradient(
        colors = listOf(
            RoadmapBgTop,
            RoadmapBgBottom
        )
    )

    val TopicBannerBrush: Brush = Brush.verticalGradient(
        colors = listOf(
            TopicBannerBlueTop,
            TopicBannerBlueBottom
        )
    )

    val CrimsonRibbonBrush: Brush = Brush.verticalGradient(
        colors = listOf(
            HudCrimsonRibbonTop,
            HudCrimsonRibbonBottom
        )
    )
}
