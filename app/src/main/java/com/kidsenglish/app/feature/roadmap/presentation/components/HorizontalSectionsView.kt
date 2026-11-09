package com.kidsenglish.app.feature.roadmap.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.images.AppImages
import com.kidsenglish.app.core.designsystem.typography.KidsDisplayFontFamily
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.ActivitySceneType
import com.kidsenglish.app.domain.model.RoadmapActivityCard
import com.kidsenglish.app.domain.model.RoadmapHorizontalSection
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Horizontal Scrolling Topic & Activity Cards Roadmap matching Screenshots 2, 3, 4, and 5.
 *
 * Each horizontal section displays:
 * - An animated 3D-styled companion mascot character standing beside a tall tilted royal-blue
 *   Topic Banner Card ("FREE", "ABC", "COLOURS", "123") with a rotating white starburst or
 *   "Words learned" progress bar and theme picture backdrop.
 * - Columns of 2-row animated tilted white-bordered Activity Postcard Cards (or an animated
 *   giant ABC letter tracing board) with theme-based illustrations and wiggling golden padlocks.
 */
@Composable
fun HorizontalSectionsView(
    sections: List<RoadmapHorizontalSection>,
    scrollToSectionIndex: Int?,
    onSectionScrollConsumed: () -> Unit,
    onActivityCardClick: (RoadmapActivityCard) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val infiniteTransition = rememberInfiniteTransition(label = "horizontal_roadmap_anim")

    val waveAngleRad by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.CARD_TILT_ROCK_DURATION_MS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "horizontal_wave_angle"
    )

    val charBounce by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.CHAR_BOUNCE_DURATION_MS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "char_bounce_offset"
    )

    val charWaveAngle by infiniteTransition.animateFloat(
        initialValue = -18f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.CHAR_WAVE_DURATION_MS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "char_wave_angle"
    )

    LaunchedEffect(scrollToSectionIndex) {
        if (scrollToSectionIndex != null && scrollToSectionIndex in sections.indices) {
            listState.animateScrollToItem(scrollToSectionIndex)
            onSectionScrollConsumed()
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(
            horizontal = AppDimensions.ScreenHorizontalPadding,
            vertical = AppDimensions.RoadmapContentVerticalPadding
        ),
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.RoadmapSectionSpacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .testTag(AppConstants.TestTags.HORIZONTAL_ROADMAP_ROW)
    ) {
        items(
            items = sections,
            key = { it.id }
        ) { section ->
            RoadmapTopicSectionItem(
                section = section,
                waveAngleRad = waveAngleRad,
                charBounce = charBounce,
                charWaveAngle = charWaveAngle,
                onActivityCardClick = onActivityCardClick
            )
        }
    }
}

/**
 * Single Horizontal Topic Section combining the Animated Companion Mascot, Tall Tilted Topic Banner,
 * and 2-Row Columns of Animated Activity Postcard Cards.
 */
@Composable
private fun RoadmapTopicSectionItem(
    section: RoadmapHorizontalSection,
    waveAngleRad: Float,
    charBounce: Float,
    charWaveAngle: Float,
    onActivityCardClick: (RoadmapActivityCard) -> Unit
) {
    val largeBoards = section.activities.filter { it.isLargeBoard }
    val regularActivities = section.activities.filter { !it.isLargeBoard }
    val activityColumns = regularActivities.chunked(2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.ActivityCardColumnSpacing)
    ) {
        // Companion mascot + Tall tilted royal-blue Topic Banner Card with generous spacing
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.MascotToBannerSpacing)
        ) {
            CompanionCharacterIllustration(
                isWarmStyle = section.isFreeBanner || section.id == "section_abc_board",
                charBounce = charBounce,
                charWaveAngle = charWaveAngle,
                modifier = Modifier.size(
                    width = AppDimensions.TopicMascotWidth,
                    height = AppDimensions.TopicMascotHeight
                )
            )

            TallTiltedTopicBannerCard(
                section = section,
                waveAngleRad = waveAngleRad
            )
        }

        // Giant ABC Tracing Board (if present in section, e.g., Screenshot 3 right side)
        largeBoards.forEach { boardCard ->
            GiantAbcTracingBoardCard(
                card = boardCard,
                waveAngleRad = waveAngleRad,
                onClick = { onActivityCardClick(boardCard) }
            )
        }

        // 2-Row Columns of Tilted White-Bordered Activity Postcard Cards
        activityColumns.forEach { columnCards ->
            Column(
                verticalArrangement = Arrangement.spacedBy(AppDimensions.ActivityCardRowSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                columnCards.forEach { card ->
                    ActivityPostcardItem(
                        card = card,
                        waveAngleRad = waveAngleRad,
                        onClick = { onActivityCardClick(card) }
                    )
                }
            }
        }
    }
}

/**
 * Animated Red & Rainbow Magic Parrot mascot standing beside each Topic Banner Card.
 *
 * Features the red macaw parrot with 3 top feather tufts, creamy eye patches, huge glossy dark eyes,
 * curved golden-yellow beak, white fluffy chest bib, flapping rainbow wings (red, orange, yellow,
 * green, blue), yellow bird feet, and a waving Golden Star Magic Wand.
 */
