package com.kidsenglish.app.core.character

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kidsenglish.app.core.animation.AnimationSpecs
import com.kidsenglish.app.core.designsystem.dimens.Dimens

enum class FoxState {
    IDLE,
    WALK,
    JUMP,
    GREET,
    THINK,
    CELEBRATE
}

/**
 * Adorable, original 3D-shaded Fox mascot (Rusty / Tulkicha) for the Kids English Roadmap.
 * Rendered with layered depth, specular highlights, and articulated procedural motion.
 */
@Composable
fun FoxMascot3d(
    state: FoxState = FoxState.IDLE,
    size: Dp = Dimens.MascotSizeSmall,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "foxAnim")

    // Breathing & idle bobbing
    val bobbing by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (state == FoxState.CELEBRATE) -12f else -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (state == FoxState.CELEBRATE) 300 else 850,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "foxBob"
    )

    // Tail sway
    val tailSway by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tailSway"
    )

    // Paw wave for greet
    val pawWave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (state == FoxState.GREET) 24f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(250, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pawWave"
    )

    Box(
        modifier = modifier
            .size(size)
            .offset(y = bobbing.dp)
            .clickable { onClick() }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w / 2f, h * 0.52f)

            // Palette
            val furOrange = Color(0xFFFF6E40)
            val furShadow = Color(0xFFE64A19)
            val creamWhite = Color(0xFFFFF9C4)
            val darkChoc = Color(0xFF3E2723)
            val scarfTeal = Color(0xFF00BCD4)
            val scarfShadow = Color(0xFF0097A7)

            // 1. Soft Ground Shadow
            drawOval(
                color = Color.Black.copy(alpha = 0.22f),
                topLeft = Offset(w * 0.18f, h * 0.84f),
                size = Size(w * 0.64f, h * 0.14f)
            )

            // 2. Fluffy Bushy Tail with white tip (rotates behind body)
            rotate(degrees = tailSway, pivot = Offset(w * 0.24f, h * 0.68f)) {
                val tailPath = Path().apply {
                    moveTo(w * 0.24f, h * 0.68f)
                    cubicTo(w * 0.05f, h * 0.58f, -w * 0.08f, h * 0.35f, w * 0.04f, h * 0.22f)
                    cubicTo(w * 0.18f, h * 0.26f, w * 0.28f, h * 0.45f, w * 0.28f, h * 0.62f)
                    close()
                }
                drawPath(
                    path = tailPath,
                    brush = Brush.linearGradient(
                        listOf(furOrange, furShadow),
                        start = Offset(0f, 0f),
                        end = Offset(w * 0.3f, h * 0.7f)
                    )
                )
                // White tail tip
                drawCircle(
                    color = creamWhite,
                    radius = w * 0.075f,
                    center = Offset(w * 0.05f, h * 0.26f)
                )
            }

            // 3. Body
            val bodyPath = Path().apply {
                moveTo(center.x - w * 0.20f, h * 0.76f)
                cubicTo(center.x - w * 0.24f, h * 0.56f, center.x - w * 0.16f, h * 0.42f, center.x, h * 0.42f)
                cubicTo(center.x + w * 0.16f, h * 0.42f, center.x + w * 0.24f, h * 0.56f, center.x + w * 0.20f, h * 0.76f)
                close()
            }
            drawPath(
                path = bodyPath,
                brush = Brush.verticalGradient(
                    listOf(furOrange, furShadow),
                    startY = h * 0.42f,
                    endY = h * 0.78f
                )
            )

            // White Chest / Belly Patch
            drawOval(
                color = creamWhite,
                topLeft = Offset(center.x - w * 0.10f, h * 0.50f),
                size = Size(w * 0.20f, h * 0.25f)
            )

            // 4. Back Paws
            drawCircle(color = darkChoc, radius = w * 0.065f, center = Offset(center.x - w * 0.15f, h * 0.78f))
            drawCircle(color = darkChoc, radius = w * 0.065f, center = Offset(center.x + w * 0.15f, h * 0.78f))

            // 5. Stylized Head
            val headCenter = Offset(center.x, h * 0.35f)
            val headRadius = w * 0.26f

            // Ears
            val leftEarPath = Path().apply {
                moveTo(headCenter.x - headRadius * 0.85f, headCenter.y - headRadius * 0.2f)
                lineTo(headCenter.x - headRadius * 0.7f, headCenter.y - headRadius * 1.55f)
                lineTo(headCenter.x - headRadius * 0.1f, headCenter.y - headRadius * 0.8f)
                close()
            }
            drawPath(path = leftEarPath, color = furOrange)
            // Left ear tip dark
            val leftEarTip = Path().apply {
                moveTo(headCenter.x - headRadius * 0.77f, headCenter.y - headRadius * 1.15f)
                lineTo(headCenter.x - headRadius * 0.7f, headCenter.y - headRadius * 1.55f)
                lineTo(headCenter.x - headRadius * 0.45f, headCenter.y - headRadius * 1.25f)
                close()
            }
            drawPath(path = leftEarTip, color = darkChoc)

            val rightEarPath = Path().apply {
                moveTo(headCenter.x + headRadius * 0.1f, headCenter.y - headRadius * 0.8f)
                lineTo(headCenter.x + headRadius * 0.7f, headCenter.y - headRadius * 1.55f)
                lineTo(headCenter.x + headRadius * 0.85f, headCenter.y - headRadius * 0.2f)
                close()
            }
            drawPath(path = rightEarPath, color = furOrange)
            val rightEarTip = Path().apply {
                moveTo(headCenter.x + headRadius * 0.45f, headCenter.y - headRadius * 1.25f)
                lineTo(headCenter.x + headRadius * 0.7f, headCenter.y - headRadius * 1.55f)
                lineTo(headCenter.x + headRadius * 0.77f, headCenter.y - headRadius * 1.15f)
                close()
            }
            drawPath(path = rightEarTip, color = darkChoc)

            // Head Base
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFF8A65), furOrange),
                    center = headCenter,
                    radius = headRadius
                ),
                radius = headRadius,
                center = headCenter
            )

            // Fluffy White Cheeks
            val cheekY = headCenter.y + headRadius * 0.25f
            drawCircle(color = creamWhite, radius = headRadius * 0.42f, center = Offset(headCenter.x - headRadius * 0.5f, cheekY))
            drawCircle(color = creamWhite, radius = headRadius * 0.42f, center = Offset(headCenter.x + headRadius * 0.5f, cheekY))

            // Snout / Muzzle
            drawOval(
                color = creamWhite,
                topLeft = Offset(headCenter.x - headRadius * 0.32f, headCenter.y + headRadius * 0.1f),
                size = Size(headRadius * 0.64f, headRadius * 0.55f)
            )

            // Black Button Nose
            drawOval(
                color = darkChoc,
                topLeft = Offset(headCenter.x - headRadius * 0.11f, headCenter.y + headRadius * 0.22f),
                size = Size(headRadius * 0.22f, headRadius * 0.16f)
            )

            // Eyes
            val eyeY = headCenter.y + headRadius * 0.05f
            val eyeSpacing = headRadius * 0.42f
            val eyeRadius = headRadius * 0.16f

            if (state == FoxState.CELEBRATE || state == FoxState.JUMP) {
                // Cheerful arc eyes ^ ^
                drawArc(
                    color = darkChoc,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(headCenter.x - eyeSpacing - eyeRadius, eyeY - eyeRadius),
                    size = Size(eyeRadius * 2, eyeRadius * 2),
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = darkChoc,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(headCenter.x + eyeSpacing - eyeRadius, eyeY - eyeRadius),
                    size = Size(eyeRadius * 2, eyeRadius * 2),
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )
            } else {
                // Large glossy Pixar eyes
                drawCircle(color = darkChoc, radius = eyeRadius, center = Offset(headCenter.x - eyeSpacing, eyeY))
                drawCircle(color = Color.White, radius = eyeRadius * 0.45f, center = Offset(headCenter.x - eyeSpacing - 2f, eyeY - 2f))
                drawCircle(color = Color.White, radius = eyeRadius * 0.2f, center = Offset(headCenter.x - eyeSpacing + 3f, eyeY + 3f))

                drawCircle(color = darkChoc, radius = eyeRadius, center = Offset(headCenter.x + eyeSpacing, eyeY))
                drawCircle(color = Color.White, radius = eyeRadius * 0.45f, center = Offset(headCenter.x + eyeSpacing - 2f, eyeY - 2f))
                drawCircle(color = Color.White, radius = eyeRadius * 0.2f, center = Offset(headCenter.x + eyeSpacing + 3f, eyeY + 3f))
            }

            // Smiling Mouth
            drawArc(
                color = darkChoc,
                startAngle = 20f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(headCenter.x - headRadius * 0.14f, headCenter.y + headRadius * 0.38f),
                size = Size(headRadius * 0.28f, headRadius * 0.2f),
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            // 6. Cozy Scarf wrapping around neck
            val scarfY = headCenter.y + headRadius * 0.72f
            drawRoundRect(
                color = scarfTeal,
                topLeft = Offset(headCenter.x - headRadius * 0.55f, scarfY),
                size = Size(headRadius * 1.1f, headRadius * 0.26f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )
            // Scarf tails hanging down
            val tailEndPath = Path().apply {
                moveTo(headCenter.x + headRadius * 0.15f, scarfY + headRadius * 0.2f)
                lineTo(headCenter.x + headRadius * 0.35f, scarfY + headRadius * 0.7f)
                lineTo(headCenter.x + headRadius * 0.05f, scarfY + headRadius * 0.65f)
                close()
            }
            drawPath(path = tailEndPath, color = scarfShadow)

            // 7. Waving Front Paw (for greet/celebrate)
            if (state == FoxState.GREET) {
                rotate(degrees = pawWave, pivot = Offset(center.x + w * 0.18f, h * 0.56f)) {
                    drawCircle(color = darkChoc, radius = w * 0.055f, center = Offset(center.x + w * 0.28f, h * 0.48f))
                }
            }
        }
    }
}
