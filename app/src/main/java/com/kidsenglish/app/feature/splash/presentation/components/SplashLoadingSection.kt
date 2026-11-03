package com.kidsenglish.app.feature.splash.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions

/**
 * Subtle bottom loading indicator displaying smooth animated progress and a localized
 * status message on the Splash Screen.
 */
@Composable
fun SplashLoadingSection(
    progress: Float,
    @StringRes statusMessageRes: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = AppConstants.SPLASH_ENTRANCE_DURATION_MS,
            easing = FastOutSlowInEasing
        ),
        label = "splash_progress_animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.testTag(AppConstants.TestTags.SPLASH_LOADING_INDICATOR)
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            color = AppColors.SplashLoadingIndicator,
            trackColor = AppColors.SplashLoadingTrack,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .width(AppDimensions.SplashLoadingBarWidth)
                .height(AppDimensions.SplashLoadingBarHeight)
                .clip(RoundedCornerShape(AppDimensions.RadiusPill))
        )

        Spacer(modifier = Modifier.height(AppDimensions.SpaceS))

        Text(
            text = stringResource(id = statusMessageRes),
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.SplashSubtitle,
            textAlign = TextAlign.Center
        )
    }
}
