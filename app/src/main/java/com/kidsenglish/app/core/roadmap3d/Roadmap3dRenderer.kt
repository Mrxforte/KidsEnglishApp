package com.kidsenglish.app.core.roadmap3d

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.kidsenglish.app.domain.model.Camera3D
import com.kidsenglish.app.domain.model.NodeState3D
import com.kidsenglish.app.domain.model.RoadmapNode3D
import com.kidsenglish.app.domain.model.Vector3D
import kotlin.math.cos
import kotlin.math.sin

/**
 * 2D projected screen coordinate with depth scale and z-depth for sorting.
 */
data class ProjectedPoint(
    val x: Float,
    val y: Float,
    val scale: Float,
    val depthZ: Float,
    val isVisible: Boolean
)

/**
 * High-performance 3D projection and mathematical rendering pipeline for the Kids English Adventure Roadmap.
 */
object Roadmap3dRenderer {

    /**
     * Projects a 3D coordinate in world space onto the 2D viewport using camera pitch and perspective division.
     */
    fun project(
        worldPos: Vector3D,
        camera: Camera3D,
        screenWidth: Float,
        screenHeight: Float
    ): ProjectedPoint {
        // 1. Camera translation
        val dx = worldPos.x - camera.x
        val dy = worldPos.y - camera.y
        val dz = worldPos.z - camera.z

        // 2. Camera pitch rotation (around X axis)
        val radPitch = Math.toRadians(camera.pitch.toDouble()).toFloat()
        val cosPitch = cos(radPitch)
        val sinPitch = sin(radPitch)

        val yRot = dy * cosPitch - dz * sinPitch
        val zRot = dy * sinPitch + dz * cosPitch

        // 3. Near plane clipping
        if (zRot <= 10f) {
            return ProjectedPoint(0f, 0f, 0f, zRot, false)
        }

        // 4. Perspective projection
        val scale = camera.fov / zRot
        val screenX = screenWidth / 2f + dx * scale
        val screenY = screenHeight / 2f + yRot * scale

        val isVisible = screenX >= -200f && screenX <= screenWidth + 200f &&
                screenY >= -200f && screenY <= screenHeight + 200f

        return ProjectedPoint(screenX, screenY, scale, zRot, isVisible)
    }

