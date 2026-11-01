package com.kidsenglish.app.ui.roadmap

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import com.kidsenglish.app.core.audio.rememberSoundManager
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kidsenglish.app.domain.model.NodeState
import com.kidsenglish.app.domain.model.RoadmapData
import com.kidsenglish.app.domain.model.RoadmapNode
import com.kidsenglish.app.domain.model.RoadmapTopic
import com.kidsenglish.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.sin

/**
 * Main Roadmap Screen – beautiful vertical cartoon nature journey.
 * Winding path, large colorful nodes, animated current indicator.
 */
@Composable
fun RoadmapScreen(
    roadmapData: RoadmapData,
    onNodeClick: (RoadmapNode) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val soundManager = rememberSoundManager()

    // Start cheerful background music when roadmap appears
    LaunchedEffect(Unit) {
        soundManager.playBackgroundMusic()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SoftSky,
                        Color(0xFFE1F5FE),
                        SoftMint,
                        Color(0xFFC8E6C9)
                    )
                )
            )
    ) {
        // Decorative floating elements (clouds, birds, butterflies)
        FloatingDecorations()

        // Roadmap grows from bottom → top (first lessons at the bottom)
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = 24.dp, bottom = 140.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            reverseLayout = false, // we reverse content order manually for clearer control
            modifier = Modifier.fillMaxSize()
        ) {
            // Header stays at the very top
            item {
                RoadmapHeader(
                    totalStars = roadmapData.totalStars,
                    streakDays = roadmapData.streakDays
                )
            }

            // Topics in normal order, but nodes inside each topic are reversed
            // so the first lesson appears lower on the screen and path goes upward
            roadmapData.topics.forEachIndexed { topicIndex, topic ->
                item {
                    TopicSectionHeader(topic = topic)
                }

                // Reverse nodes so lesson 1 is at the bottom of the topic section
                val reversedNodes = topic.nodes.reversed()
                itemsIndexed(reversedNodes) { index, node ->
                    val originalIndex = topic.nodes.lastIndex - index
                    val isLeftSide = originalIndex % 2 == 0

                    // Because list is reversed, "above" and "below" are swapped visually
                    val isFirstInTopic = originalIndex == 0          // bottom-most node
                    val isLastInTopic = originalIndex == topic.nodes.lastIndex

                    RoadmapNodeItem(
                        node = node,
                        isLeftSide = isLeftSide,
                        showPathAbove = !isLastInTopic,   // path continues upward
                        showPathBelow = !isFirstInTopic,  // path comes from below
                        onClick = {
                            soundManager.playTapSound()
                            if (node.state == NodeState.AVAILABLE) {
                                soundManager.playUnlockSound()
                            }
                            onNodeClick(node)
                        }
                    )
                }
            }

            // Future content teaser at the very top of the path (after last topic)
            item {
                EndOfJourneyCard()
            }
        }
    }
}

@Composable
private fun RoadmapHeader(
    totalStars: Int,
    streakDays: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🌟 Mening yo‘lim",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )
        Text(
            text = "Ingliz tilini o‘rganish sarguzashti",
            fontSize = 15.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatChip(
                emoji = "⭐",
                value = "$totalStars",
                label = "Yulduzlar"
            )
            StatChip(
                emoji = "🔥",
                value = "$streakDays",
                label = "Kunlik"
            )
        }
    }
}

