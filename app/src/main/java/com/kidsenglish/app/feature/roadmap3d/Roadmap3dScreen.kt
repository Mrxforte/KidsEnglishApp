package com.kidsenglish.app.feature.roadmap3d

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kidsenglish.app.R
import com.kidsenglish.app.core.audio.AudioEffect
import com.kidsenglish.app.core.audio.AudioManager
import com.kidsenglish.app.core.character.FoxMascot3d
import com.kidsenglish.app.core.character.FoxState
import com.kidsenglish.app.core.designsystem.color.AppColors
import com.kidsenglish.app.core.designsystem.dimens.Dimens
import com.kidsenglish.app.core.roadmap3d.Roadmap3dRenderer
import com.kidsenglish.app.core.roadmap3d.Roadmap3dRenderer.render3dPath
import com.kidsenglish.app.core.roadmap3d.Roadmap3dRenderer.render3dPlatform
import com.kidsenglish.app.core.roadmap3d.Roadmap3dRenderer.renderGreenValleyEnvironment
import com.kidsenglish.app.domain.model.Camera3D
import com.kidsenglish.app.domain.model.NodeState3D
import com.kidsenglish.app.domain.model.RoadmapNode3D
import com.kidsenglish.app.domain.model.Vector3D
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Full 3D Duolingo-inspired adventure learning roadmap featuring Green Valley, winding vertical 3D path,
 * smooth camera tracking, Pixar-style lighting, and all 26 ABC letter levels.
 */
