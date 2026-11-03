package com.kidsenglish.app.core.designsystem.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.kidsenglish.app.R
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions

/**
 * Rounded, playful display font family (Fredoka) bundled locally in `res/font/fredoka.ttf`.
 * Used for hero titles, brand headers, and child-friendly display headings.
 */
val KidsDisplayFontFamily = FontFamily(
    Font(R.font.fredoka, FontWeight.Normal),
    Font(R.font.fredoka, FontWeight.Medium),
    Font(R.font.fredoka, FontWeight.SemiBold),
    Font(R.font.fredoka, FontWeight.Bold)
)

/**
 * Clean, highly legible rounded sans-serif font family (Nunito) bundled locally in `res/font/nunito.ttf`.
 * Supports Uzbek Latin characters (`o‘`, `g‘`, `sh`, `ch`), Cyrillic, and English alphabets.
 */
val KidsBodyFontFamily = FontFamily(
    Font(R.font.nunito, FontWeight.Normal),
    Font(R.font.nunito, FontWeight.Medium),
    Font(R.font.nunito, FontWeight.SemiBold),
    Font(R.font.nunito, FontWeight.Bold)
)

/**
 * Centralized Material 3 [Typography] configuration for Kids English.
 * All font sizes and line heights are sourced from [AppDimensions].
 */
val AppTypography = Typography(
    // Splash Screen main brand title ("Kids English")
    displayLarge = TextStyle(
        fontFamily = KidsDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = AppDimensions.TextSizeDisplayLarge,
        lineHeight = AppDimensions.LineHeightDisplayLarge
    ),
    // Major feature headers
    displayMedium = TextStyle(
        fontFamily = KidsDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = AppDimensions.TextSizeDisplayMedium,
        lineHeight = AppDimensions.LineHeightDisplayMedium
    ),
    // Screen headlines
    headlineLarge = TextStyle(
        fontFamily = KidsDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = AppDimensions.TextSizeHeadlineLarge,
        lineHeight = AppDimensions.LineHeightHeadlineLarge
    ),
    // Splash Screen friendly subtitle and card headers
    titleLarge = TextStyle(
        fontFamily = KidsBodyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = AppDimensions.TextSizeTitleLarge,
        lineHeight = AppDimensions.LineHeightTitleLarge
    ),
    // Welcome pill badge and prominent labels
    titleMedium = TextStyle(
        fontFamily = KidsBodyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = AppDimensions.TextSizeTitleMedium,
        lineHeight = AppDimensions.LineHeightTitleMedium
    ),
    // Primary body copy
    bodyLarge = TextStyle(
        fontFamily = KidsBodyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = AppDimensions.TextSizeBodyLarge,
        lineHeight = AppDimensions.LineHeightBodyLarge
    ),
    // Splash Screen loading status text and secondary copy
    bodyMedium = TextStyle(
        fontFamily = KidsBodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = AppDimensions.TextSizeBodyMedium,
        lineHeight = AppDimensions.LineHeightBodyMedium
    ),
    // Button text
    labelLarge = TextStyle(
        fontFamily = KidsDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = AppDimensions.TextSizeLabelLarge,
        lineHeight = AppDimensions.LineHeightLabelLarge
    )
)
