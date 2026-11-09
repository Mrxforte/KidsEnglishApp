package com.kidsenglish.app.feature.roadmap.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kidsenglish.app.core.designsystem.colors.AppColors
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions
import com.kidsenglish.app.core.designsystem.icons.AppIcons
import com.kidsenglish.app.core.designsystem.images.AppImages
import com.kidsenglish.app.core.designsystem.typography.KidsDisplayFontFamily
import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.domain.model.RoadmapActivityCard
import kotlin.math.cos
import kotlin.math.sin

/**
 * Full-featured interactive ABC Learning & Letter Tracing Game Modal.
 *
 * Features:
 * - Animated 3D Letter with Wizard Hat Mascot and Phonics Audio Speaker button.
 * - Interactive Finger Letter Tracing Canvas with guided paths and star particle strokes.
 * - Phonics Word Match Quiz ("Which picture starts with Letter X?").
 * - Victory Fanfare & Level Completion to unlock the next Alphabet Level!
 */
@Composable
fun AbcInteractiveGameModal(
    card: RoadmapActivityCard,
    onPlayAudioChime: () -> Unit,
    onCompleteAndUnlockNext: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val letterChar = card.letterChar ?: 'A'
    val phonicsWord = card.phonicsWord ?: "Apple"
    val phonicsEmoji = card.phonicsEmoji ?: "🍎"

    var selectedTab by remember { mutableStateOf(0) } // 0: Tracing, 1: Phonics Quiz
    var quizAnsweredCorrectly by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "abc_game_anim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "abc_game_pulse"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(AppDimensions.RadiusLarge),
            color = AppColors.Surface,
            shadowElevation = AppDimensions.ElevationHigh,
            modifier = modifier
                .widthIn(max = 660.dp)
                .padding(AppDimensions.SpaceXXS)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(AppDimensions.SpaceS)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Bar: Title + Speaker Audio Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = AppImages.LetterAWizard),
                            contentDescription = null,
                            modifier = Modifier
                                .size(42.dp)
                                .scale(pulseScale)
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.SpaceS))
                        Column {
                            Text(
                                text = "Letter $letterChar - $phonicsWord $phonicsEmoji",
                                style = MaterialTheme.typography.titleLarge,
                                color = AppColors.TextPrimary
                            )
                            Text(
                                text = "$letterChar says /${letterChar.lowercaseChar()}/ as in $phonicsWord",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppColors.TextSecondary
                            )
                        }
                    }

                    // Interactive Speaker Audio Button
                    IconButton(
                        onClick = onPlayAudioChime,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AppColors.PrimaryContainer)
                    ) {
                        Icon(
                            imageVector = AppIcons.SoundWave,
                            contentDescription = "Listen Phonics Sound",
                            tint = AppColors.Primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.SpaceXS))

                // Mode Tabs Switcher (Tracing vs Phonics Quiz)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.SpaceS),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { selectedTab = 0 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == 0) AppColors.Primary else AppColors.SurfaceVariant,
                            contentColor = if (selectedTab == 0) AppColors.OnPrimary else AppColors.TextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Text(text = "✏️ Finger Tracing")
                    }

                    Button(
                        onClick = { selectedTab = 1 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == 1) AppColors.Primary else AppColors.SurfaceVariant,
                            contentColor = if (selectedTab == 1) AppColors.OnPrimary else AppColors.TextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Text(text = "🧩 Phonics Quiz")
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.SpaceS))

                // Interactive Content Surface
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(AppDimensions.RadiusMedium))
                        .background(AppColors.SceneAbcChalkboard)
                ) {
                    if (selectedTab == 0) {
                        InteractiveTracingCanvas(
                            letterChar = letterChar,
                            phonicsEmoji = phonicsEmoji,
                            pulseScale = pulseScale,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        PhonicsWordQuizSection(
                            letterChar = letterChar,
                            targetWord = phonicsWord,
                            targetEmoji = phonicsEmoji,
                            onCorrectAnswer = {
                                quizAnsweredCorrectly = true
                                onPlayAudioChime()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.SpaceM))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.SpaceS),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(AppDimensions.ButtonHeight)
                    ) {
                        Text(text = stringResource(id = AppStrings.Roadmap.MODAL_CLOSE))
                    }

                    Button(
                        onClick = onCompleteAndUnlockNext,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Secondary,
                            contentColor = AppColors.OnSecondary
                        ),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(AppDimensions.ButtonHeight)
                    ) {
                        Icon(
                            imageVector = AppIcons.PlayAction,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.SpaceXXS))
                        Text(text = "Complete & Earn Sticker 🌟")
                    }
                }
            }
        }
    }
}

/**
 * Interactive Touch/Finger Tracing Canvas with guided letter paths and particle star trail.
 */
