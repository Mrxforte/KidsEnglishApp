package com.kidsenglish.app.feature.lesson

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kidsenglish.app.R
import com.kidsenglish.app.core.animation.CharacterState
import com.kidsenglish.app.core.animation.ConfettiOverlay
import com.kidsenglish.app.core.animation.MascotCharacter
import com.kidsenglish.app.core.animation.wobble
import com.kidsenglish.app.core.audio.AudioEffect
import com.kidsenglish.app.core.audio.AudioManager
import com.kidsenglish.app.core.designsystem.color.AppColors
import com.kidsenglish.app.core.designsystem.dimens.Dimens
import com.kidsenglish.app.core.designsystem.image.AppAsyncImage
import com.kidsenglish.app.core.tts.TtsPlaybackController
import com.kidsenglish.app.core.tts.rememberTtsPlaybackController
import com.kidsenglish.app.domain.model.Exercise
import com.kidsenglish.app.domain.model.ExerciseOption
import com.kidsenglish.app.domain.model.Lesson
import com.kidsenglish.app.domain.model.VocabularyItem
import com.kidsenglish.app.lesson_engine.EngineLessonSection
import com.kidsenglish.app.lesson_engine.ExerciseAnswerFeedback
import com.kidsenglish.app.lesson_engine.LessonEngine

/**
 * Reusable, production-grade interactive Lesson Player supporting TTS word pronunciation,
 * mascot reactions, audio ducking, and exercise evaluation.
 */
@Composable
fun LessonPlayerScreen(
    lesson: Lesson,
    audioManager: AudioManager,
    onBack: () -> Unit,
    onLessonCompleted: (stars: Int, correct: Int, total: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val engine = remember(lesson) { LessonEngine(lesson) }
    val engineState by engine.state.collectAsState()
    val ttsController = rememberTtsPlaybackController()

    // Duck background music while inside lesson for clear educational speech
    LaunchedEffect(Unit) {
        audioManager.duckForVoice()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColors.SoftSky, AppColors.SoftMint, Color(0xFFF1F8E9))
                )
            )
    ) {
        // Top Bar
        Surface(
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = Dimens.ElevationLow
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceS)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            audioManager.restoreAfterVoice()
                            onBack()
                        },
                        modifier = Modifier.testTag("lesson_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = AppColors.TextPrimary
                        )
                    }
                    Text(
                        text = lesson.titleUz,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                LinearProgressIndicator(
                    progress = { engineState.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(Dimens.RadiusS)),
                    color = AppColors.SuccessGreen,
                    trackColor = Color(0xFFECEFF1)
                )
            }
        }

        // Animated Content Sections
        AnimatedContent(
            targetState = engineState.currentSection,
            transitionSpec = {
                fadeIn(tween(300)).togetherWith(fadeOut(tween(200)))
            },
            modifier = Modifier.weight(1f),
            label = "lessonSection"
        ) { section ->
            when (section) {
                EngineLessonSection.WELCOME -> {
                    WelcomeSection(
                        lesson = lesson,
                        onContinue = {
                            audioManager.playSfx(AudioEffect.CLICK)
                            engine.moveToNextSection()
                        }
                    )
                }
                EngineLessonSection.DISCOVER -> {
                    DiscoverSection(
                        lesson = lesson,
                        ttsController = ttsController,
                        onContinue = {
                            audioManager.playSfx(AudioEffect.CLICK)
                            engine.moveToNextSection()
                        }
                    )
                }
                EngineLessonSection.UNDERSTAND -> {
                    UnderstandSection(
                        lesson = lesson,
                        ttsController = ttsController,
                        onContinue = {
                            audioManager.playSfx(AudioEffect.CLICK)
                            engine.moveToNextSection()
                        }
                    )
                }
                EngineLessonSection.PRACTICE -> {
                    PracticeSection(
                        lesson = lesson,
                        ttsController = ttsController,
                        feedback = engineState.feedback,
                        onAnswer = { exercise, option ->
                            val isCorrect = option.isCorrect
                            if (isCorrect) {
                                audioManager.playSfx(AudioEffect.CORRECT)
                            } else {
                                audioManager.playSfx(AudioEffect.WRONG)
                            }
                            engine.submitAnswer(exercise, option.id, isCorrect)
                        },
                        onContinue = {
                            audioManager.playSfx(AudioEffect.CLICK)
                            engine.clearFeedback()
                            engine.moveToNextSection()
                        }
                    )
                }
                EngineLessonSection.MINI_GAME -> {
                    MiniGameSection(
                        lesson = lesson,
                        audioManager = audioManager,
                        onContinue = {
                            audioManager.playSfx(AudioEffect.CLICK)
                            engine.moveToNextSection()
                        }
                    )
                }
                EngineLessonSection.REVIEW -> {
                    ReviewSection(
                        lesson = lesson,
                        ttsController = ttsController,
                        onContinue = {
                            audioManager.playSfx(AudioEffect.CLICK)
                            engine.moveToNextSection()
                        }
                    )
                }
                EngineLessonSection.COMPLETION -> {
                    CompletionSection(
                        lesson = lesson,
                        stars = engineState.starsEarned,
                        correctCount = engineState.correctAnswersCount,
                        totalCount = engineState.totalAnsweredCount,
                        audioManager = audioManager,
                        onFinish = {
                            audioManager.restoreAfterVoice()
                            onLessonCompleted(
                                engineState.starsEarned,
                                engineState.correctAnswersCount,
                                engineState.totalAnsweredCount
                            )
                        }
                    )
                }
            }
        }
    }
}