@Composable
private fun StatChip(
    emoji: String,
    value: String,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.85f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Column {
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun TopicSectionHeader(topic: RoadmapTopic) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.92f),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Topic emoji badge
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(SunYellow, AccentOrange)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = topic.emoji, fontSize = 28.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mavzu ${topic.number}",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = topic.titleUz,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Text(
                            text = topic.title,
                            fontSize = 14.sp,
                            color = AccentOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = topic.descriptionUz,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Progress bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { topic.completionPercent },
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = NodeCompleted,
                        trackColor = Color(0xFFE0E0E0)
                    )
                    Text(
                        text = "${(topic.completionPercent * 100).toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NodeCompleted
                    )
                }

                Text(
                    text = "${topic.completedLessons} / ${topic.totalLessons} dars tugallandi",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun RoadmapNodeItem(
    node: RoadmapNode,
    isLeftSide: Boolean,
    showPathAbove: Boolean,
    showPathBelow: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "nodePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val nodeScale = if (node.isCurrent || node.state == NodeState.CURRENT) pulseScale else 1f

    // Smooth multi-axis walk / idle animation for the cartoon kid
    val walkBounceY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "walkBounceY"
    )
    val walkScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "walkScale"
    )
    val walkTilt by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "walkTilt"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        // Soft, mostly-straight path with only gentle zigzag
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val centerX = size.width / 2
            // Much gentler offset – only slight left/right movement
            val nodeX = if (isLeftSide) size.width * 0.38f else size.width * 0.62f
            val midY = size.height / 2

            val pathColor = when {
                node.state == NodeState.COMPLETED || node.state == NodeState.CURRENT -> NodeCompleted
                node.state == NodeState.AVAILABLE -> NodeAvailable.copy(alpha = 0.65f)
                else -> NodeLocked.copy(alpha = 0.4f)
            }

            // Soft path from above
            if (showPathAbove) {
                val path = Path().apply {
                    moveTo(centerX, 0f)
                    // Gentle curve instead of sharp zigzag
                    cubicTo(
                        centerX, size.height * 0.35f,
                        nodeX, size.height * 0.35f,
                        nodeX, midY - 42.dp.toPx()
                    )
                }
                // Glow
                drawPath(
                    path = path,
                    color = pathColor.copy(alpha = 0.22f),
                    style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round)
                )
                // Main path
                drawPath(
                    path = path,
                    color = pathColor,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Soft path continuing down
            if (showPathBelow) {
                val nextIsLeft = !isLeftSide
                val nextX = if (nextIsLeft) size.width * 0.38f else size.width * 0.62f
                val path = Path().apply {
                    moveTo(nodeX, midY + 42.dp.toPx())
                    cubicTo(
                        nodeX, size.height * 0.7f,
                        nextX, size.height * 0.7f,
                        nextX, size.height
                    )
                }
                drawPath(
                    path = path,
                    color = pathColor.copy(alpha = 0.22f),
                    style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round)
                )
                drawPath(
                    path = path,
                    color = pathColor,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        // The actual node
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            contentAlignment = if (isLeftSide) Alignment.CenterStart else Alignment.CenterEnd
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Cute cartoon kid with smooth walk / idle bounce + tilt
                // TODO: Replace emoji with custom Lottie or animated vector later
                if (node.isCurrent || node.state == NodeState.CURRENT) {
                    Text(
                        text = "🧒",
                        fontSize = 52.sp,
                        modifier = Modifier
                            .offset(y = (-walkBounceY).dp)
                            .scale(walkScale)
                            .rotate(walkTilt)
                            .padding(bottom = 2.dp)
                    )
                }

                NodeCard(
                    node = node,
                    scale = nodeScale,
                    onClick = onClick,
                    showUnlockAnimation = node.state == NodeState.AVAILABLE
                )
            }
        }
    }
}

