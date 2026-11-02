package com.kidsenglish.app.core.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/**
 * Spring-based physics motion specs tailored for delightful, responsive child interactions.
 */
object AnimationSpecs {
    // Playful, bouncy feedback for button clicks & star collection
    val BouncySpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Snappy spring for card selections and toggles
    val SnappySpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Gentle spring for mascot bobbing and natural motion
    val GentleSpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow
    )

    // Smooth tween for crossfades
    val SmoothTween: AnimationSpec<Float> = tween(
        durationMillis = AnimationConstants.DURATION_SMOOTH,
        easing = FastOutSlowInEasing
    )
}
