package com.kidsenglish.app.feature.roadmap.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.icons.AppIcons
import com.kidsenglish.app.core.resources.AppStrings
import kotlin.math.cos
import kotlin.math.sin

/**
 * Top Horizontal HUD Bar matching the 5 reference screenshots:
 * - Left: 3D Blue Hexagonal All-Topics Grid toggle button + 3D Grey Hexagonal Settings button
 *   + Purple Hexagonal Puzzle Piece Ribbon with "Stickers today: X/6" counter.
 * - Center: Animated cartoon monster buddies header illustration.
 * - Right: Angled 3D Crimson Ribbon button with white crown icon to toggle view mode.
 */
@Composable
fun RoadmapTopHudBar(
    isTopicsGridMode: Boolean,
    stickersEarnedToday: Int,
    dailyStickerTarget: Int,
    onToggleGridMode: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimensions.RoadmapHudHeight)
            .padding(
                horizontal = AppDimensions.ScreenHorizontalPadding,
                vertical = AppDimensions.HudTopPadding
            )
    ) {
        // Left HUD group: Blue Hex Grid Toggle + Grey Hex Settings + Stickers Ribbon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.SpaceM)
        ) {
            HexagonalHudButton(
                icon = if (isTopicsGridMode) AppIcons.AdventureMap else AppIcons.GridMenu,
                contentDescription = stringResource(id = AppStrings.Roadmap.CD_GRID_TOGGLE),
                fillColor = AppColors.HudHexBlueFill,
                borderColor = AppColors.HudHexBlueBorder,
                shadowColor = AppColors.HudHexBlueShadow,
                onClick = onToggleGridMode,
                modifier = Modifier.testTag(AppConstants.TestTags.HUD_GRID_TOGGLE_BUTTON)
            )

            HexagonalHudButton(
                icon = AppIcons.SettingsGear,
                contentDescription = stringResource(id = AppStrings.Roadmap.CD_SETTINGS),
                fillColor = AppColors.HudHexGreyFill,
                borderColor = AppColors.HudHexGreyBorder,
                shadowColor = AppColors.HudHexGreyShadow,
                onClick = onOpenSettings,
                modifier = Modifier.testTag(AppConstants.TestTags.HUD_SETTINGS_BUTTON)
            )

            StickersTodayRibbonBadge(
                stickersEarned = stickersEarnedToday,
                dailyTarget = dailyStickerTarget,
                modifier = Modifier
                    .padding(start = AppDimensions.SpaceL)
                    .testTag(AppConstants.TestTags.HUD_STICKERS_BADGE)
            )
        }

        // Center HUD group: Animated Red & Rainbow Magic Parrot mascot
        HudMagicParrotIllustration(
            modifier = Modifier.size(
                width = AppDimensions.HudMascotHeaderWidth,
                height = AppDimensions.HudMascotHeaderHeight
            )
        )

        // Right HUD group: Crimson 3D angled ribbon button with white crown
        CrimsonModeRibbonButton(
            isTopicsGridMode = isTopicsGridMode,
            onClick = onToggleGridMode,
            modifier = Modifier.testTag(AppConstants.TestTags.HUD_MODE_RIBBON_BUTTON)
        )
    }
}

/**
 * 3D Beveled Hexagonal button matching the top-left blue grid and grey settings buttons,
 * with a bouncy spring scale on press.
 */
@Composable
private fun HexagonalHudButton(
    icon: ImageVector,
    contentDescription: String,
    fillColor: Color,
    borderColor: Color,
    shadowColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "hex_hud_button_scale"
    )

    val hexagonShape = GenericShape { size, _ ->
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(cx, cy)
        for (i in 0 until 6) {
            val angleRad = Math.toRadians((60.0 * i - 30.0))
            val x = cx + radius * cos(angleRad).toFloat()
            val y = cy + radius * sin(angleRad).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(pressScale)
            .size(AppDimensions.HudHexButtonSize)
            .clip(hexagonShape)
            .background(shadowColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = AppDimensions.BorderMedium)
                .clip(hexagonShape)
                .background(borderColor)
                .padding(AppDimensions.BorderThin)
                .clip(hexagonShape)
                .background(fillColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = AppColors.CardWhiteFrame,
                modifier = Modifier.size(AppDimensions.HudHexIconSize)
            )
        }
    }
}

