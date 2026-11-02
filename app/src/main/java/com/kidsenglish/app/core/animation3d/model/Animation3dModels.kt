package com.kidsenglish.app.core.animation3d.model

/**
 * 3D Animation clips exported from Blender via glTF / GLB skeletal animations.
 */
enum class Character3dAnimation(val clipName: String) {
    IDLE("idle_loop"),
    WALK("walk_cycle"),
    JUMP("jump_happy"),
    DANCE("victory_dance"),
    GREET("wave_hello"),
    CELEBRATE("star_celebration")
}

/**
 * 3D Learning environments for immersive vocabulary discovery.
 */
enum class Environment3dType {
    SUNNY_MEADOW,
    MAGIC_CLASSROOM,
    ALPHABET_ISLAND
}

/**
 * Descriptor for 3D model asset integration.
 */
data class Model3dAsset(
    val assetPath: String,
    val format: ModelFormat = ModelFormat.GLB,
    val scaleFactor: Float = 1.0f,
    val defaultAnimation: Character3dAnimation = Character3dAnimation.IDLE
)

enum class ModelFormat {
    GLB,
    GLTF
}