    /**
     * Renders Green Valley Pixar-style environment backdrop with rolling hills, depth fog, and river.
     */
    fun DrawScope.renderGreenValleyEnvironment(
        camera: Camera3D,
        timeSeconds: Float
    ) {
        val w = size.width
        val h = size.height

        // 1. Sky Gradient with warm sunlight
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF81D4FA), // Radiant sky blue
                    Color(0xFFB3E5FC),
                    Color(0xFFE1F5FE)
                ),
                startY = 0f,
                endY = h * 0.45f
            )
        )

        // 2. Background Rolling Hills (Distant)
        val hillFar = Path().apply {
            moveTo(0f, h * 0.35f)
            cubicTo(w * 0.25f, h * 0.28f, w * 0.65f, h * 0.38f, w, h * 0.30f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(hillFar, Color(0xFF81C784).copy(alpha = 0.55f)) // Atmospheric haze

        // 3. Middle Rolling Hills
        val hillMid = Path().apply {
            moveTo(0f, h * 0.45f)
            cubicTo(w * 0.35f, h * 0.52f, w * 0.70f, h * 0.40f, w, h * 0.48f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(hillMid, Color(0xFF66BB6A))

        // 4. Foreground Lush Valley Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF66BB6A),
                    Color(0xFF4CAF50),
                    Color(0xFF43A047),
                    Color(0xFF388E3C)
                ),
                startY = h * 0.45f,
                endY = h
            )
        )

        // 5. Stylized Winding River with specular waves
        val riverPath = Path().apply {
            val startY = -camera.y * 0.2f
            moveTo(w * 0.15f, 0f)
            cubicTo(w * 0.45f, h * 0.35f, w * 0.05f, h * 0.70f, w * 0.35f, h)
            lineTo(w * 0.55f, h)
            cubicTo(w * 0.25f, h * 0.70f, w * 0.65f, h * 0.35f, w * 0.35f, 0f)
            close()
        }
        drawPath(
            path = riverPath,
            brush = Brush.horizontalGradient(
                listOf(Color(0xFF4FC3F7), Color(0xFF29B6F6), Color(0xFF0288D1))
            )
        )
    }

    /**
     * Renders winding 3D cobblestone pathway connecting the lesson nodes.
     */
    fun DrawScope.render3dPath(
        nodes: List<RoadmapNode3D>,
        camera: Camera3D
    ) {
        if (nodes.size < 2) return

        val projectedPoints = nodes.map { node ->
            project(node.position, camera, size.width, size.height)
        }

        // Draw shadow path
        val shadowPath = Path()
        var started = false
        for (i in 0 until projectedPoints.size - 1) {
            val p1 = projectedPoints[i]
            val p2 = projectedPoints[i + 1]
            if (!p1.isVisible && !p2.isVisible) continue

            if (!started) {
                shadowPath.moveTo(p1.x, p1.y + 12f * p1.scale)
                started = true
            }

            val midX = (p1.x + p2.x) / 2f
            val midY = (p1.y + p2.y) / 2f + 12f * ((p1.scale + p2.scale) / 2f)
            shadowPath.quadraticTo(p1.x, p1.y + 12f * p1.scale, midX, midY)
        }
        drawPath(
            path = shadowPath,
            color = Color(0x33263238),
            style = Stroke(width = 24f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        )

        // Draw Main Cobblestone Trail
        val stonePath = Path()
        started = false
        for (i in 0 until projectedPoints.size - 1) {
            val p1 = projectedPoints[i]
            val p2 = projectedPoints[i + 1]
            if (!p1.isVisible && !p2.isVisible) continue

            if (!started) {
                stonePath.moveTo(p1.x, p1.y)
                started = true
            }

            val midX = (p1.x + p2.x) / 2f
            val midY = (p1.y + p2.y) / 2f
            stonePath.quadraticTo(p1.x, p1.y, midX, midY)
        }

        drawPath(
            path = stonePath,
            brush = Brush.verticalGradient(
                listOf(Color(0xFFD7CCC8), Color(0xFFBCAAA4), Color(0xFFA1887F))
            ),
            style = Stroke(width = 20f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        )

        // Stepping stone dashes
        drawPath(
            path = stonePath,
            color = Color(0xFF8D6E63),
            style = Stroke(
                width = 12f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(18f, 22f), 0f)
            )
        )
    }

    /**
     * Renders a 3D cylindrical lesson platform with bevel, drop shadow, and state-specific lighting.
     */
    fun DrawScope.render3dPlatform(
        node: RoadmapNode3D,
        projected: ProjectedPoint,
        timeSeconds: Float
    ) {
        if (!projected.isVisible) return

        val radius = 42f * projected.scale
        val height3d = 16f * projected.scale

        val centerX = projected.x
        val centerY = projected.y

        // Colors per node state
        val (topColor, sideColor, rimColor) = when (node.state) {
            NodeState3D.LOCKED -> Triple(
                Color(0xFFB0BEC5), // Slate Gray
                Color(0xFF78909C),
                Color(0xFFCFD8DC)
            )
            NodeState3D.AVAILABLE -> Triple(
                Color(0xFF42A5F5), // Sky Blue
                Color(0xFF1E88E5),
                Color(0xFF90CAF9)
            )
            NodeState3D.CURRENT -> Triple(
                Color(0xFFFFD54F), // Radiant Gold
                Color(0xFFFFA000),
                Color(0xFFFFF9C4)
            )
            NodeState3D.COMPLETED -> Triple(
                Color(0xFF66BB6A), // Emerald Green
                Color(0xFF388E3C),
                Color(0xFFA5D6A7)
            )
            NodeState3D.PERFECT -> Triple(
                Color(0xFFFFCA28), // Brilliant Amber
                Color(0xFFFF8F00),
                Color(0xFFFFECB3)
            )
        }

        // 1. Soft Ambient Ground Shadow
        drawOval(
            color = Color.Black.copy(alpha = 0.25f),
            topLeft = Offset(centerX - radius * 1.15f, centerY + height3d * 0.4f),
            size = Size(radius * 2.3f, radius * 0.9f)
        )

        // 2. 3D Cylinder Extruded Side Wall
        val sidePath = Path().apply {
            moveTo(centerX - radius, centerY)
            lineTo(centerX - radius, centerY + height3d)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    centerX - radius,
                    centerY + height3d - radius * 0.35f,
                    centerX + radius,
                    centerY + height3d + radius * 0.35f
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(centerX + radius, centerY)
            close()
        }
        drawPath(
            path = sidePath,
            brush = Brush.verticalGradient(
                listOf(rimColor, sideColor),
                startY = centerY,
                endY = centerY + height3d
            )
        )

        // 3. Top Platform Disk (Beveled 3D look)
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(rimColor, topColor),
                center = Offset(centerX, centerY - radius * 0.1f),
                radius = radius
            ),
            topLeft = Offset(centerX - radius, centerY - radius * 0.35f),
            size = Size(radius * 2f, radius * 0.7f)
        )

        // 4. Inner Ring Inscription
        drawOval(
            color = Color.White.copy(alpha = 0.45f),
            topLeft = Offset(centerX - radius * 0.75f, centerY - radius * 0.26f),
            size = Size(radius * 1.5f, radius * 0.52f),
            style = Stroke(width = 3f * projected.scale)
        )

        // 5. Pulsing Glow on Current / Unlocked platform
        if (node.state == NodeState3D.CURRENT || node.state == NodeState3D.AVAILABLE) {
            val pulse = (sin(timeSeconds * 4.0) * 0.15f + 0.85f).toFloat()
            drawOval(
                color = if (node.state == NodeState3D.CURRENT) Color(0xFFFFD54F).copy(alpha = 0.35f * pulse) else Color(0xFF64B5F6).copy(alpha = 0.30f * pulse),
                topLeft = Offset(centerX - radius * 1.25f * pulse, centerY - radius * 0.44f * pulse),
                size = Size(radius * 2.5f * pulse, radius * 0.88f * pulse),
                style = Stroke(width = 4f * projected.scale)
            )
        }
    }
}
