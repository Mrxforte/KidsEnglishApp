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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.icons.AppIcons
import com.kidsenglish.app.core.designsystem.images.AppImages
import com.kidsenglish.app.domain.model.RoadmapTopicCategory
import com.kidsenglish.app.domain.model.TopicColorStyle
import com.kidsenglish.app.domain.model.TopicIconType
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * 3-Row Multi-Column All-Topics Tilted Card Grid matching Screenshot 1.
 *
 * Displays all 200 topic categories efficiently using a horizontal [LazyRow] of 3-item columns.
 * Each card features:
 * - Bouncy spring entrance, continuous floating & rocking motion, and press scale animation.
 * - Theme-based picture backdrop ([AppImages.resolveTopicThemeDrawable]) blended with its topic color.
 * - Animated topic icon emblem, readable title, and animated bottom progress bar.
 */
@Composable
fun TopicsGridView(
    categories: List<RoadmapTopicCategory>,
    onCategoryClick: (RoadmapTopicCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = categories.chunked(3)
    val infiniteTransition = rememberInfiniteTransition(label = "topics_grid_wave")

    val waveAngleRad by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.CARD_FLOAT_DURATION_MS * 2,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "topics_grid_wave_angle"
    )

    LazyRow(
        contentPadding = PaddingValues(
            horizontal = AppDimensions.ScreenHorizontalPadding,
            vertical = AppDimensions.RoadmapContentVerticalPadding
        ),
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.TopicGridColumnSpacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .testTag(AppConstants.TestTags.TOPICS_GRID_CONTAINER)
    ) {
        items(
            items = columns,
            key = { col -> col.firstOrNull()?.id ?: col.hashCode() }
        ) { columnCategories ->
            Column(
                verticalArrangement = Arrangement.spacedBy(AppDimensions.TopicGridRowSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                columnCategories.forEach { category ->
                    TiltedTopicCategoryCard(
                        category = category,
                        waveAngleRad = waveAngleRad,
                        onClick = { onCategoryClick(category) }
                    )
                }
            }
        }
    }
}

/**
 * Animated tilted polaroid-style Topic Category Card with theme-based illustration backdrop.
 */
@Composable
private fun TiltedTopicCategoryCard(
    category: RoadmapTopicCategory,
    waveAngleRad: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fillColor = resolveTopicColor(category.colorStyle)
    val themeDrawableRes = AppImages.resolveTopicThemeDrawable(category.iconType)
    val displayTitle = category.customTitle ?: stringResource(id = category.titleRes)

    // Entrance pop-in animation
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(category.id) {
        appeared = true
    }

    val entranceScale by animateFloatAsState(
        targetValue = if (appeared) 1.0f else 0.82f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "topic_card_entrance_scale"
    )

    // Interactive press-bounce animation
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "topic_card_press_scale"
    )

    // Smoothly animated progress bar width
    val animatedProgress by animateFloatAsState(
        targetValue = if (appeared) category.wordsLearnedProgress.coerceIn(0.08f, 1f) else 0.08f,
        animationSpec = tween(
            durationMillis = AppConstants.PROGRESS_ANIMATION_DURATION_MS,
            easing = FastOutSlowInEasing
        ),
        label = "topic_card_progress"
    )

    // Per-card phase offset for lively floating & tilt-rocking
    val cardPhase = remember(category.id) {
        (abs(category.id.hashCode()) % 12) * 0.52f
    }
    val density = LocalDensity.current
    val floatAmpPx = with(density) { AppDimensions.CardFloatAmplitude.toPx() }
    val floatOffsetY = sin(waveAngleRad + cardPhase) * floatAmpPx
    val rockTiltOffset = cos(waveAngleRad + cardPhase) * 1.4f
    val iconPulseScale = 1.0f + sin(waveAngleRad + cardPhase) * 0.08f

    Box(
        modifier = modifier
            .size(
                width = AppDimensions.TopicGridCardWidth,
                height = AppDimensions.TopicGridCardHeight
            )
            .graphicsLayer {
                val combinedScale = entranceScale * pressScale
                scaleX = combinedScale
                scaleY = combinedScale
                translationY = floatOffsetY
                rotationZ = category.tiltDegrees + rockTiltOffset
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Drop shadow plate behind the polaroid frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    x = AppDimensions.CardShadowOffset,
                    y = AppDimensions.CardShadowOffset
                )
                .background(AppColors.CardDropShadow)
        )

        // Thick white polaroid border frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.CardWhiteFrame)
                .padding(AppDimensions.BorderMedium)
                .background(fillColor)
        ) {
            // Layer 1: Theme-based picture backdrop inside the card
            Image(
                painter = painterResource(id = themeDrawableRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.45f)
            )

            // Layer 2: Rich theme color tint overlay for crisp foreground readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(fillColor.copy(alpha = 0.58f))
            )

            // Layer 3: Foreground Icon + Title + Progress Bar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimensions.CardInnerPadding)
            ) {
                when {
                    category.customTitle != null -> {
                        // Topics 19..200: Animated theme icon badge + custom topic title
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(AppDimensions.TopicGridIconBadgeSize)
                                    .scale(iconPulseScale)
                                    .clip(CircleShape)
                                    .background(AppColors.CardProgressTrack)
                            ) {
                                Icon(
                                    imageVector = resolveTopicIcon(category.iconType),
                                    contentDescription = null,
                                    tint = AppColors.CardWhiteFrame,
                                    modifier = Modifier.size(AppDimensions.SpaceM)
                                )
                            }
                            Spacer(modifier = Modifier.width(AppDimensions.SpaceXXS))
                            Text(
                                text = displayTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = AppColors.CardWhiteFrame,
                                textAlign = TextAlign.Start,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    category.iconType == TopicIconType.ABC_LETTERS ||
                        category.iconType == TopicIconType.NUMBERS_123 -> {
                        Spacer(modifier = Modifier.height(AppDimensions.SpaceXXS))
                        Text(
                            text = displayTitle,
                            style = MaterialTheme.typography.displayMedium,
                            color = AppColors.CardWhiteFrame,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            modifier = Modifier.scale(iconPulseScale)
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = resolveTopicIcon(category.iconType),
                            contentDescription = displayTitle,
                            tint = AppColors.CardWhiteFrame,
                            modifier = Modifier
                                .size(AppDimensions.TopicGridIconSize)
                                .scale(iconPulseScale)
                        )
                        Text(
                            text = displayTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.CardWhiteFrame,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Bottom dark progress bar with white glossy animated fill
                if (category.iconType != TopicIconType.ABC_LETTERS || category.customTitle != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppDimensions.TopicGridProgressHeight)
                            .clip(RoundedCornerShape(AppDimensions.CardProgressCornerRadius))
                            .background(AppColors.CardProgressTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = animatedProgress)
                                .background(AppColors.CardProgressFill)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(AppDimensions.TopicGridProgressHeight))
                }
            }
        }
    }
}

