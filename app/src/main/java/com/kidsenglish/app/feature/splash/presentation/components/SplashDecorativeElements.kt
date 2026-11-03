package com.kidsenglish.app.feature.splash.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.icons.AppIcons

/**
 * Subtle animated background decorations (soft clouds and twinkling stars)
 * that give the Splash Screen a warm, playful atmosphere without crowding the layout.
 */
@Composable
fun SplashDecorativeElements(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash_decorations")

    val starScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.SPLASH_STAR_PULSE_DURATION_MS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_pulse_scale"
    )

    val starAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.SPLASH_STAR_PULSE_DURATION_MS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = AppDimensions.ScreenHorizontalPadding,
                vertical = AppDimensions.SpaceXXL
            )
    ) {
        // Top atmospheric row: soft cloud pill and twinkling star
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(AppDimensions.SplashCloudWidth)
                    .height(AppDimensions.SplashCloudHeight)
                    .background(
                        color = AppColors.SplashCloudWhite,
                        shape = RoundedCornerShape(AppDimensions.RadiusPill)
                    )
            )

            Icon(
                imageVector = AppIcons.SparkleStar,
                contentDescription = null,
                tint = AppColors.SplashSparkleGold,
                modifier = Modifier
                    .size(AppDimensions.SplashSparkleSizeMedium)
                    .scale(starScale)
                    .alpha(starAlpha)
            )
        }

        // Bottom atmospheric row: small sparkle and soft cloud pill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = AppDimensions.SpaceXXL),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = AppIcons.MagicSparkle,
                contentDescription = null,
                tint = AppColors.SplashSparkleGold,
                modifier = Modifier
                    .size(AppDimensions.SplashSparkleSizeSmall)
                    .scale(starScale)
                    .alpha(starAlpha)
            )

            Box(
                modifier = Modifier
                    .width(AppDimensions.SplashCloudWidth)
                    .height(AppDimensions.SplashCloudHeight)
                    .background(
                        color = AppColors.SplashCloudWhite,
                        shape = RoundedCornerShape(AppDimensions.RadiusPill)
                    )
            )
        }
    }
}
