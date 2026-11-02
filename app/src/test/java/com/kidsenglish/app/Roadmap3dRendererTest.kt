package com.kidsenglish.app

import com.kidsenglish.app.core.roadmap3d.Roadmap3dRenderer
import com.kidsenglish.app.domain.model.Camera3D
import com.kidsenglish.app.domain.model.Vector3D
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Roadmap3dRendererTest {

    @Test
    fun `3D projection projects centered point to center of screen`() {
        val camera = Camera3D(x = 0f, y = 0f, z = -500f, pitch = 0f, fov = 500f)
        val worldPoint = Vector3D(x = 0f, y = 0f, z = 0f)

        val projected = Roadmap3dRenderer.project(
            worldPos = worldPoint,
            camera = camera,
            screenWidth = 400f,
            screenHeight = 800f
        )

        assertTrue(projected.isVisible)
        assertEquals(200f, projected.x, 0.1f)
        assertEquals(400f, projected.y, 0.1f)
        assertEquals(1.0f, projected.scale, 0.05f)
    }

    @Test
    fun `point behind camera is clipped`() {
        val camera = Camera3D(x = 0f, y = 0f, z = 100f, pitch = 0f, fov = 500f)
        val worldPoint = Vector3D(x = 0f, y = 0f, z = 0f) // Behind camera (dz = -100)

        val projected = Roadmap3dRenderer.project(
            worldPos = worldPoint,
            camera = camera,
            screenWidth = 400f,
            screenHeight = 800f
        )

        assertFalse(projected.isVisible)
    }

    @Test
    fun `distant points scale down due to perspective`() {
        val camera = Camera3D(x = 0f, y = 0f, z = -500f, pitch = 0f, fov = 500f)
        val nearPoint = Vector3D(x = 50f, y = 0f, z = 0f)
        val farPoint = Vector3D(x = 50f, y = 0f, z = 500f)

        val nearProj = Roadmap3dRenderer.project(nearPoint, camera, 400f, 800f)
        val farProj = Roadmap3dRenderer.project(farPoint, camera, 400f, 800f)

        assertTrue(nearProj.scale > farProj.scale)
    }
}