/**
 * Maps [TopicColorStyle] to the centralized [AppColors] palette.
 */
@Composable
fun resolveTopicColor(style: TopicColorStyle): Color = when (style) {
    TopicColorStyle.PURPLE -> AppColors.TopicCardPurple
    TopicColorStyle.ROYAL_BLUE -> AppColors.TopicCardRoyalBlue
    TopicColorStyle.GREEN -> AppColors.TopicCardGreen
    TopicColorStyle.ORANGE -> AppColors.TopicCardOrange
    TopicColorStyle.MAGENTA -> AppColors.TopicCardMagenta
    TopicColorStyle.LAVENDER -> AppColors.TopicCardLavender
}

/**
 * Maps [TopicIconType] to the centralized [AppIcons] registry.
 */
fun resolveTopicIcon(iconType: TopicIconType): ImageVector = when (iconType) {
    TopicIconType.ABC_LETTERS -> AppIcons.TopicAbc
    TopicIconType.COLOURS_PALETTE -> AppIcons.TopicColours
    TopicIconType.NUMBERS_123 -> AppIcons.TopicNumbers
    TopicIconType.CLOTHES_SHIRT -> AppIcons.TopicClothes
    TopicIconType.PETS_HOUSE -> AppIcons.TopicPets
    TopicIconType.ZOO_ELEPHANT -> AppIcons.TopicZoo
    TopicIconType.SWEET_CAKE -> AppIcons.TopicSweets
    TopicIconType.FRUITS_APPLE -> AppIcons.TopicFruits
    TopicIconType.CITY_BUILDINGS -> AppIcons.TopicCity
    TopicIconType.SCHOOL_CAP -> AppIcons.TopicSchool
    TopicIconType.FOOD_PLATE -> AppIcons.TopicFood
    TopicIconType.SPORTS_BALLS -> AppIcons.TopicSports
    TopicIconType.FAMILY_GROUP -> AppIcons.TopicFamily
    TopicIconType.ACTIONS_RUN -> AppIcons.TopicActions
    TopicIconType.HOUSE_HOME -> AppIcons.TopicHouse
    TopicIconType.FURNITURE_CHAIR -> AppIcons.TopicFurniture
    TopicIconType.CALENDAR_GRID -> AppIcons.TopicCalendar
    TopicIconType.BODY_SENSES -> AppIcons.TopicBody
}