@Composable
private fun CompanionCharacterIllustration(
    isWarmStyle: Boolean,
    charBounce: Float,
    charWaveAngle: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.46f
        val bobY = charBounce * (h * 0.032f)
        val wingFlapAngle = charWaveAngle * 0.85f

        // Left Rainbow Wing (holding the Golden Star Magic Wand)
        rotate(degrees = -wingFlapAngle, pivot = Offset(cx - w * 0.16f, h * 0.56f + bobY)) {
            // Outer Blue Feather
            drawRoundRect(
                color = AppColors.Primary,
                topLeft = Offset(cx - w * 0.40f, h * 0.52f + bobY),
                size = Size(w * 0.28f, h * 0.18f),
                cornerRadius = CornerRadius(22f, 22f)
            )
            // Middle Green Feather
            drawRoundRect(
                color = AppColors.SceneGreenAccent,
                topLeft = Offset(cx - w * 0.36f, h * 0.50f + bobY),
                size = Size(w * 0.24f, h * 0.15f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            // Inner Yellow/Orange Feather
            drawRoundRect(
                color = AppColors.SceneYellowAccent,
                topLeft = Offset(cx - w * 0.31f, h * 0.48f + bobY),
                size = Size(w * 0.20f, h * 0.12f),
                cornerRadius = CornerRadius(18f, 18f)
            )

            // Striped Magic Wand + Glowing Golden Star
            drawRoundRect(
                color = AppColors.MascotBlueGlow,
                topLeft = Offset(cx - w * 0.38f, h * 0.28f + bobY),
                size = Size(w * 0.05f, h * 0.28f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawCircle(
                color = AppColors.SplashSparkleGold,
                radius = w * 0.11f,
                center = Offset(cx - w * 0.355f, h * 0.26f + bobY)
            )
            drawCircle(
                color = AppColors.CardWhiteFrame,
                radius = w * 0.045f,
                center = Offset(cx - w * 0.355f, h * 0.26f + bobY)
            )
        }

        // Right Rainbow Wing (flapping inside bounds so it never touches the adjacent card)
        rotate(degrees = wingFlapAngle, pivot = Offset(cx + w * 0.16f, h * 0.56f + bobY)) {
            // Outer Blue Feather
            drawRoundRect(
                color = AppColors.Primary,
                topLeft = Offset(cx + w * 0.12f, h * 0.52f + bobY),
                size = Size(w * 0.28f, h * 0.18f),
                cornerRadius = CornerRadius(22f, 22f)
            )
            // Middle Green Feather
            drawRoundRect(
                color = AppColors.SceneGreenAccent,
                topLeft = Offset(cx + w * 0.12f, h * 0.50f + bobY),
                size = Size(w * 0.24f, h * 0.15f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            // Inner Yellow/Orange Feather
            drawRoundRect(
                color = AppColors.SceneYellowAccent,
                topLeft = Offset(cx + w * 0.12f, h * 0.48f + bobY),
                size = Size(w * 0.19f, h * 0.12f),
                cornerRadius = CornerRadius(18f, 18f)
            )
        }

        // Parrot Red Body
        drawRoundRect(
            color = AppColors.SceneRedAccent,
            topLeft = Offset(cx - w * 0.24f, h * 0.46f + bobY),
            size = Size(w * 0.48f, h * 0.36f),
            cornerRadius = CornerRadius(32f, 32f)
        )

        // Fluffy Creamy-White Chest Bib
        drawRoundRect(
            color = AppColors.CardWhiteFrame,
            topLeft = Offset(cx - w * 0.14f, h * 0.52f + bobY),
            size = Size(w * 0.28f, h * 0.25f),
            cornerRadius = CornerRadius(24f, 24f)
        )

        // Top Fluffy 3-Feather Red Crest
        rotate(degrees = charWaveAngle * 0.22f, pivot = Offset(cx, h * 0.18f + bobY)) {
            val crestPath = Path().apply {
                moveTo(cx - w * 0.10f, h * 0.19f + bobY)
                quadraticTo(cx - w * 0.04f, h * 0.03f + bobY, cx + w * 0.04f, h * 0.06f + bobY)
                quadraticTo(cx + w * 0.14f, h * 0.05f + bobY, cx + w * 0.10f, h * 0.19f + bobY)
                close()
            }
            drawPath(path = crestPath, color = AppColors.SceneRedAccent)
        }

        // Big Round Scarlet-Red Parrot Head
        drawCircle(
            color = AppColors.SceneRedAccent,
            radius = w * 0.29f,
            center = Offset(cx, h * 0.33f + bobY)
        )

        // Creamy-White Eye & Cheek Patch
        drawRoundRect(
            color = AppColors.SplashBadgeBackground,
            topLeft = Offset(cx - w * 0.24f, h * 0.22f + bobY),
            size = Size(w * 0.48f, h * 0.22f),
            cornerRadius = CornerRadius(26f, 26f)
        )

        // Big Glossy Dark-Brown Eyes with Animated Pupil Tracking
        val pupilShift = (charWaveAngle / 22f) * (w * 0.016f)
        drawCircle(
            color = AppColors.PadlockKeyhole,
            radius = w * 0.088f,
            center = Offset(cx - w * 0.11f + pupilShift, h * 0.31f + bobY)
        )
        drawCircle(
            color = AppColors.PadlockKeyhole,
            radius = w * 0.088f,
            center = Offset(cx + w * 0.11f + pupilShift, h * 0.31f + bobY)
        )

        // Eye Specular Highlights
        drawCircle(
            color = AppColors.CardWhiteFrame,
            radius = w * 0.032f,
            center = Offset(cx - w * 0.125f + pupilShift, h * 0.29f + bobY)
        )
        drawCircle(
            color = AppColors.CardWhiteFrame,
            radius = w * 0.032f,
            center = Offset(cx + w * 0.095f + pupilShift, h * 0.29f + bobY)
        )

        // Rosy Pink Cheeks
        drawCircle(
            color = AppColors.ScenePinkAccent.copy(alpha = 0.55f),
            radius = w * 0.045f,
            center = Offset(cx - w * 0.17f, h * 0.38f + bobY)
        )
        drawCircle(
            color = AppColors.ScenePinkAccent.copy(alpha = 0.55f),
            radius = w * 0.045f,
            center = Offset(cx + w * 0.17f, h * 0.38f + bobY)
        )

        // Curved Golden-Yellow Parrot Beak & Smile
        drawCircle(
            color = AppColors.SceneYellowAccent,
            radius = w * 0.085f,
            center = Offset(cx, h * 0.37f + bobY)
        )
        drawArc(
            color = AppColors.PadlockKeyhole,
            startAngle = 15f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(cx - w * 0.06f, h * 0.36f + bobY),
            size = Size(w * 0.12f, h * 0.06f),
            style = Stroke(width = 3.5f)
        )

        // Cute Yellow Parrot Feet
        drawRoundRect(
            color = AppColors.SceneYellowAccent,
            topLeft = Offset(cx - w * 0.18f, h * 0.80f + bobY * 0.4f),
            size = Size(w * 0.14f, h * 0.07f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = AppColors.SceneYellowAccent,
            topLeft = Offset(cx + w * 0.04f, h * 0.80f + bobY * 0.4f),
            size = Size(w * 0.14f, h * 0.07f),
            cornerRadius = CornerRadius(10f, 10f)
        )
    }
}

/**
 * Animated Tall tilted Royal-Blue Topic Banner Card matching "COLOURS" (Screenshot 4) and "FREE" (Screenshot 5),
 * enhanced with a subtle theme-based picture backdrop and animated badge/icon.
 */
@Composable
private fun TallTiltedTopicBannerCard(
    section: RoadmapHorizontalSection,
    waveAngleRad: Float,
    modifier: Modifier = Modifier
) {
    val bannerTitle = stringResource(id = section.bannerTitleRes)
    val themeDrawableRes = AppImages.resolveTopicThemeDrawable(section.iconType)
    val swayTilt = -1.8f + sin(waveAngleRad) * 1.1f
    val badgeScale = 1.0f + cos(waveAngleRad) * 0.06f

    val starburstShape = GenericShape { size, _ ->
        val cx = size.width / 2f
        val cy = size.height / 2f
        val outerR = minOf(cx, cy)
        val innerR = outerR * 0.80f
        val points = 24
        for (i in 0 until points) {
            val r = if (i % 2 == 0) outerR else innerR
            val angle = Math.toRadians((360.0 / points) * i)
            val x = cx + r * cos(angle).toFloat()
            val y = cy + r * sin(angle).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }

    Box(
        modifier = modifier
            .size(
                width = AppDimensions.TopicBannerWidth,
                height = AppDimensions.TopicBannerHeight
            )
            .graphicsLayer {
                rotationZ = swayTilt
            }
            .background(brush = AppColors.TopicBannerBrush)
    ) {
        // Subtle theme-based picture backdrop inside the tall banner
        Image(
            painter = painterResource(id = themeDrawableRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.24f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(AppDimensions.SpaceM)
        ) {
            Text(
                text = bannerTitle,
                style = MaterialTheme.typography.displayMedium,
                color = AppColors.CardWhiteFrame,
                textAlign = TextAlign.Center
            )

            if (section.isFreeBanner) {
                // Animated white multi-point starburst badge with "FREE" inside (Screenshot 5)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(AppDimensions.FreeStarburstSize)
                        .graphicsLayer {
                            scaleX = badgeScale
                            scaleY = badgeScale
                            rotationZ = sin(waveAngleRad) * 8f
                        }
                        .clip(starburstShape)
                        .background(AppColors.CardWhiteFrame)
                ) {
                    Text(
                        text = bannerTitle,
                        style = MaterialTheme.typography.headlineLarge,
                        color = AppColors.TopicBannerBlueTop
                    )
                }
                Spacer(modifier = Modifier.height(AppDimensions.SpaceS))
            } else {
                // Animated central white topic icon + "Words learned" label + progress bar (Screenshot 4)
                Icon(
                    imageVector = resolveTopicIcon(section.iconType),
                    contentDescription = bannerTitle,
                    tint = AppColors.CardWhiteFrame,
                    modifier = Modifier
                        .size(AppDimensions.TopicBannerIconSize)
                        .scale(badgeScale)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = AppStrings.Roadmap.WORDS_LEARNED_LABEL),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.CardWhiteFrame
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.SpaceXS))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppDimensions.TopicBannerProgressHeight)
                            .clip(RoundedCornerShape(AppDimensions.CardProgressCornerRadius))
                            .background(AppColors.CardProgressTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = section.wordsLearnedProgress.coerceIn(0.12f, 1f))
                                .background(AppColors.CardProgressFill)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Animated Giant ABC Letter Tracing Board Card matching Screenshot 3 (right side):
 * Dark chalkboard surface with animated A, B, C characters and moving green tracing dots.
 */
@Composable
private fun GiantAbcTracingBoardCard(
    card: RoadmapActivityCard,
    waveAngleRad: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "abc_board_press_scale"
    )

    val density = LocalDensity.current
    val floatAmpPx = with(density) { AppDimensions.CardFloatAmplitude.toPx() }
    val floatY = sin(waveAngleRad) * floatAmpPx
    val swayAngle = card.tiltDegrees + cos(waveAngleRad) * 0.9f

    Box(
        modifier = modifier
            .size(
                width = AppDimensions.AbcBoardCardWidth,
                height = AppDimensions.AbcBoardCardHeight
            )
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
                translationY = floatY
                rotationZ = swayAngle
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Shadow plate
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    x = AppDimensions.CardShadowOffset,
                    y = AppDimensions.CardShadowOffset
                )
                .background(AppColors.CardDropShadow)
        )

        // White polaroid frame + dark teal chalkboard
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.CardWhiteFrame)
                .padding(AppDimensions.BorderThick)
                .background(AppColors.SceneAbcChalkboard)
        ) {
            // Subtle ABC theme picture overlay
            Image(
                painter = painterResource(id = AppImages.ThemeAbc),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.18f)
            )

            if (card.letterChar == 'A') {
                // Level 1: Featured 3D Letter 'A' Wizard
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppDimensions.SpaceXXS)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = AppImages.LetterAWizard),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxHeight(0.75f)
                                .graphicsLayer {
                                    translationY = sin(waveAngleRad * 1.5f) * 6f
                                    rotationZ = cos(waveAngleRad) * 3f
                                }
                        )
                        Text(
                            text = "A - Apple 🍎",
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.CardWhiteFrame
                        )
                    }
                }
            } else if (card.letterChar != null) {
                // Levels 2..26 (Letters B..Z)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val charBounce = sin(waveAngleRad) * (h * 0.03f)

                    // Big Letter Display
                    drawText(
                        textMeasurer = textMeasurer,
                        text = card.letterChar.toString(),
                        topLeft = Offset(w * 0.12f, h * 0.18f + charBounce),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeDisplayLarge * 1.2f,
                            color = AppColors.SceneYellowAccent
                        )
                    )

                    // Phonics Emoji
                    if (card.phonicsEmoji != null) {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = card.phonicsEmoji,
                            topLeft = Offset(w * 0.62f, h * 0.28f - charBounce),
                            style = TextStyle(
                                fontSize = AppDimensions.TextSizeDisplayLarge
                            )
                        )
                    }

                    // Green Tracing Dot
                    drawCircle(
                        color = AppColors.SceneGreenAccent,
                        radius = w * 0.038f,
                        center = Offset(w * 0.22f, h * 0.68f + charBounce)
                    )

                    // Stars
                    drawCircle(
                        color = AppColors.SplashSparkleGold,
                        radius = w * (0.018f + abs(sin(waveAngleRad)) * 0.01f),
                        center = Offset(w * 0.82f, h * 0.18f)
                    )
                }

                // Word Label
                if (card.phonicsWord != null) {
                    Text(
                        text = "${card.letterChar} - ${card.phonicsWord}",
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.CardWhiteFrame,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = AppDimensions.SpaceXS)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val charBounceA = sin(waveAngleRad) * (h * 0.03f)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "ABC",
                        topLeft = Offset(w * 0.15f, h * 0.32f + charBounceA),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeDisplayLarge,
                            color = AppColors.SceneYellowAccent
                        )
                    )
                }
            }

            // Locked Overlay & Padlock
            if (card.isLocked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.SceneLockedTint)
                )
                GoldenPadlockBadge(
                    wiggleAngle = sin(waveAngleRad) * 8f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(AppDimensions.SpaceXXS)
                        .size(AppDimensions.ActivityPadlockSize)
                )
            }
        }
    }
}

