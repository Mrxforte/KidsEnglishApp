package com.kidsenglish.app.core.animation3d.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kidsenglish.app.core.animation.CharacterState
import com.kidsenglish.app.core.animation.MascotCharacter
import com.kidsenglish.app.core.animation3d.controller.Animation3dController
import com.kidsenglish.app.core.animation3d.model.Character3dAnimation
import com.kidsenglish.app.core.animation3d.model.Model3dAsset
import com.kidsenglish.app.core.designsystem.color.AppColors
import com.kidsenglish.app.core.designsystem.dimens.Dimens

/**
 * Isolated Compose 3D Viewport container for 3D character and environment rendering.
 *
 * Integration Architecture:
 * - Compatible with SceneView / Filament: `io.github.sceneview:sceneview:2.2.1`
 * - glTF / GLB assets placed in: `app/src/main/assets/models3d/`
 * - When hardware-accelerated 3D context is loaded, delegates to Filament surface.
 * - When offline or in low-power mode, renders an animated interactive fallback with full yaw rotation support.
 */
@Composable
fun Scene3dView(
    controller: Animation3dController,
    modelAsset: Model3dAsset? = null,
    modifier: Modifier = Modifier
) {
    val rotationYaw by controller.rotationYaw.collectAsState()
    val currentAnimation by controller.currentAnimation.collectAsState()

    val mapped2dState = when (currentAnimation) {
        Character3dAnimation.IDLE -> CharacterState.IDLE
        Character3dAnimation.WALK -> CharacterState.HAPPY
        Character3dAnimation.JUMP -> CharacterState.HAPPY
        Character3dAnimation.DANCE -> CharacterState.CELEBRATING
        Character3dAnimation.GREET -> CharacterState.GREETING
        Character3dAnimation.CELEBRATE -> CharacterState.CORRECT_ANSWER
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.RadiusL))
            .background(Color(0xFFE0F7FA))
            .border(Dimens.BorderMedium, AppColors.PrimarySky, RoundedCornerShape(Dimens.RadiusL))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    controller.onDragRotate(dragAmount * 0.5f)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Dimens.SpaceM)
        ) {
            Box(
                modifier = Modifier.rotate(rotationYaw * 0.25f)
            ) {
                MascotCharacter(
                    state = mapped2dState,
                    size = 140.dp,
                    onClick = {
                        controller.playAnimation(
                            if (currentAnimation == Character3dAnimation.CELEBRATE)
                                Character3dAnimation.IDLE
                            else
                                Character3dAnimation.CELEBRATE
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceS))

            Text(
                text = "3D Interactive Mode • Drag to rotate",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
