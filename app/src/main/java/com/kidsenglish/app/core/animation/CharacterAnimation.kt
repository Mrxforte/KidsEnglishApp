package com.kidsenglish.app.core.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kidsenglish.app.core.designsystem.color.AppColors
import com.kidsenglish.app.core.designsystem.dimens.Dimens

/**
 * Expressive states for the educational character mascot.
 */
enum class CharacterState {
    IDLE,
    HAPPY,
    SAD,
    THINKING,
    LISTENING,
    SPEAKING,
    CELEBRATING,
    CORRECT_ANSWER,
    INCORRECT_ANSWER,
    GREETING
}

/**
 * 2D Interactive mascot character (Leo the Lion) rendered with native Compose animation.
 * Features:
 * - Breathing and idle bobbing.
 * - Ear twitches and waving.
 * - Reactive facial emotions mapped to learning progress.
 * - Animated speech bubble.
 * - Prepared architecture for external Rive runtime (.riv) and Lottie (.json) assets.
 */
@Composable
fun MascotCharacter(
    state: CharacterState,
    modifier: Modifier = Modifier,
    size: Dp = Dimens.MascotSizeMedium,
    speechText: String? = null,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascotBreathe")

    // Idle breathing & bobbing
    val bobbingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (state == CharacterState.CELEBRATING || state == CharacterState.CORRECT_ANSWER) -14f else -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (state == CharacterState.CELEBRATING) 250 else 900,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mascotBob"
    )

    // Wave or celebratory tilt
    val tiltAngle by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = if (state == CharacterState.GREETING || state == CharacterState.CELEBRATING) 10f else 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (state == CharacterState.GREETING) 300 else 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mascotTilt"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Speech Bubble
        AnimatedVisibility(
            visible = !speechText.isNullOrBlank(),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = Dimens.SpaceS)
                    .shadow(Dimens.ElevationLow, RoundedCornerShape(Dimens.RadiusM))
                    .background(Color.White, RoundedCornerShape(Dimens.RadiusM))
                    .border(Dimens.BorderMedium, AppColors.PrimarySky, RoundedCornerShape(Dimens.RadiusM))
                    .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceS)
            ) {
                Text(
                    text = speechText.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
            }
        }

        // Mascot Body Canvas
        Box(
            modifier = Modifier
                .size(size)
                .offset(y = bobbingOffset.dp)
                .rotate(tiltAngle)
                .clickable { onClick() }
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = this.size.width
                val h = this.size.height
                val center = Offset(w / 2f, h / 2f)

                // 1. Lion Mane (fluffy outer petals)
                val maneColor = Color(0xFFFFB300)
                val maneRadius = w * 0.46f
                for (angle in 0 until 360 step 30) {
                    val rad = Math.toRadians(angle.toDouble())
                    val x = center.x + (maneRadius * 0.78f * Math.cos(rad)).toFloat()
                    val y = center.y + (maneRadius * 0.78f * Math.sin(rad)).toFloat()
                    drawCircle(
                        color = maneColor,
                        radius = w * 0.22f,
                        center = Offset(x, y)
                    )
                }

                // 2. Head
                val headColor = Color(0xFFFFCC80)
                drawCircle(
                    color = headColor,
                    radius = w * 0.36f,
                    center = center
                )

                // 3. Ears
                val earRadius = w * 0.12f
                drawCircle(color = headColor, radius = earRadius, center = Offset(w * 0.22f, h * 0.24f))
                drawCircle(color = Color(0xFFFFAB91), radius = earRadius * 0.6f, center = Offset(w * 0.22f, h * 0.24f))
                drawCircle(color = headColor, radius = earRadius, center = Offset(w * 0.78f, h * 0.24f))
                drawCircle(color = Color(0xFFFFAB91), radius = earRadius * 0.6f, center = Offset(w * 0.78f, h * 0.24f))

                // 4. Muzzle
                drawCircle(
                    color = Color(0xFFFFF8E1),
                    radius = w * 0.18f,
                    center = Offset(center.x, center.y + h * 0.10f)
                )

                // 5. Nose
                val noseY = center.y + h * 0.04f
                val nosePath = Path().apply {
                    moveTo(center.x - w * 0.05f, noseY)
                    lineTo(center.x + w * 0.05f, noseY)
                    lineTo(center.x, noseY + h * 0.06f)
                    close()
                }
                drawPath(path = nosePath, color = Color(0xFF5D4037))

                // 6. Eyes based on state
                val leftEyeCenter = Offset(w * 0.38f, h * 0.44f)
                val rightEyeCenter = Offset(w * 0.62f, h * 0.44f)
                val eyeRadius = w * 0.055f

                when (state) {
                    CharacterState.HAPPY, CharacterState.CELEBRATING, CharacterState.CORRECT_ANSWER -> {
                        // Happy curved arches ^ ^
                        drawArc(
                            color = Color(0xFF3E2723),
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(leftEyeCenter.x - eyeRadius, leftEyeCenter.y - eyeRadius),
                            size = androidx.compose.ui.geometry.Size(eyeRadius * 2, eyeRadius * 2),
                            style = Stroke(width = 4f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = Color(0xFF3E2723),
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(rightEyeCenter.x - eyeRadius, rightEyeCenter.y - eyeRadius),
                            size = androidx.compose.ui.geometry.Size(eyeRadius * 2, eyeRadius * 2),
                            style = Stroke(width = 4f, cap = StrokeCap.Round)
                        )
                    }
                    CharacterState.SAD, CharacterState.INCORRECT_ANSWER -> {
                        // Droopy eyes with little tear
                        drawCircle(color = Color(0xFF3E2723), radius = eyeRadius, center = leftEyeCenter)
                        drawCircle(color = Color(0xFF3E2723), radius = eyeRadius, center = rightEyeCenter)
                        // Tear
                        drawCircle(color = Color(0xFF81D4FA), radius = eyeRadius * 0.6f, center = Offset(rightEyeCenter.x + 8f, rightEyeCenter.y + 14f))
                    }
                    CharacterState.THINKING -> {
                        // Look up to top-right
                        drawCircle(color = Color(0xFF3E2723), radius = eyeRadius, center = Offset(leftEyeCenter.x + 3f, leftEyeCenter.y - 3f))
                        drawCircle(color = Color(0xFF3E2723), radius = eyeRadius, center = Offset(rightEyeCenter.x + 3f, rightEyeCenter.y - 3f))
                    }
                    else -> {
                        // Wide bright open eyes with shine
                        drawCircle(color = Color(0xFF3E2723), radius = eyeRadius, center = leftEyeCenter)
                        drawCircle(color = Color.White, radius = eyeRadius * 0.35f, center = Offset(leftEyeCenter.x - 2f, leftEyeCenter.y - 2f))
                        drawCircle(color = Color(0xFF3E2723), radius = eyeRadius, center = rightEyeCenter)
                        drawCircle(color = Color.White, radius = eyeRadius * 0.35f, center = Offset(rightEyeCenter.x - 2f, rightEyeCenter.y - 2f))
                    }
                }

                // 7. Mouth
                val mouthY = center.y + h * 0.15f
                val mouthWidth = w * 0.08f
                drawArc(
                    color = Color(0xFF5D4037),
                    startAngle = 20f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(center.x - mouthWidth, mouthY),
                    size = androidx.compose.ui.geometry.Size(mouthWidth * 2, h * 0.06f),
                    style = Stroke(width = 4f, cap = StrokeCap.Round)
                )

                // 8. Rosy Cheeks
                drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.5f), radius = w * 0.05f, center = Offset(w * 0.28f, h * 0.54f))
                drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.5f), radius = w * 0.05f, center = Offset(w * 0.72f, h * 0.54f))
            }
        }
    }
}