/**
 * Animated Tilted White-Bordered Activity Postcard Card matching Screenshots 2, 3, 4, and 5.
 * Includes theme-based pictures, animated procedural scene illustrations, and a wiggling golden padlock if locked.
 */
@Composable
fun ActivityPostcardItem(
    card: RoadmapActivityCard,
    waveAngleRad: Float = 0f,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(card.id) {
        appeared = true
    }

    val entranceScale by animateFloatAsState(
        targetValue = if (appeared) 1.0f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "activity_card_entrance_scale"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.91f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "activity_card_press_scale"
    )

    val cardPhase = remember(card.id) {
        (abs(card.id.hashCode()) % 10) * 0.6f
    }
    val density = LocalDensity.current
    val floatAmpPx = with(density) { AppDimensions.CardFloatAmplitude.toPx() }
    val floatY = sin(waveAngleRad + cardPhase) * floatAmpPx
    val rockTilt = card.tiltDegrees + cos(waveAngleRad + cardPhase) * 1.2f

    Box(
        modifier = modifier
            .size(
                width = AppDimensions.ActivityCardWidth,
                height = AppDimensions.ActivityCardHeight
            )
            .graphicsLayer {
                val scale = entranceScale * pressScale
                scaleX = scale
                scaleY = scale
                translationY = floatY
                rotationZ = rockTilt
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Drop shadow plate
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    x = AppDimensions.CardShadowOffset,
                    y = AppDimensions.CardShadowOffset
                )
                .background(AppColors.CardDropShadow)
        )

        // White Polaroid Frame + Illustrated Scene Canvas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.CardWhiteFrame)
                .padding(AppDimensions.BorderMedium)
        ) {
            ActivitySceneIllustration(
                sceneType = card.sceneType,
                isLocked = card.isLocked,
                waveAngleRad = waveAngleRad + cardPhase,
                modifier = Modifier.fillMaxSize()
            )

            // Top-right Animated Golden Padlock badge on locked cards (matching Screenshot 4)
            if (card.isLocked) {
                GoldenPadlockBadge(
                    wiggleAngle = sin(waveAngleRad + cardPhase) * 8f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(AppDimensions.SpaceXXS)
                        .size(AppDimensions.ActivityPadlockSize)
                )
            }
        }
    }
}