@Composable
private fun NodeCard(
    node: RoadmapNode,
    scale: Float,
    onClick: () -> Unit,
    showUnlockAnimation: Boolean = false
) {
    val backgroundColor = when (node.state) {
        NodeState.COMPLETED -> NodeCompleted
        NodeState.CURRENT -> NodeCurrent
        NodeState.AVAILABLE -> NodeAvailable
        NodeState.LOCKED -> NodeLocked
    }

    val canClick = node.state != NodeState.LOCKED

    // Unlock animation values
    val infiniteTransition = rememberInfiniteTransition(label = "unlock")
    val starSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "starSpin"
    )
    val sparkleScale by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                enabled = canClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Unlock celebration layer (stars + sparkles)
            if (showUnlockAnimation || node.state == NodeState.AVAILABLE) {
                // Rotating stars around the node
                Text(
                    text = "⭐",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .offset(x = 40.dp, y = (-30).dp)
                        .rotate(starSpin)
                        .scale(sparkleScale)
                )
                Text(
                    text = "✨",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .offset(x = (-38).dp, y = (-28).dp)
                        .scale(sparkleScale)
                )
                Text(
                    text = "⭐",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .offset(x = 36.dp, y = 32.dp)
                        .rotate(-starSpin)
                )
            }

            // Main circular node
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(88.dp)
                    .shadow(
                        elevation = if (node.isCurrent) 12.dp else 6.dp,
                        shape = CircleShape,
                        ambientColor = backgroundColor.copy(alpha = 0.5f),
                        spotColor = backgroundColor.copy(alpha = 0.5f)
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                backgroundColor.copy(alpha = 0.95f),
                                backgroundColor
                            )
                        )
                    )
                    .border(
                        width = 4.dp,
                        color = Color.White.copy(alpha = 0.9f),
                        shape = CircleShape
                    )
            ) {
                when (node.state) {
                    NodeState.LOCKED -> {
                        // Clear lock icon for uncompleted / locked levels
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Yopiq dars",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    NodeState.CURRENT -> {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Hozirgi dars",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    NodeState.AVAILABLE -> {
                        // Freshly unlocked – show the emoji with a small glow feeling
                        Text(
                            text = node.iconEmoji,
                            fontSize = 36.sp
                        )
                    }
                    else -> {
                        Text(
                            text = node.iconEmoji,
                            fontSize = 36.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 3.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = node.titleUz,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = node.title,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                // Stars
                if (node.state == NodeState.COMPLETED || node.starsEarned > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(node.maxStars) { index ->
                            Icon(
                                imageVector = if (index < node.starsEarned)
                                    Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (index < node.starsEarned) StarGold else StarEmpty,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EndOfJourneyCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🏔️", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Keyingi sarguzashtlar tez orada!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Yangi mavzular qo‘shiladi…",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun FloatingDecorations() {
    val infiniteTransition = rememberInfiniteTransition(label = "float")

    val cloudOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud1"
    )

    val cloudOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -14f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud2"
    )

    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    val slowFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "slowFloat"
    )

    // Soft clouds
    Text(
        text = "☁️",
        fontSize = 44.sp,
        modifier = Modifier.offset(x = 28.dp, y = (22 + cloudOffset1).dp)
    )
    Text(
        text = "☁️",
        fontSize = 34.sp,
        modifier = Modifier.offset(x = 290.dp, y = (70 + cloudOffset2).dp)
    )
    Text(
        text = "☁️",
        fontSize = 26.sp,
        modifier = Modifier.offset(x = 170.dp, y = (16 + cloudOffset1 * 0.6f).dp)
    )

    // Pixar-style friendly nature characters
    Text(
        text = "🦋",
        fontSize = 26.sp,
        modifier = Modifier
            .offset(x = 48.dp, y = (160 + floatY).dp)
            .rotate(floatY * 1.8f)
    )
    Text(
        text = "🐦",
        fontSize = 24.sp,
        modifier = Modifier.offset(x = 310.dp, y = (195 - floatY).dp)
    )
    Text(
        text = "🌸",
        fontSize = 22.sp,
        modifier = Modifier.offset(x = 20.dp, y = (320 + slowFloat).dp)
    )
    Text(
        text = "🌳",
        fontSize = 36.sp,
        modifier = Modifier.offset(x = 330.dp, y = (380 + slowFloat * 0.5f).dp)
    )
    Text(
        text = "🦊",
        fontSize = 28.sp,
        modifier = Modifier.offset(x = 30.dp, y = (480 - slowFloat).dp)
    )
    Text(
        text = "🌼",
        fontSize = 20.sp,
        modifier = Modifier.offset(x = 300.dp, y = (520 + floatY).dp)
    )
    Text(
        text = "🐿️",
        fontSize = 24.sp,
        modifier = Modifier.offset(x = 50.dp, y = (600 + slowFloat).dp)
    )
    Text(
        text = "🌿",
        fontSize = 22.sp,
        modifier = Modifier.offset(x = 320.dp, y = (650 - floatY).dp)
    )
}

// ─────────────────────────────────────────────────────────────
// Sample data for preview & development
// ─────────────────────────────────────────────────────────────

fun createSampleRoadmapData(): RoadmapData {
    val abcNodes = listOf(
        RoadmapNode(
            id = "abc_a",
            lessonNumber = 1,
            title = "A — Apple",
            titleUz = "A — Olma",
            topicId = "funny_abc",
            state = NodeState.COMPLETED,
            starsEarned = 3,
            iconEmoji = "🍎"
        ),
        RoadmapNode(
            id = "abc_b",
            lessonNumber = 2,
            title = "B — Ball",
            titleUz = "B — To‘p",
            topicId = "funny_abc",
            state = NodeState.COMPLETED,
            starsEarned = 2,
            iconEmoji = "⚽"
        ),
        RoadmapNode(
            id = "abc_c",
            lessonNumber = 3,
            title = "C — Cat",
            titleUz = "C — Mushuk",
            topicId = "funny_abc",
            state = NodeState.CURRENT,
            starsEarned = 0,
            isCurrent = true,
            iconEmoji = "🐱"
        ),
        RoadmapNode(
            id = "abc_d",
            lessonNumber = 4,
            title = "D — Dog",
            titleUz = "D — It",
            topicId = "funny_abc",
            state = NodeState.AVAILABLE,
            iconEmoji = "🐶"
        ),
        RoadmapNode(
            id = "abc_e",
            lessonNumber = 5,
            title = "E — Egg",
            titleUz = "E — Tuxum",
            topicId = "funny_abc",
            state = NodeState.LOCKED,
            iconEmoji = "🥚"
        ),
        RoadmapNode(
            id = "abc_f",
            lessonNumber = 6,
            title = "F — Fish",
            titleUz = "F — Baliq",
            topicId = "funny_abc",
            state = NodeState.LOCKED,
            iconEmoji = "🐟"
        )
    )

    val numbersNodes = listOf(
        RoadmapNode(
            id = "num_1",
            lessonNumber = 1,
            title = "One",
            titleUz = "Bir",
            topicId = "happy_numbers",
            state = NodeState.LOCKED,
            iconEmoji = "1️⃣"
        ),
        RoadmapNode(
            id = "num_2",
            lessonNumber = 2,
            title = "Two",
            titleUz = "Ikki",
            topicId = "happy_numbers",
            state = NodeState.LOCKED,
            iconEmoji = "2️⃣"
        )
    )

    return RoadmapData(
        topics = listOf(
            RoadmapTopic(
                id = "funny_abc",
                number = 1,
                title = "Funny ABC",
                titleUz = "Qiziqarli ABC",
                descriptionUz = "Harflarni o‘rganamiz! Har bir harf uchun rasm, ovoz va o‘yinlar bor.",
                emoji = "🔤",
                totalLessons = 26,
                completedLessons = 2,
                nodes = abcNodes,
                isUnlocked = true
            ),
            RoadmapTopic(
                id = "happy_numbers",
                number = 2,
                title = "Happy Numbers",
                titleUz = "Baxtli Sonlar",
                descriptionUz = "Sonlarni hisoblash, sanash va o‘ynash orqali o‘rganamiz.",
                emoji = "🔢",
                totalLessons = 26,
                completedLessons = 0,
                nodes = numbersNodes,
                isUnlocked = false
            )
        ),
        currentNodeId = "abc_c",
        totalStars = 5,
        streakDays = 3
    )
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900)
@Composable
fun RoadmapScreenPreview() {
    MaterialTheme {
        RoadmapScreen(
            roadmapData = createSampleRoadmapData(),
            onNodeClick = {}
        )
    }
}