// ────────────────────────────────────────────────
// SECTION 1 — Welcome
// ────────────────────────────────────────────────
@Composable
private fun WelcomeSection(
    lesson: Lesson,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MascotCharacter(
            state = CharacterState.GREETING,
            size = Dimens.MascotSizeLarge,
            speechText = "Salom! 👋"
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceL))

        Text(
            text = lesson.titleUz,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppColors.TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceS))

        Text(
            text = lesson.descriptionUz,
            fontSize = 16.sp,
            color = AppColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Dimens.SpaceM)
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXL))

        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(Dimens.RadiusPill),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimarySky),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(Dimens.ButtonHeight)
                .testTag("welcome_continue_button")
        ) {
            Text(
                text = stringResource(R.string.action_continue),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// ────────────────────────────────────────────────
// SECTION 2 — Discover (TTS Vocabulary)
// ────────────────────────────────────────────────
@Composable
private fun DiscoverSection(
    lesson: Lesson,
    ttsController: TtsPlaybackController,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.SpaceL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Yangi so‘zlar bilan tanishamiz! 📖",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceM))

        lesson.vocabulary.forEach { item ->
            VocabularyCardWithTts(item = item, ttsController = ttsController)
            Spacer(modifier = Modifier.height(Dimens.SpaceM))
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceL))

        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(Dimens.RadiusPill),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.SuccessGreen),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(Dimens.ButtonHeight)
                .testTag("discover_continue_button")
        ) {
            Text(
                text = stringResource(R.string.action_continue),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun VocabularyCardWithTts(
    item: VocabularyItem,
    ttsController: TtsPlaybackController
) {
    Surface(
        shape = RoundedCornerShape(Dimens.RadiusL),
        color = Color.White,
        shadowElevation = Dimens.ElevationMedium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppAsyncImage(
                model = item.audioKey,
                contentDescription = item.english,
                fallbackEmoji = item.emoji,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(Dimens.SpaceM))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.english,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppColors.PrimaryNavy
                )
                Text(
                    text = item.uzbek,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.TextSecondary
                )
            }

            // Normal Listen TTS Button
            IconButton(
                onClick = { ttsController.speakWord(item.english) },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFE1F5FE), CircleShape)
                    .testTag("tts_normal_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = stringResource(R.string.action_listen),
                    tint = AppColors.PrimaryNavy,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceS))

            // Slow Phonics Listen Button
            IconButton(
                onClick = { ttsController.speakWord(item.english, isSlow = true) },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFFFF9C4), CircleShape)
                    .testTag("tts_slow_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = stringResource(R.string.action_slow_listen),
                    tint = AppColors.WarningOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ────────────────────────────────────────────────
// SECTION 3 — Understand
// ────────────────────────────────────────────────
@Composable
private fun UnderstandSection(
    lesson: Lesson,
    ttsController: TtsPlaybackController,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.SpaceL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gap tuzishni o‘rganamiz! 💬",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceM))

        lesson.examples.forEach { example ->
            Surface(
                shape = RoundedCornerShape(Dimens.RadiusM),
                color = Color.White,
                shadowElevation = Dimens.ElevationLow,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.SpaceS)
                    .clickable { ttsController.speakSentence(example.english) }
            ) {
                Row(
                    modifier = Modifier.padding(Dimens.SpaceM),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = example.emoji ?: "✨", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(Dimens.SpaceM))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = example.english,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            text = example.uzbek,
                            fontSize = 14.sp,
                            color = AppColors.TextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = AppColors.PrimarySky
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceL))

        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(Dimens.RadiusPill),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimarySky),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(Dimens.ButtonHeight)
        ) {
            Text(
                text = stringResource(R.string.action_continue),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// ────────────────────────────────────────────────
// SECTION 4 — Practice (Exercises)
// ────────────────────────────────────────────────
@Composable
private fun PracticeSection(
    lesson: Lesson,
    ttsController: TtsPlaybackController,
    feedback: ExerciseAnswerFeedback,
    onAnswer: (Exercise, ExerciseOption) -> Unit,
    onContinue: () -> Unit
) {
    val exercise = lesson.exercises.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = exercise?.questionUz ?: "To‘g‘ri javobni tanlang:",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppColors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceL))

            exercise?.options?.forEach { option ->
                Surface(
                    shape = RoundedCornerShape(Dimens.RadiusM),
                    color = Color.White,
                    shadowElevation = Dimens.ElevationMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.SpaceS)
                        .clickable { onAnswer(exercise, option) }
                ) {
                    Row(
                        modifier = Modifier.padding(Dimens.SpaceM),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = option.emoji ?: "⭐", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(Dimens.SpaceM))
                        Text(
                            text = option.text,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                    }
                }
            }
        }

        // Feedback Bar
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (feedback) {
                is ExerciseAnswerFeedback.Correct -> {
                    Text(
                        text = stringResource(R.string.feedback_correct),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.SuccessGreenDeep
                    )
                }
                is ExerciseAnswerFeedback.Incorrect -> {
                    Text(
                        text = stringResource(R.string.feedback_incorrect),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.ErrorDeep
                    )
                }
                is ExerciseAnswerFeedback.Idle -> { }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            Button(
                onClick = onContinue,
                shape = RoundedCornerShape(Dimens.RadiusPill),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.SuccessGreen),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(Dimens.ButtonHeight)
            ) {
                Text(
                    text = stringResource(R.string.action_next),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// ────────────────────────────────────────────────
// SECTION 5 — Mini Game
// ────────────────────────────────────────────────
@Composable
private fun MiniGameSection(
    lesson: Lesson,
    audioManager: AudioManager,
    onContinue: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Kichik O‘yin: Yulduzlarni tut! ⭐",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(Dimens.SpaceS))
        Text(
            text = "Yulduzchani bosing va ball to‘plang!",
            fontSize = 15.sp,
            color = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXL))

        // Tap star
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(AppColors.StarGold, CircleShape)
                .border(4.dp, Color.White, CircleShape)
                .clickable {
                    score++
                    audioManager.playSfx(AudioEffect.STAR)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "⭐", fontSize = 48.sp)
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceM))

        Text(
            text = "Ball: $score",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppColors.PrimaryNavy
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXL))

        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(Dimens.RadiusPill),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimarySky),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(Dimens.ButtonHeight)
        ) {
            Text(
                text = stringResource(R.string.action_continue),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// ────────────────────────────────────────────────
// SECTION 6 — Review
// ────────────────────────────────────────────────
@Composable
private fun ReviewSection(
    lesson: Lesson,
    ttsController: TtsPlaybackController,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MascotCharacter(
            state = CharacterState.THINKING,
            size = Dimens.MascotSizeLarge,
            speechText = "Xulosa qilamiz! 🌟"
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceL))

        Text(
            text = "Bugun nimalarni o‘rgandik?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceM))

        lesson.vocabulary.forEach { item ->
            Text(
                text = "${item.emoji}  ${item.english} — ${item.uzbek}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.PrimaryNavy,
                modifier = Modifier.padding(vertical = Dimens.SpaceXS)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceXL))

        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(Dimens.RadiusPill),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.SuccessGreen),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(Dimens.ButtonHeight)
        ) {
            Text(
                text = "Natijani ko‘rish 🎉",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// ────────────────────────────────────────────────
// SECTION 7 — Completion
// ────────────────────────────────────────────────
@Composable
private fun CompletionSection(
    lesson: Lesson,
    stars: Int,
    correctCount: Int,
    totalCount: Int,
    audioManager: AudioManager,
    onFinish: () -> Unit
) {
    LaunchedEffect(Unit) {
        audioManager.playSfx(AudioEffect.LESSON_COMPLETE)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiOverlay(isActive = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpaceL),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MascotCharacter(
                state = CharacterState.CELEBRATING,
                size = Dimens.MascotSizeLarge,
                speechText = "Barakalla! 🎉"
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            Text(
                text = stringResource(R.string.completion_congrats),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppColors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            // Stars celebration row
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val isEarned = index < stars
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isEarned) AppColors.StarGold else Color.LightGray,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceM))

            Text(
                text = stringResource(R.string.completion_score_format, correctCount, maxOf(totalCount, 1)),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceXL))

            Button(
                onClick = onFinish,
                shape = RoundedCornerShape(Dimens.RadiusPill),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryNavy),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(Dimens.ButtonHeight)
                    .testTag("finish_lesson_button")
            ) {
                Text(
                    text = stringResource(R.string.action_finish),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