/**
 * Resolves the background theme picture drawable for each [ActivitySceneType].
 */
private fun resolveSceneThemeDrawable(sceneType: ActivitySceneType): Int = when (sceneType) {
    ActivitySceneType.ICE_CREAM_COLORING,
    ActivitySceneType.SWEET_SHOP_COUNTER,
    ActivitySceneType.YUMMY_FOOD_TABLE,
    ActivitySceneType.SUPERMARKET_CART,
    ActivitySceneType.NUMBER_BAKERY_DONUTS -> AppImages.ThemeSweets

    ActivitySceneType.COW_SHADOW_MATCH,
    ActivitySceneType.DOG_MICROPHONE_SPEAK -> AppImages.ThemeAnimals

    ActivitySceneType.CITY_POLICE_PATROL,
    ActivitySceneType.CRANE_PINK_WORD -> AppImages.ThemeCity

    ActivitySceneType.ROOM_MYSTERY_QUIZ,
    ActivitySceneType.KITCHEN_MAGNIFIER_2 -> AppImages.ThemeHome

    ActivitySceneType.MONSTER_FACE_STICKERS -> AppImages.ThemeSpace

    ActivitySceneType.ABC_TRACING_BOARD,
    ActivitySceneType.COLOR_SPEECH_BUBBLES,
    ActivitySceneType.SPELL_RED_BLOCKS,
    ActivitySceneType.MIC_COLOR_CIRCLES -> AppImages.ThemeAbc
}

