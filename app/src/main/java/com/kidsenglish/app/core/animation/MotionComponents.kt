package com.kidsenglish.app.core.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Reusable motion modifier that applies an error shake animation.
 */
@Composable
fun rememberShakeController(): ShakeController {
    return remember { ShakeController() }
}

class ShakeController {
    val animatable = Animatable(0f)

    suspend fun shake() {
        animatable.snapTo(0f)
        for (i in 0..3) {
            animatable.animateTo(
                targetValue = if (i % 2 == 0) 12f else -12f,
                animationSpec = tween(50, easing = LinearEasing)
            )
        }
        animatable.animateTo(0f, animationSpec = tween(50))
    }
}

fun Modifier.shake(controller: ShakeController): Modifier = this.then(
    Modifier.offset { IntOffset(x = controller.animatable.value.roundToInt(), y = 0) }
)

/**
 * Wobble celebration animation for correct answer feedback.
 */
@Composable
fun Modifier.wobble(trigger: Boolean): Modifier {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            rotation.animateTo(8f, tween(60))
            rotation.animateTo(-8f, tween(60))
            rotation.animateTo(4f, tween(60))
            rotation.animateTo(-4f, tween(60))
            rotation.animateTo(0f, tween(60))
        }
    }

    return this.then(Modifier.rotate(rotation.value))
}

/**
 * Interactive bouncy card/button wrapper that provides tactile squish feedback on press.
 */
@Composable
fun BouncyBox(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .scale(scale.value)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectTapGestures(
                    onPress = {
                        scope.launch {
                            scale.animateTo(AnimationConstants.SCALE_PRESSED, AnimationSpecs.SnappySpring)
                        }
                        tryAwaitRelease()
                        scope.launch {
                            scale.animateTo(1f, AnimationSpecs.BouncySpring)
                        }
                    },
                    onTap = {
                        onClick()
                    }
                )
            }
    ) {
        content()
    }
}

/**
 * Celebration confetti particle canvas for lesson and level completion.
 */
@Composable
fun ConfettiOverlay(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isActive) return

    val particles = remember {
        List(40) {
            ConfettiParticle(
                xRatio = Random.nextFloat(),
                speed = Random.nextFloat() * 0.6f + 0.4f,
                size = Random.nextFloat() * 10f + 6f,
                color = listOf(
                    Color(0xFFFFC107),
                    Color(0xFFFF5722),
                    Color(0xFF4CAF50),
                    Color(0xFF29B6F6),
                    Color(0xFFAB47BC)
                ).random()
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "confetti")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(AnimationConstants.DURATION_CONFETTI, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiProgress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { p ->
            val y = (progress * p.speed * size.height * 1.2f) % (size.height + 40f) - 20f
            val x = p.xRatio * size.width + kotlin.math.sin(progress * 10f + p.xRatio * 20f) * 15f
            drawCircle(
                color = p.color,
                radius = p.size,
                center = Offset(x, y)
            )
        }
    }
}

private data class ConfettiParticle(
    val xRatio: Float,
    val speed: Float,
    val size: Float,
    val color: Color
)