/**
 * Purple Hexagonal Puzzle Piece badge attached to the "Stickers today: 0/6" ribbon banner.
 */
@Composable
private fun StickersTodayRibbonBadge(
    stickersEarned: Int,
    dailyTarget: Int,
    modifier: Modifier = Modifier
) {
    val hexagonShape = GenericShape { size, _ ->
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(cx, cy)
        for (i in 0 until 6) {
            val angleRad = Math.toRadians((60.0 * i - 30.0))
            val x = cx + radius * cos(angleRad).toFloat()
            val y = cy + radius * sin(angleRad).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.SpaceXS),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(AppDimensions.HudStickerHexSize)
                .clip(hexagonShape)
                .background(AppColors.HudStickerHexBorder)
                .padding(AppDimensions.BorderThin)
                .clip(hexagonShape)
                .background(AppColors.HudStickerHexFill)
        ) {
            Icon(
                imageVector = AppIcons.PuzzlePiece,
                contentDescription = stringResource(id = AppStrings.Roadmap.CD_STICKERS),
                tint = AppColors.CardWhiteFrame,
                modifier = Modifier.size(AppDimensions.HudHexIconSize)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(AppDimensions.HudStickerBannerHeight)
                .clip(RoundedCornerShape(AppDimensions.RadiusSmall))
                .background(AppColors.HudStickerBannerBg)
                .padding(horizontal = AppDimensions.SpaceM)
        ) {
            Text(
                text = stringResource(id = AppStrings.Roadmap.STICKERS_TODAY_LABEL),
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.CardWhiteFrame
            )
            Text(
                text = stringResource(
                    id = AppStrings.Roadmap.STICKERS_COUNT_FORMAT,
                    stickersEarned,
                    dailyTarget
                ),
                style = MaterialTheme.typography.titleLarge,
                color = AppColors.CardWhiteFrame
            )
        }
    }
}

/**
 * Animated Red & Rainbow Magic Parrot mascot in the center of the top HUD bar,
 * flapping its rainbow wings and waving its golden star wand.
 */