/**
 * Custom Compose Canvas + Theme Picture illustrations for each Activity Postcard Scene in Screenshots 2–5,
 * featuring the Red & Rainbow Magic Parrot as the sole character across all scenes.
 */
@Composable
fun ActivitySceneIllustration(
    sceneType: ActivitySceneType,
    isLocked: Boolean,
    waveAngleRad: Float = 0f,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val themeDrawableRes = resolveSceneThemeDrawable(sceneType)

    Box(modifier = modifier) {
        // Base layer: Theme-based vector illustration
        Image(
            painter = painterResource(id = themeDrawableRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.32f)
        )

        // For Lesson 1 (ICE_CREAM_COLORING): Display animated 3D Letter 'A' Wizard image
        if (sceneType == ActivitySceneType.ICE_CREAM_COLORING) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimensions.SpaceXXS)
            ) {
                Image(
                    painter = painterResource(id = AppImages.LetterAWizard),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxHeight(0.90f)
                        .graphicsLayer {
                            translationY = sin(waveAngleRad * 1.5f) * 6f
                            rotationZ = cos(waveAngleRad) * 3f
                        }
                )
            }
        } else {
            // Foreground layer: Animated scene-specific illustration
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val bobOffset = sin(waveAngleRad) * (h * 0.04f)
                val altBobOffset = cos(waveAngleRad) * (h * 0.04f)

                when (sceneType) {
                ActivitySceneType.ICE_CREAM_COLORING -> {
                    drawRect(color = AppColors.SceneIceCreamBg.copy(alpha = 0.82f))
                    // Waffle cone
                    val conePath = Path().apply {
                        moveTo(w * 0.22f, h * 0.55f)
                        lineTo(w * 0.48f, h * 0.55f)
                        lineTo(w * 0.35f, h * 0.94f)
                        close()
                    }
                    drawPath(path = conePath, color = AppColors.MascotSuitOrange)
                    // Animated bobbing ice cream scoops & cherry
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.18f, center = Offset(w * 0.28f, h * 0.44f + bobOffset * 0.5f))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.18f, center = Offset(w * 0.42f, h * 0.44f - bobOffset * 0.5f))
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.08f, center = Offset(w * 0.35f, h * 0.22f + bobOffset))
                    // Animated paintbrush & color dots on right
                    drawRoundRect(
                        color = AppColors.SceneBrownWood,
                        topLeft = Offset(w * 0.64f, h * 0.28f + altBobOffset),
                        size = Size(w * 0.06f, h * 0.62f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    val paletteColors = listOf(
                        AppColors.SceneDarkInk,
                        AppColors.SceneYellowAccent,
                        AppColors.SceneGreenAccent,
                        AppColors.MascotSuitOrange,
                        AppColors.SceneRedAccent
                    )
                    paletteColors.forEachIndexed { idx, dotColor ->
                        drawCircle(
                            color = dotColor,
                            radius = h * 0.065f,
                            center = Offset(w * 0.86f, h * (0.16f + idx * 0.17f))
                        )
                    }
                }

                ActivitySceneType.SWEET_SHOP_COUNTER -> {
                    drawRect(color = AppColors.SceneShopBg.copy(alpha = 0.82f))
                    // Shelves on right
                    drawRect(
                        color = AppColors.SceneShopShelf,
                        topLeft = Offset(w * 0.54f, h * 0.08f),
                        size = Size(w * 0.42f, h * 0.88f)
                    )
                    // Counter on left
                    drawRect(
                        color = AppColors.SceneShopCounter,
                        topLeft = Offset(w * 0.04f, h * 0.58f),
                        size = Size(w * 0.48f, h * 0.38f)
                    )
                    // Magic Parrot at counter
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.16f, center = Offset(w * 0.26f, h * 0.36f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.05f, center = Offset(w * 0.22f, h * 0.33f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.05f, center = Offset(w * 0.30f, h * 0.33f + bobOffset))
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.045f, center = Offset(w * 0.26f, h * 0.39f + bobOffset))
                    // Colorful cupcakes on shelves
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.07f, center = Offset(w * 0.66f, h * 0.24f + altBobOffset * 0.4f))
                    drawCircle(color = AppColors.SceneGreenAccent, radius = h * 0.07f, center = Offset(w * 0.84f, h * 0.24f - altBobOffset * 0.4f))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.07f, center = Offset(w * 0.66f, h * 0.52f))
                    drawCircle(color = AppColors.ScenePinkAccent, radius = h * 0.07f, center = Offset(w * 0.84f, h * 0.80f))
                }

                ActivitySceneType.COW_SHADOW_MATCH -> {
                    drawRect(color = AppColors.SceneCowMeadowBg.copy(alpha = 0.82f))
                    // Dark parrot silhouette on left
                    drawCircle(color = AppColors.SceneCowSilhouette, radius = h * 0.22f, center = Offset(w * 0.28f, h * 0.54f))
                    // Animated Red & Rainbow Parrot on right
                    drawCircle(color = AppColors.Primary, radius = h * 0.14f, center = Offset(w * 0.52f, h * 0.58f + bobOffset))
                    drawCircle(color = AppColors.SceneGreenAccent, radius = h * 0.14f, center = Offset(w * 0.78f, h * 0.58f + bobOffset))
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.22f, center = Offset(w * 0.65f, h * 0.54f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.06f, center = Offset(w * 0.60f, h * 0.48f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.06f, center = Offset(w * 0.70f, h * 0.48f + bobOffset))
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.06f, center = Offset(w * 0.65f, h * 0.56f + bobOffset))
                }

                ActivitySceneType.MONSTER_FACE_STICKERS -> {
                    drawRect(color = AppColors.SceneMonsterBg.copy(alpha = 0.86f))
                    // Animated Red & Rainbow Magic Parrot with sticker sheet
                    drawCircle(color = AppColors.Primary, radius = h * 0.18f, center = Offset(w * 0.18f, h * 0.58f + bobOffset))
                    drawCircle(color = AppColors.SceneGreenAccent, radius = h * 0.18f, center = Offset(w * 0.52f, h * 0.58f + bobOffset))
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.28f, center = Offset(w * 0.35f, h * 0.54f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.08f, center = Offset(w * 0.29f, h * 0.46f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.08f, center = Offset(w * 0.41f, h * 0.46f + bobOffset))
                    drawCircle(color = AppColors.PadlockKeyhole, radius = h * 0.04f, center = Offset(w * 0.29f + altBobOffset * 0.3f, h * 0.46f + bobOffset))
                    drawCircle(color = AppColors.PadlockKeyhole, radius = h * 0.04f, center = Offset(w * 0.41f + altBobOffset * 0.3f, h * 0.46f + bobOffset))
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.075f, center = Offset(w * 0.35f, h * 0.56f + bobOffset))
                    // Sticker sheet on right
                    drawRect(
                        color = AppColors.SceneLightGrey,
                        topLeft = Offset(w * 0.68f, h * 0.18f),
                        size = Size(w * 0.24f, h * 0.66f)
                    )
                }

                ActivitySceneType.YUMMY_FOOD_TABLE -> {
                    drawRect(color = AppColors.SceneShopBg.copy(alpha = 0.80f))
                    // Blue dining table
                    drawRect(
                        color = AppColors.SceneTableBg,
                        topLeft = Offset(0f, h * 0.46f),
                        size = Size(w, h * 0.54f)
                    )
                    // Animated Magic Parrot behind table
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.15f, center = Offset(w * 0.22f, h * 0.26f + bobOffset))
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.05f, center = Offset(w * 0.22f, h * 0.30f + bobOffset))
                    // Plates with pizza, soup, cheese
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.13f, center = Offset(w * 0.20f, h * 0.65f))
                    drawCircle(color = AppColors.MascotSuitOrange, radius = h * 0.14f, center = Offset(w * 0.66f, h * 0.62f + altBobOffset * 0.4f))
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.13f, center = Offset(w * 0.66f, h * 0.84f))
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.12f, center = Offset(w * 0.20f, h * 0.86f))
                }

                ActivitySceneType.SUPERMARKET_CART -> {
                    drawRect(color = AppColors.SceneSupermarketBg.copy(alpha = 0.82f))
                    // Display screen with bananas
                    drawRect(
                        color = AppColors.CardWhiteFrame,
                        topLeft = Offset(w * 0.56f, h * 0.12f),
                        size = Size(w * 0.34f, h * 0.34f)
                    )
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.09f, center = Offset(w * 0.73f, h * 0.29f + bobOffset * 0.5f))
                    // Animated Magic Parrot shopper & shopping cart
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.14f, center = Offset(w * 0.15f, h * 0.46f + bobOffset))
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.05f, center = Offset(w * 0.18f, h * 0.48f + bobOffset))
                    drawRect(
                        color = AppColors.SceneGreenAccent,
                        topLeft = Offset(w * 0.28f + altBobOffset * 0.5f, h * 0.62f),
                        size = Size(w * 0.24f, h * 0.22f)
                    )
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.07f, center = Offset(w * 0.40f + altBobOffset * 0.5f, h * 0.58f))
                }

                ActivitySceneType.NUMBER_BAKERY_DONUTS -> {
                    drawRect(color = AppColors.SceneShopBg.copy(alpha = 0.82f))
                    // Bakery shelf on right with animated 1..6 number treats
                    drawRect(
                        color = AppColors.SceneShopShelf,
                        topLeft = Offset(w * 0.52f, h * 0.06f),
                        size = Size(w * 0.44f, h * 0.90f)
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "1 2 3",
                        topLeft = Offset(w * 0.58f, h * 0.14f + bobOffset * 0.5f),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeTitleMedium,
                            color = AppColors.CardWhiteFrame
                        )
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "4 5 6",
                        topLeft = Offset(w * 0.58f, h * 0.42f - bobOffset * 0.5f),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeTitleMedium,
                            color = AppColors.SceneYellowAccent
                        )
                    )
                }

                ActivitySceneType.ROOM_MYSTERY_QUIZ -> {
                    // Split room (grey left, colorful right) with pulsing blue "?" badge
                    drawRect(color = AppColors.SceneGreyMetal.copy(alpha = 0.85f), topLeft = Offset.Zero, size = Size(w * 0.5f, h))
                    drawRect(color = AppColors.SceneBrownWood.copy(alpha = 0.85f), topLeft = Offset(w * 0.5f, 0f), size = Size(w * 0.5f, h))
                    drawCircle(
                        color = AppColors.Primary,
                        radius = h * (0.18f + abs(sin(waveAngleRad)) * 0.025f),
                        center = Offset(w * 0.5f, h * 0.5f + bobOffset * 0.5f)
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "?",
                        topLeft = Offset(w * 0.46f, h * 0.38f + bobOffset * 0.5f),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeTitleLarge,
                            color = AppColors.CardWhiteFrame
                        )
                    )
                }

                ActivitySceneType.CITY_POLICE_PATROL -> {
                    drawRect(color = AppColors.SceneSupermarketBg.copy(alpha = 0.78f))
                    // Yellow city building on right
                    drawRect(
                        color = AppColors.SceneYellowAccent,
                        topLeft = Offset(w * 0.54f, h * 0.12f),
                        size = Size(w * 0.42f, h * 0.56f)
                    )
                    // Road at bottom
                    drawRect(
                        color = AppColors.SceneDarkInk,
                        topLeft = Offset(0f, h * 0.68f),
                        size = Size(w, h * 0.32f)
                    )
                    // Animated patrolling blue police car
                    drawRoundRect(
                        color = AppColors.HudHexBlueFill,
                        topLeft = Offset(w * 0.24f + altBobOffset, h * 0.52f),
                        size = Size(w * 0.46f, h * 0.22f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }

                ActivitySceneType.KITCHEN_MAGNIFIER_2 -> {
                    drawRect(color = AppColors.SceneKitchenWood.copy(alpha = 0.82f))
                    // Animated magnifying glass ring in center with pink "2"
                    drawCircle(
                        color = AppColors.CardWhiteFrame,
                        radius = h * 0.32f,
                        center = Offset(w * 0.48f + altBobOffset * 0.5f, h * 0.44f + bobOffset * 0.5f)
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "2",
                        topLeft = Offset(w * 0.43f + altBobOffset * 0.5f, h * 0.26f + bobOffset * 0.5f),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeDisplayMedium,
                            color = AppColors.TopicCardMagenta
                        )
                    )
                }

                ActivitySceneType.DOG_MICROPHONE_SPEAK -> {
                    drawRect(color = AppColors.SceneDogVoiceBg.copy(alpha = 0.82f))
                    // Golden microphone icon on left with pulsing sound ring
                    drawCircle(
                        color = AppColors.SplashMascotHalo,
                        radius = h * (0.20f + abs(sin(waveAngleRad)) * 0.05f),
                        center = Offset(w * 0.19f, h * 0.50f)
                    )
                    drawRoundRect(
                        color = AppColors.SceneYellowAccent,
                        topLeft = Offset(w * 0.12f, h * 0.32f),
                        size = Size(w * 0.14f, h * 0.36f),
                        cornerRadius = CornerRadius(14f, 14f)
                    )
                    // White photo card on right with animated Red & Rainbow Magic Parrot
                    drawRect(
                        color = AppColors.CardWhiteFrame,
                        topLeft = Offset(w * 0.38f, h * 0.14f),
                        size = Size(w * 0.52f, h * 0.72f)
                    )
                    drawCircle(color = AppColors.Primary, radius = h * 0.12f, center = Offset(w * 0.50f, h * 0.52f + bobOffset))
                    drawCircle(color = AppColors.SceneGreenAccent, radius = h * 0.12f, center = Offset(w * 0.78f, h * 0.52f + bobOffset))
                    drawCircle(color = AppColors.SceneRedAccent, radius = h * 0.20f, center = Offset(w * 0.64f, h * 0.50f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.055f, center = Offset(w * 0.59f, h * 0.44f + bobOffset))
                    drawCircle(color = AppColors.CardWhiteFrame, radius = h * 0.055f, center = Offset(w * 0.69f, h * 0.44f + bobOffset))
                    drawCircle(color = AppColors.SceneYellowAccent, radius = h * 0.055f, center = Offset(w * 0.64f, h * 0.52f + bobOffset))
                }

                ActivitySceneType.CRANE_PINK_WORD -> {
                    drawRect(color = AppColors.SceneLightGrey.copy(alpha = 0.82f))
                    drawRect(
                        color = AppColors.SceneDarkInk,
                        topLeft = Offset(w * 0.14f, h * 0.32f + bobOffset * 0.4f),
                        size = Size(w * 0.72f, h * 0.52f)
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "pink",
                        topLeft = Offset(w * 0.22f, h * 0.48f + bobOffset * 0.4f),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeTitleLarge,
                            color = AppColors.CardWhiteFrame
                        )
                    )
                }

                ActivitySceneType.COLOR_SPEECH_BUBBLES -> {
                    drawRect(color = AppColors.CardWhiteFrame.copy(alpha = 0.88f))
                    drawCircle(color = AppColors.SceneLightGrey, radius = h * 0.14f, center = Offset(w * 0.24f, h * 0.72f + bobOffset))
                    drawCircle(color = AppColors.SceneDarkInk, radius = h * 0.14f, center = Offset(w * 0.50f, h * 0.72f - bobOffset))
                    drawCircle(color = AppColors.SceneGreyMetal, radius = h * 0.14f, center = Offset(w * 0.76f, h * 0.72f + altBobOffset))
                }

                ActivitySceneType.SPELL_RED_BLOCKS -> {
                    drawRect(color = AppColors.SceneDarkInk.copy(alpha = 0.85f))
                    drawRect(
                        color = AppColors.CardWhiteFrame,
                        topLeft = Offset(w * 0.20f, h * 0.14f),
                        size = Size(w * 0.60f, h * 0.48f)
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "r   e   d",
                        topLeft = Offset(w * 0.28f, h * 0.68f + bobOffset * 0.4f),
                        style = TextStyle(
                            fontFamily = KidsDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppDimensions.TextSizeTitleLarge,
                            color = AppColors.CardWhiteFrame
                        )
                    )
                }

                ActivitySceneType.MIC_COLOR_CIRCLES,
                ActivitySceneType.ABC_TRACING_BOARD -> {
                    drawRect(color = AppColors.SceneDarkInk.copy(alpha = 0.85f))
                    drawRoundRect(
                        color = AppColors.SceneGreyMetal,
                        topLeft = Offset(w * 0.12f, h * 0.36f),
                        size = Size(w * 0.14f, h * 0.40f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                    drawRect(
                        color = AppColors.CardWhiteFrame,
                        topLeft = Offset(w * 0.38f, h * 0.16f),
                        size = Size(w * 0.52f, h * 0.70f)
                    )
                    drawCircle(color = AppColors.SceneGreyMetal, radius = h * 0.11f, center = Offset(w * 0.54f, h * 0.38f + bobOffset * 0.5f))
                    drawCircle(color = AppColors.SceneGreyMetal, radius = h * 0.11f, center = Offset(w * 0.74f, h * 0.52f - bobOffset * 0.5f))
                }
            }

            if (isLocked) {
                drawRect(color = AppColors.SceneLockedTint)
            }
        }
        }
    }
}

