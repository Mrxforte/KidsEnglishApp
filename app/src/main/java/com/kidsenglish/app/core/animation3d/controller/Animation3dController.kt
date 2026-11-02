package com.kidsenglish.app.core.animation3d.controller

import com.kidsenglish.app.core.animation3d.model.Character3dAnimation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Controller for managing 3D character animations, touch-based yaw rotation,
 * camera framing, and battery-conscious frame rate limits.
 */
class Animation3dController(
    initialAnimation: Character3dAnimation = Character3dAnimation.IDLE
) {
    private val _currentAnimation = MutableStateFlow(initialAnimation)
    val currentAnimation: StateFlow<Character3dAnimation> = _currentAnimation.asStateFlow()

    private val _rotationYaw = MutableStateFlow(0f)
    val rotationYaw: StateFlow<Float> = _rotationYaw.asStateFlow()

    private val _isRenderingActive = MutableStateFlow(true)
    val isRenderingActive: StateFlow<Boolean> = _isRenderingActive.asStateFlow()

    fun playAnimation(animation: Character3dAnimation) {
        _currentAnimation.value = animation
    }

    fun onDragRotate(deltaDegrees: Float) {
        _rotationYaw.value = (_rotationYaw.value + deltaDegrees) % 360f
    }

    fun setRenderingPaused(paused: Boolean) {
        _isRenderingActive.value = !paused
    }

    fun resetView() {
        _rotationYaw.value = 0f
        _currentAnimation.value = Character3dAnimation.IDLE
    }
}
