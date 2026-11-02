package com.kidsenglish.app.ui.theme

import androidx.compose.runtime.Composable
import com.kidsenglish.app.core.theme.KidsEnglishAppTheme

/**
 * Backwards-compatible theme delegate connecting legacy UI calls to the centralized Design System.
 */
@Composable
fun KidsEnglishTheme(
    content: @Composable () -> Unit
) {
    KidsEnglishAppTheme(content = content)
}
