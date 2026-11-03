package com.kidsenglish.app.feature.splash.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.icons.AppIcons
import com.kidsenglish.app.core.designsystem.images.AppImages
import com.kidsenglish.app.core.resources.AppStrings

/**
 * Central hero mascot medallion displayed on the Splash Screen.
 *
 * Features a soft golden halo, an elevated circular card with the friendly fox mascot
 * illustration ([AppImages.SplashMascot]), a subtle floating animation, and a corner
 * brand logo badge ([AppIcons.AppLogo]).
 */
@Composable
fun SplashMascotMedallion(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_float")

    val floatMultiplier by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.SPLASH_FLOAT_ANIMATION_DURATION_MS,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mascot_float_offset"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .offset(y = AppDimensions.SplashFloatOffsetDp * floatMultiplier)
            .testTag(AppConstants.TestTags.SPLASH_MASCOT)
    ) {
        // Soft sunny halo behind the medallion
        Box(
            modifier = Modifier
                .size(AppDimensions.SplashMascotHaloSize)
                .background(
                    color = AppColors.SplashMascotHalo,
                    shape = CircleShape
                )
        )

        // Elevated circular mascot card
        Surface(
            shape = CircleShape,
            color = AppColors.SplashMascotCardSurface,
            shadowElevation = AppDimensions.ElevationHigh,
            border = BorderStroke(
                width = AppDimensions.BorderThick,
                color = AppColors.SplashMascotCardBorder
            ),
            modifier = Modifier.size(AppDimensions.SplashMascotCardSize)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = AppImages.SplashMascot),
                    contentDescription = stringResource(id = AppStrings.Splash.CD_MASCOT),
                    modifier = Modifier.size(AppDimensions.SplashMascotImageSize)
                )
            }
        }

        // Attached corner brand logo emblem
        Surface(
            shape = CircleShape,
            color = AppColors.SplashMascotCardSurface,
            shadowElevation = AppDimensions.ElevationMedium,
            border = BorderStroke(
                width = AppDimensions.BorderMedium,
                color = AppColors.SplashBadgeBorder
            ),
            modifier = Modifier
                .size(AppDimensions.SplashCornerLogoBadgeSize)
                .align(Alignment.BottomEnd)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = AppIcons.AppLogo),
                    contentDescription = stringResource(id = AppStrings.Splash.CD_LOGO),
                    modifier = Modifier.size(AppDimensions.SplashCornerLogoIconSize)
                )
            }
        }
    }
}