@Composable
private fun InteractiveTracingCanvas(
    letterChar: Char,
    phonicsEmoji: String,
    pulseScale: Float,
    modifier: Modifier = Modifier
) {
    val drawnPathPoints = remember { mutableStateListOf<Offset>() }
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> drawnPathPoints.add(offset) },
                    onDrag = { change, _ ->
                        drawnPathPoints.add(change.position)
                        change.consume()
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Guided Letter Outline (Dashed/Dotted)
            drawText(
                textMeasurer = textMeasurer,
                text = letterChar.toString(),
                topLeft = Offset(w * 0.38f, h * 0.15f),
                style = TextStyle(
                    fontFamily = KidsDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimensions.TextSizeDisplayLarge * 1.5f,
                    color = AppColors.CardProgressTrack
                )
            )

            // Draw User Finger Tracing Path with Star/Gold Stroke
            if (drawnPathPoints.size > 1) {
                val tracePath = Path().apply {
                    moveTo(drawnPathPoints.first().x, drawnPathPoints.first().y)
                    for (i in 1 until drawnPathPoints.size) {
                        lineTo(drawnPathPoints[i].x, drawnPathPoints[i].y)
                    }
                }
                drawPath(
                    path = tracePath,
                    color = AppColors.SplashSparkleGold,
                    style = Stroke(
                        width = 18f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Glowing star particles along traced points
                drawnPathPoints.takeLast(6).forEach { pt ->
                    drawCircle(
                        color = AppColors.CardWhiteFrame,
                        radius = 8f,
                        center = pt
                    )
                }
            }

            // Big Phonics Emoji Accent on right
            drawText(
                textMeasurer = textMeasurer,
                text = phonicsEmoji,
                topLeft = Offset(w * 0.76f, h * 0.28f),
                style = TextStyle(
                    fontSize = AppDimensions.TextSizeDisplayLarge
                )
            )
        }

        // Floating Instruction & Clear Button
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.SpaceS)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = if (drawnPathPoints.isNotEmpty()) "Awesome Tracing! 🌟" else "Trace Letter '$letterChar' with your finger 👆",
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.CardWhiteFrame
            )

            if (drawnPathPoints.isNotEmpty()) {
                Button(
                    onClick = { drawnPathPoints.clear() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.HudHexBlueFill,
                        contentColor = AppColors.CardWhiteFrame
                    ),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(text = "Clear")
                }
            }
        }
    }
}

/**
 * Phonics Word Matching Quiz component ("Which picture starts with Letter X?").
 */
@Composable
private fun PhonicsWordQuizSection(
    letterChar: Char,
    targetWord: String,
    targetEmoji: String,
    onCorrectAnswer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val choices = remember(letterChar) {
        listOf(
            Triple(targetWord, targetEmoji, true),
            Triple("Donut", "🍩", false),
            Triple("Car", "🚗", false)
        ).shuffled()
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(AppDimensions.SpaceM)
    ) {
        Text(
            text = "Which picture starts with Letter '$letterChar'?",
            style = MaterialTheme.typography.titleLarge,
            color = AppColors.CardWhiteFrame,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppDimensions.SpaceM))

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.SpaceM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            choices.forEachIndexed { index, (word, emoji, isCorrect) ->
                val isSelected = selectedIndex == index
                val cardColor = when {
                    isSelected && isCorrect -> AppColors.Success
                    isSelected && !isCorrect -> AppColors.Error
                    else -> AppColors.Surface
                }

                Surface(
                    shape = RoundedCornerShape(AppDimensions.RadiusMedium),
                    color = cardColor,
                    shadowElevation = AppDimensions.ElevationMedium,
                    modifier = Modifier
                        .size(width = 130.dp, height = 120.dp)
                        .clickable {
                            selectedIndex = index
                            if (isCorrect) onCorrectAnswer()
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(AppDimensions.SpaceXS)
                    ) {
                        Text(
                            text = emoji,
                            style = TextStyle(fontSize = AppDimensions.TextSizeDisplayMedium)
                        )
                        Spacer(modifier = Modifier.height(AppDimensions.SpaceXXS))
                        Text(
                            text = word,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isSelected) AppColors.OnPrimary else AppColors.TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.SpaceS))

        if (selectedIndex != null) {
            val isWin = choices[selectedIndex!!].third
            Text(
                text = if (isWin) "Correct! 🎉 $targetWord starts with '$letterChar'!" else "Try again! Find the item starting with '$letterChar'",
                style = MaterialTheme.typography.titleMedium,
                color = if (isWin) AppColors.SplashSparkleGold else AppColors.PadlockGoldBody,
                textAlign = TextAlign.Center
            )
        }
    }
}