/**
 * Animated Golden 3D Padlock Badge in the top-right corner of locked Activity & Topic Cards.
 */
@Composable
fun GoldenPadlockBadge(
    wiggleAngle: Float = 0f,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.graphicsLayer {
            rotationZ = wiggleAngle
        }
    ) {
        val w = size.width
        val h = size.height

        // Shackle arch
        drawArc(
            color = AppColors.PadlockGoldShackle,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.24f, h * 0.08f),
            size = Size(w * 0.52f, h * 0.48f),
            style = Stroke(width = w * 0.12f)
        )

        // Round golden lock body
        drawCircle(
            color = AppColors.PadlockGoldBorder,
            radius = w * 0.40f,
            center = Offset(w * 0.50f, h * 0.60f)
        )
        drawCircle(
            color = AppColors.PadlockGoldBody,
            radius = w * 0.36f,
            center = Offset(w * 0.50f, h * 0.60f)
        )

        // Keyhole
        drawCircle(
            color = AppColors.PadlockKeyhole,
            radius = w * 0.08f,
            center = Offset(w * 0.50f, h * 0.56f)
        )
        drawRect(
            color = AppColors.PadlockKeyhole,
            topLeft = Offset(w * 0.46f, h * 0.56f),
            size = Size(w * 0.08f, h * 0.16f)
        )
    }
}
