package com.kidsenglish.app.feature.roadmap.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import com.kidsenglish.app.core.constants.AppConstants
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.typography.KidsDisplayFontFamily
import kotlin.math.cos
import kotlin.math.sin

/**
 * Full-screen horizontal background matching the reference screenshots:
 * a vibrant cyan-blue to purple-indigo vertical gradient covered in scattered,
 * semi-transparent tilted uppercase English alphabet letters (`A..Z`) that
 * gently float and sway across the canvas.
 */
@Composable
fun AlphabetWatermarkBackground(
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val infiniteTransition = rememberInfiniteTransition(label = "watermark_chars_transition")

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AppConstants.WATERMARK_CHAR_FLOAT_DURATION_MS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "watermark_chars_wave_phase"
    )

    val watermarkStyle = TextStyle(
        fontFamily = KidsDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = AppDimensions.TextSizeDisplayLarge,
        color = AppColors.RoadmapWatermarkLetter
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = AppColors.RoadmapBackgroundBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val cols = 11
            val rows = 5
            val cellWidth = size.width / cols
            val cellHeight = size.height / rows

            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    val index = (row * cols + col) % letters.length
                    val charText = letters[index].toString()
                    val charOffsetPhase = wavePhase + (row * 0.85f) + (col * 0.55f)
                    val floatX = cos(charOffsetPhase) * 6f
                    val floatY = sin(charOffsetPhase) * 8f
                    val centerX = col * cellWidth + cellWidth * 0.35f + floatX
                    val centerY = row * cellHeight + cellHeight * 0.25f + floatY
                    val baseAngle = ((row * 3 + col * 7) % 5 - 2) * 14f
                    val animatedAngle = baseAngle + sin(charOffsetPhase) * 6f

                    rotate(degrees = animatedAngle, pivot = Offset(centerX, centerY)) {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = charText,
                            topLeft = Offset(centerX, centerY),
                            style = watermarkStyle
                        )
                    }
                }
            }
        }
    }
}