@Composable
fun Roadmap3dScreen(
    audioManager: AudioManager,
    totalStars: Int,
    streakDays: Int,
    currentLevelNumber: Int,
    onNodeClick: (RoadmapNode3D) -> Unit,
    onSettingsClick: () -> Unit,
    onLegacyPathClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    // 1. Data-driven 3D Nodes for all 26 ABC levels (A..Z)
    val nodes = remember(currentLevelNumber) {
        createAbcGreenValleyNodes(currentLevelNumber)
    }

    // 2. Camera Y coordinate (Vertical scroll position)
    val initialCameraY = remember(currentLevelNumber) {
        val currentNode = nodes.find { it.lessonNumber == currentLevelNumber } ?: nodes.first()
        currentNode.position.y - 120f
    }

    val cameraYAnim = remember { Animatable(initialCameraY) }

    // Start background music
    LaunchedEffect(Unit) {
        audioManager.startMusic()
    }

    // Auto-scroll camera to current lesson smoothly on start or level change
    LaunchedEffect(currentLevelNumber) {
        val targetNode = nodes.find { it.lessonNumber == currentLevelNumber } ?: nodes.first()
        cameraYAnim.animateTo(
            targetValue = targetNode.position.y - 120f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    // 3. Fox Mascot State & Position
    var foxTargetNodeId by remember { mutableStateOf(nodes.find { it.state == NodeState3D.CURRENT }?.id ?: nodes.first().id) }
    var foxAnimationState by remember { mutableStateOf(FoxState.IDLE) }

    val infiniteTransition = rememberInfiniteTransition(label = "worldTime")
    val timeSeconds by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "timeSec"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch {
                        val newY = (cameraYAnim.value + dragAmount.y * 1.5f).coerceIn(-4400f, 400f)
                        cameraYAnim.snapTo(newY)
                    }
                }
            }
    ) {
        val screenW = constraints.maxWidth.toFloat()
        val screenH = constraints.maxHeight.toFloat()

        val activeCamera = Camera3D(
            x = 0f,
            y = cameraYAnim.value,
            z = -650f,
            pitch = 24f,
            fov = 580f
        )

        val projectedNodes = remember(activeCamera, nodes) {
            nodes.map { node ->
                node to Roadmap3dRenderer.project(node.position, activeCamera, screenW, screenH)
            }
        }

        // ==========================================
        // 1. Full 3D Canvas Rendering Layer
        // ==========================================
        Canvas(modifier = Modifier.fillMaxSize()) {
            renderGreenValleyEnvironment(activeCamera, timeSeconds)
            render3dPath(nodes, activeCamera)

            projectedNodes
                .sortedByDescending { it.second.depthZ }
                .forEach { (node, projected) ->
                    render3dPlatform(node, projected, timeSeconds)
                }
        }

        // ==========================================
        // 2. Interactive Overlay Layer
        // ==========================================
        projectedNodes.forEach { (node, proj) ->
            if (proj.isVisible) {
                val nodeSizePx = (80f * proj.scale).coerceIn(44f, 120f)
                val isSelectedFoxNode = (node.id == foxTargetNodeId)

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (proj.x - nodeSizePx / 2f).roundToInt(),
                                y = (proj.y - nodeSizePx / 2f).roundToInt()
                            )
                        }
                        .size((nodeSizePx / screenW * constraints.maxWidth).dp)
                        .testTag("3d_node_${node.lessonNumber}")
                        .clickable(enabled = node.state != NodeState3D.LOCKED) {
                            foxTargetNodeId = node.id
                            foxAnimationState = FoxState.JUMP
                            audioManager.playSfx(AudioEffect.TAP)

                            coroutineScope.launch {
                                cameraYAnim.animateTo(
                                    targetValue = node.position.y - 120f,
                                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                                )
                                onNodeClick(node)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (node.state == NodeState3D.LOCKED) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.roadmap_locked_label),
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = node.emoji,
                                fontSize = (20 * proj.scale).coerceIn(14f, 28f).sp
                            )

                            if (node.starsEarned > 0) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(node.starsEarned) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = AppColors.StarGold,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Title label
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (proj.x - 70f).roundToInt(),
                                y = (proj.y + nodeSizePx * 0.45f).roundToInt()
                            )
                        }
                        .width(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = node.titleUz,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Fox Mascot on selected node
                if (isSelectedFoxNode) {
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (proj.x - 32f * proj.scale).roundToInt(),
                                    y = (proj.y - 68f * proj.scale).roundToInt()
                                )
                            }
                    ) {
                        FoxMascot3d(
                            state = foxAnimationState,
                            size = (64 * proj.scale).coerceIn(40f, 90f).dp,
                            onClick = {
                                foxAnimationState = FoxState.GREET
                                audioManager.playSfx(AudioEffect.CHEER)
                            }
                        )
                    }
                }
            }
        }

        // ==========================================
        // 3. Top Navigation & Stats Bar
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceS)
                .zIndex(20f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(Dimens.RadiusPill),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = Dimens.ElevationMedium,
                modifier = Modifier.clickable { onLegacyPathClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = Dimens.SpaceM, vertical = 8.dp)
                ) {
                    Text(text = "🌿", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "ABC Sayohati",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            text = "26 ta Harf • A-Z",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppColors.TextSecondary
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
            ) {
                Surface(
                    shape = RoundedCornerShape(Dimens.RadiusPill),
                    color = AppColors.StarGold,
                    shadowElevation = Dimens.ElevationLow
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Stars",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$totalStars",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(Dimens.RadiusPill),
                    color = AppColors.StreakFlame,
                    shadowElevation = Dimens.ElevationLow
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "Streak",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$streakDays",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                IconButton(
                    onClick = {
                        audioManager.playSfx(AudioEffect.CLICK)
                        onSettingsClick()
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(alpha = 0.95f), CircleShape)
                        .testTag("settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.action_settings),
                        tint = AppColors.PrimaryNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Generates all 26 3D nodes (A through Z) winding up Green Valley.
 */
private fun createAbcGreenValleyNodes(currentLevel: Int): List<RoadmapNode3D> {
    val letters = listOf(
        "A" to "🍎", "B" to "🏐", "C" to "🐱", "D" to "🐶", "E" to "🥚",
        "F" to "🐟", "G" to "🐐", "H" to "🎩", "I" to "🧊", "J" to "🍓",
        "K" to "🔑", "L" to "🦁", "M" to "🌙", "N" to "👃", "O" to "🍊",
        "P" to "🐷", "Q" to "👑", "R" to "🐰", "S" to "☀️", "T" to "🐅",
        "U" to "☂️", "V" to "🚐", "W" to "💧", "X" to "📦", "Y" to "🟡", "Z" to "🦓"
    )

    return letters.mapIndexed { index, (letter, emoji) ->
        val levelNum = index + 1
        // Winding sine wave positions from bottom (Y=260) to top (Y=-4200)
        val xPos = (if (index % 2 == 0) -120f else 120f) + (index % 3) * 10f
        val yPos = 260f - (index * 170f)
        val zPos = index * 12f

        val state = when {
            levelNum < currentLevel -> NodeState3D.PERFECT
            levelNum == currentLevel -> NodeState3D.CURRENT
            levelNum == currentLevel + 1 -> NodeState3D.AVAILABLE
            else -> NodeState3D.LOCKED
        }

        RoadmapNode3D(
            id = "abc_${letter.lowercase()}",
            worldId = "green_valley",
            lessonNumber = levelNum,
            title = "$letter — Lesson",
            titleUz = "$letter — Harfi",
            subtitle = letter,
            emoji = emoji,
            position = Vector3D(x = xPos, y = yPos, z = zPos),
            state = state,
            starsEarned = if (levelNum < currentLevel) 3 else 0
        )
    }
}