@Composable
private fun HudMagicParrotIllustration(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hud_parrot_anim")

    val bouncePhase by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.CHAR_BOUNCE_DURATION_MS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hud_parrot_bounce"
    )

    val wingFlap by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.CHAR_WAVE_DURATION_MS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hud_parrot_wing_flap"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.50f
        val bobY = bouncePhase * (h * 0.06f)
        val wingY = (wingFlap / 12f) * (h * 0.07f)

        // Left & Right Rainbow Wings (Blue, Green, Yellow, Orange)
        drawCircle(
            color = AppColors.Primary,
            radius = h * 0.28f,
            center = Offset(cx - w * 0.19f, h * 0.66f + bobY - wingY)
        )
        drawCircle(
            color = AppColors.SceneGreenAccent,
            radius = h * 0.22f,
            center = Offset(cx - w * 0.16f, h * 0.62f + bobY - wingY)
        )
        drawCircle(
            color = AppColors.SceneYellowAccent,
            radius = h * 0.16f,
            center = Offset(cx - w * 0.13f, h * 0.58f + bobY - wingY)
        )

        drawCircle(
            color = AppColors.Primary,
            radius = h * 0.28f,
            center = Offset(cx + w * 0.19f, h * 0.66f + bobY + wingY)
        )
        drawCircle(
            color = AppColors.SceneGreenAccent,
            radius = h * 0.22f,
            center = Offset(cx + w * 0.16f, h * 0.62f + bobY + wingY)
        )
        drawCircle(
            color = AppColors.SceneYellowAccent,
            radius = h * 0.16f,
            center = Offset(cx + w * 0.13f, h * 0.58f + bobY + wingY)
        )

        // Magic Star Wand on left side
        drawRoundRect(
            color = AppColors.MascotBlueGlow,
            topLeft = Offset(cx - w * 0.30f, h * 0.28f + bobY - wingY),
            size = Size(w * 0.035f, h * 0.44f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawCircle(
            color = AppColors.SplashSparkleGold,
            radius = h * 0.14f,
            center = Offset(cx - w * 0.28f, h * 0.24f + bobY - wingY)
        )

        // Top Red Feather Crest
        val crestPath = Path().apply {
            moveTo(cx - w * 0.05f, h * 0.26f + bobY)
            quadraticTo(cx, h * 0.01f + bobY, cx + w * 0.07f, h * 0.26f + bobY)
            close()
        }
        drawPath(path = crestPath, color = AppColors.SceneRedAccent)

        // Parrot Scarlet-Red Head & Body
        drawCircle(
            color = AppColors.SceneRedAccent,
            radius = h * 0.38f,
            center = Offset(cx, h * 0.56f + bobY)
        )

        // Creamy-White Eye Patch
        drawRoundRect(
            color = AppColors.SplashBadgeBackground,
            topLeft = Offset(cx - w * 0.14f, h * 0.34f + bobY),
            size = Size(w * 0.28f, h * 0.36f),
            cornerRadius = CornerRadius(24f, 24f)
        )

        // Big Glossy Dark Eyes + Catchlights
        val pupilShift = (wingFlap / 12f) * (w * 0.008f)
        drawCircle(
            color = AppColors.PadlockKeyhole,
            radius = h * 0.12f,
            center = Offset(cx - w * 0.07f + pupilShift, h * 0.50f + bobY)
        )
        drawCircle(
            color = AppColors.PadlockKeyhole,
            radius = h * 0.12f,
            center = Offset(cx + w * 0.07f + pupilShift, h * 0.50f + bobY)
        )
        drawCircle(
            color = AppColors.CardWhiteFrame,
            radius = h * 0.045f,
            center = Offset(cx - w * 0.08f + pupilShift, h * 0.46f + bobY)
        )
        drawCircle(
            color = AppColors.CardWhiteFrame,
            radius = h * 0.045f,
            center = Offset(cx + w * 0.06f + pupilShift, h * 0.46f + bobY)
        )

        // Golden-Yellow Parrot Beak
        drawCircle(
            color = AppColors.SceneYellowAccent,
            radius = h * 0.11f,
            center = Offset(cx, h * 0.62f + bobY)
        )
        drawArc(
            color = AppColors.PadlockKeyhole,
            startAngle = 15f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(cx - w * 0.04f, h * 0.60f + bobY),
            size = Size(w * 0.08f, h * 0.10f),
            style = Stroke(width = 3f)
        )
    }
}

/**
 * Top-right angled crimson 3D ribbon button with a white crown icon and bouncy press feedback.
 */
@Composable
private fun CrimsonModeRibbonButton(
    isTopicsGridMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "crimson_ribbon_scale"
    )

    val angledRibbonShape = GenericShape { size, _ ->
        val slant = size.height * 0.24f
        moveTo(0f, 0f)
        lineTo(size.width - slant, 0f)
        lineTo(size.width, size.height)
        lineTo(slant, size.height)
        close()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(pressScale)
            .height(AppDimensions.HudRibbonHeight)
            .clip(angledRibbonShape)
            .background(AppColors.HudCrimsonRibbonShadow)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(bottom = AppDimensions.BorderMedium)
            .clip(angledRibbonShape)
            .background(brush = AppColors.CrimsonRibbonBrush)
            .padding(horizontal = AppDimensions.HudRibbonHorizontalPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = AppIcons.CrownBadge,
                contentDescription = null,
                tint = AppColors.CardWhiteFrame,
                modifier = Modifier.size(AppDimensions.HudHexIconSize)
            )
            Spacer(modifier = Modifier.width(AppDimensions.SpaceXS))
            Text(
                text = stringResource(
                    id = if (isTopicsGridMode) {
                        AppStrings.Roadmap.ADVENTURE_BADGE
                    } else {
                        AppStrings.Roadmap.ALL_TOPICS_BADGE
                    }
                ),
                style = MaterialTheme.typography.labelLarge,
                color = AppColors.CardWhiteFrame
            )
        }
    }
}
