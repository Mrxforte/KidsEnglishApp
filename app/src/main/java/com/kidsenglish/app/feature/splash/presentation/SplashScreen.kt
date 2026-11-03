package com.kidsenglish.app.feature.splash.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.theme.AppTheme
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.feature.splash.presentation.components.SplashDecorativeElements
import com.kidsenglish.app.feature.splash.presentation.components.SplashLoadingSection
import com.kidsenglish.app.feature.splash.presentation.components.SplashMascotMedallion
import kotlinx.coroutines.delay

/**
 * Stateful route composable connecting [SplashViewModel] to the stateless [SplashScreen].
 */
@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit = {},
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isReady) {
        if (uiState.isReady) {
            onSplashFinished()
        }
    }

    SplashScreen(
        uiState = uiState,
        onTapToStart = onSplashFinished,
        modifier = modifier
    )
}

/**
 * Stateless, responsive Splash Screen composable representing the Kids English brand identity.
 *
 * Tapping anywhere on the screen allows the user to immediately enter the Levels Roadmap menu screen.
 */
@Composable
fun SplashScreen(
    uiState: SplashUiState,
    onTapToStart: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var startEntranceAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startEntranceAnimation = true
    }

    val contentScale by animateFloatAsState(
        targetValue = if (startEntranceAnimation) 1.0f else 0.88f,
        animationSpec = tween(
            durationMillis = AppConstants.SPLASH_ENTRANCE_DURATION_MS,
            easing = FastOutSlowInEasing
        ),
        label = "splash_entrance_scale"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (startEntranceAnimation) 1.0f else 0.0f,
        animationSpec = tween(
            durationMillis = AppConstants.SPLASH_ENTRANCE_DURATION_MS,
            easing = FastOutSlowInEasing
        ),
        label = "splash_entrance_alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(brush = AppColors.SplashBackgroundBrush)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .clickable(onClick = onTapToStart)
            .testTag(AppConstants.TestTags.SPLASH_SCREEN_ROOT)
    ) {
        // Subtle background clouds and twinkling stars
        SplashDecorativeElements()

        // Responsive central column constrained for phones, foldables, and tablets
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = AppDimensions.MaxContentWidth)
                .padding(
                    horizontal = AppDimensions.ScreenHorizontalPadding,
                    vertical = AppDimensions.ScreenVerticalPadding
                )
                .scale(contentScale)
                .alpha(contentAlpha)
        ) {
            // Top playful welcome badge pill
            Surface(
                shape = RoundedCornerShape(AppDimensions.RadiusPill),
                color = AppColors.SplashBadgeBackground,
                shadowElevation = AppDimensions.ElevationLow,
                border = BorderStroke(
                    width = AppDimensions.BorderThin,
                    color = AppColors.SplashBadgeBorder
                ),
                modifier = Modifier.testTag(AppConstants.TestTags.SPLASH_BADGE)
            ) {
                Text(
                    text = stringResource(id = AppStrings.Splash.BADGE_LABEL),
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.SplashBadgeText,
                    modifier = Modifier.padding(
                        horizontal = AppDimensions.SplashBadgeHorizontalPadding,
                        vertical = AppDimensions.SplashBadgeVerticalPadding
                    )
                )
            }

            // Center hero identity: Mascot medallion + Application Title + Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                SplashMascotMedallion()

                Spacer(modifier = Modifier.height(AppDimensions.SplashTitleTopSpacing))

                Text(
                    text = stringResource(id = AppStrings.Splash.WELCOME_TITLE),
                    style = MaterialTheme.typography.displayLarge,
                    color = AppColors.SplashTitle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag(AppConstants.TestTags.SPLASH_TITLE)
                )

                Spacer(modifier = Modifier.height(AppDimensions.SplashSubtitleTopSpacing))

                Text(
                    text = stringResource(id = AppStrings.Splash.SUBTITLE),
                    style = MaterialTheme.typography.titleLarge,
                    color = AppColors.SplashSubtitle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag(AppConstants.TestTags.SPLASH_SUBTITLE)
                )
            }

            // Bottom loading progress indicator
            SplashLoadingSection(
                progress = uiState.loadingProgress,
                statusMessageRes = uiState.statusMessageRes
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        SplashScreen(
            uiState = SplashUiState(
                loadingProgress = 0.75f
            )
        )
    }
}
