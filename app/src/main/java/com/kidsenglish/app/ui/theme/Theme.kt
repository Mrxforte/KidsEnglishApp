package com.kidsenglish.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KidsLightColorScheme = lightColorScheme(
    primary = NodeAvailable,
    onPrimary = Color.White,
    secondary = NodeCurrent,
    onSecondary = Color.White,
    tertiary = NodeCompleted,
    background = SoftSky,
    surface = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun KidsEnglishTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KidsLightColorScheme,
        content = content
    )
}
