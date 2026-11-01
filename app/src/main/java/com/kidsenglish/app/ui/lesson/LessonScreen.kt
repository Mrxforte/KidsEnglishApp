package com.kidsenglish.app.ui.lesson

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kidsenglish.app.core.audio.rememberSoundManager
import com.kidsenglish.app.domain.model.Example
import com.kidsenglish.app.domain.model.Exercise
import com.kidsenglish.app.domain.model.ExerciseOption
import com.kidsenglish.app.domain.model.ExerciseType
import com.kidsenglish.app.domain.model.Lesson
import com.kidsenglish.app.domain.model.VocabularyItem
import com.kidsenglish.app.ui.theme.*

/**
 * Lesson sections in order
 */
enum class LessonSection {
    WELCOME,
    DISCOVER,
    UNDERSTAND,
    PRACTICE,
    MINI_GAME,
    REVIEW,
    COMPLETION
}

@Composable
fun LessonScreen(
    lesson: Lesson,
    onBack: () -> Unit,
    onLessonCompleted: (stars: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val soundManager = rememberSoundManager()
    var currentSection by remember { mutableStateOf(LessonSection.WELCOME) }
    var correctCount by remember { mutableIntStateOf(0) }
    var totalAnswered by remember { mutableIntStateOf(0) }

    // Lower music while in lesson (voice will play)
    LaunchedEffect(Unit) {
        soundManager.lowerMusicForVoice()
    }

    val progress = when (currentSection) {
        LessonSection.WELCOME -> 0.05f
        LessonSection.DISCOVER -> 0.20f
        LessonSection.UNDERSTAND -> 0.35f
        LessonSection.PRACTICE -> 0.55f
        LessonSection.MINI_GAME -> 0.75f
        LessonSection.REVIEW -> 0.90f
        LessonSection.COMPLETION -> 1f
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(SoftSky, SoftMint, Color(0xFFE8F5E9))
                )
            )
    ) {
        // Top bar
        LessonTopBar(
            title = lesson.titleUz,
            progress = progress,
            onBack = {
                soundManager.restoreMusicVolume()
                onBack()
            }
        )

        // Section content
        AnimatedContent(
            targetState = currentSection,
            transitionSpec = {
                (fadeIn(tween(300)) + scaleIn(initialScale = 0.92f, animationSpec = tween(300)))
                    .togetherWith(fadeOut(tween(200)))
            },
            modifier = Modifier.weight(1f),
            label = "section"
        ) { section ->
            when (section) {
                LessonSection.WELCOME -> WelcomeSection(
                    lesson = lesson,
                    onContinue = { currentSection = LessonSection.DISCOVER }
                )
                LessonSection.DISCOVER -> DiscoverSection(
                    lesson = lesson,
                    onContinue = { currentSection = LessonSection.UNDERSTAND }
                )
                LessonSection.UNDERSTAND -> UnderstandSection(
                    lesson = lesson,
                    onContinue = { currentSection = LessonSection.PRACTICE }
                )
                LessonSection.PRACTICE -> PracticeSection(
                    lesson = lesson,
                    onAnswer = { isCorrect ->
                        totalAnswered++
                        if (isCorrect) {
                            correctCount++
                            soundManager.playCorrectSound()
                        }
                    },
                    onContinue = { currentSection = LessonSection.MINI_GAME }
                )
                LessonSection.MINI_GAME -> MiniGameSection(
                    lesson = lesson,
                    onContinue = { currentSection = LessonSection.REVIEW }
                )
                LessonSection.REVIEW -> ReviewSection(
                    lesson = lesson,
                    onContinue = { currentSection = LessonSection.COMPLETION }
                )
                LessonSection.COMPLETION -> CompletionSection(
                    lesson = lesson,
                    correctCount = correctCount,
                    totalAnswered = totalAnswered,
                    onFinish = { stars ->
                        soundManager.playStarSound()
                        soundManager.restoreMusicVolume()
                        onLessonCompleted(stars)
                    }
                )
            }
        }
    }
}

@Composable
private fun LessonTopBar(
    title: String,
    progress: Float,
    onBack: () -> Unit
) {
    Surface(
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Orqaga")
                }
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = NodeCompleted,
                trackColor = Color(0xFFE0E0E0)
            )
        }
    }
}

// ────────────────────────────────────────────────
// SECTION A — Welcome
// ────────────────────────────────────────────────
@Composable
private fun WelcomeSection(
    lesson: Lesson,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = lesson.emoji, fontSize = 96.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Salom! 👋",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = lesson.titleUz,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentOrange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = lesson.descriptionUz,
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(40.dp))
        BigContinueButton(text = "Boshlaymiz!", onClick = onContinue)
    }
}

// ────────────────────────────────────────────────
// SECTION B — Discover (Vocabulary)
// ────────────────────────────────────────────────
@Composable
private fun DiscoverSection(
    lesson: Lesson,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Yangi so‘zlarni bilib olaylik!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(20.dp))

        lesson.vocabulary.forEach { item ->
            VocabularyCard(item = item)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        BigContinueButton(text = "Davom etamiz", onClick = onContinue)
    }
}

@Composable
private fun VocabularyCard(item: VocabularyItem) {
    val soundManager = rememberSoundManager()

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = item.emoji, fontSize = 72.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.english,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NodeAvailable
            )
            Text(
                text = item.uzbek,
                fontSize = 18.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Play pronunciation button
            Button(
                onClick = { soundManager.playTapSound() /* TODO: play TTS */ },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = NodeCurrent),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Tingla", tint = Color.White)
            }
        }
    }
}

// ────────────────────────────────────────────────
// SECTION C — Understand
// ────────────────────────────────────────────────
@Composable
private fun UnderstandSection(
    lesson: Lesson,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tushunaylik!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Bu harf va so‘zlar haqida qisqacha:",
                    fontSize = 15.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                lesson.examples.forEach { example ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        if (example.emoji != null) {
                            Text(text = example.emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Column {
                            Text(
                                text = example.english,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NodeAvailable
                            )
                            Text(
                                text = example.uzbek,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        BigContinueButton(text = "Mashq qilamiz!", onClick = onContinue)
    }
}

// ────────────────────────────────────────────────
// SECTION D — Practice
// ────────────────────────────────────────────────
@Composable
private fun PracticeSection(
    lesson: Lesson,
    onAnswer: (Boolean) -> Unit,
    onContinue: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val exercises = lesson.exercises
    val exercise = exercises.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mashqlar (${currentIndex + 1}/${exercises.size})",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (exercise != null) {
            ExerciseCard(
                exercise = exercise,
                onAnswered = { isCorrect ->
                    onAnswer(isCorrect)
                    if (currentIndex < exercises.lastIndex) {
                        currentIndex++
                    } else {
                        onContinue()
                    }
                }
            )
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: Exercise,
    onAnswered: (Boolean) -> Unit
) {
    var selectedId by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = exercise.questionUz,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(24.dp))

        exercise.options.forEach { option ->
            val isSelected = selectedId == option.id
            val bgColor = when {
                !showResult -> Color.White
                option.isCorrect -> NodeCompleted.copy(alpha = 0.2f)
                isSelected && !option.isCorrect -> Color(0xFFFFCDD2)
                else -> Color.White
            }
            val borderColor = when {
                !showResult && isSelected -> NodeAvailable
                showResult && option.isCorrect -> NodeCompleted
                showResult && isSelected && !option.isCorrect -> Color(0xFFE53935)
                else -> Color(0xFFE0E0E0)
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = bgColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .clickable(enabled = !showResult) {
                        selectedId = option.id
                        showResult = true
                        onAnswered(option.isCorrect)
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (option.emoji != null) {
                        Text(text = option.emoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = option.text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (showResult && option.isCorrect) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = NodeCompleted)
                    }
                    if (showResult && isSelected && !option.isCorrect) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFE53935))
                    }
                }
            }
        }
    }
}

// ────────────────────────────────────────────────
// SECTION E — Mini Game (simple version)
// ────────────────────────────────────────────────
@Composable
private fun MiniGameSection(
    lesson: Lesson,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎮", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Mini o‘yin!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "To‘g‘ri rasmni toping",
            fontSize = 16.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Simple tap-the-correct-picture game
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf("🐶", "🐱", "🍎").forEach { emoji ->
                val isCorrect = emoji == lesson.emoji
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(90.dp)
                        .clickable { if (isCorrect) onContinue() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = emoji, fontSize = 48.sp)
                    }
                }
            }
        }
    }
}

// ────────────────────────────────────────────────
// SECTION F — Review
// ────────────────────────────────────────────────
@Composable
private fun ReviewSection(
    lesson: Lesson,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Qayta ko‘rib chiqamiz",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(24.dp))

        lesson.vocabulary.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = item.emoji, fontSize = 36.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = item.english,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NodeAvailable
                    )
                    Text(text = item.uzbek, fontSize = 15.sp, color = TextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        BigContinueButton(text = "Yakunlash", onClick = onContinue)
    }
}

// ────────────────────────────────────────────────
// SECTION G — Completion
// ────────────────────────────────────────────────
@Composable
private fun CompletionSection(
    lesson: Lesson,
    correctCount: Int,
    totalAnswered: Int,
    onFinish: (stars: Int) -> Unit
) {
    val stars = when {
        totalAnswered == 0 -> 3
        correctCount.toFloat() / totalAnswered >= 0.9f -> 3
        correctCount.toFloat() / totalAnswered >= 0.6f -> 2
        else -> 1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎉", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Ajoyib!",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )
        Text(
            text = "Darsni muvaffaqiyatli tugatdingiz",
            fontSize = 16.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Stars
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < stars) StarGold else StarEmpty,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$correctCount / $totalAnswered to‘g‘ri javob",
            fontSize = 16.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(40.dp))
        BigContinueButton(text = "Roadmapga qaytish") {
            onFinish(stars)
        }
    }
}

@Composable
private fun BigContinueButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = NodeAvailable),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ────────────────────────────────────────────────
// Sample Lesson — Letter C
// ────────────────────────────────────────────────
fun createSampleLessonC(): Lesson {
    return Lesson(
        id = "abc_c",
        topicId = "funny_abc",
        lessonNumber = 3,
        title = "C — Cat",
        titleUz = "C — Mushuk",
        descriptionUz = "Bugun biz C harfini va \"Cat\" so‘zini o‘rganamiz. Tayyormisiz?",
        emoji = "🐱",
        vocabulary = listOf(
            VocabularyItem(
                id = "cat",
                english = "Cat",
                uzbek = "Mushuk",
                emoji = "🐱",
                exampleSentence = "I see a cat.",
                exampleSentenceUz = "Men mushukni ko‘raman."
            ),
            VocabularyItem(
                id = "car",
                english = "Car",
                uzbek = "Mashina",
                emoji = "🚗"
            )
        ),
        examples = listOf(
            Example("Cat", "Mushuk", "🐱"),
            Example("A cat", "Bir mushuk", "🐱"),
            Example("I see a cat", "Men mushukni ko‘raman", "👀")
        ),
        exercises = listOf(
            Exercise(
                id = "ex1",
                type = ExerciseType.TAP_PICTURE,
                questionUz = "Qaysi biri \"Cat\"?",
                correctAnswer = "cat",
                options = listOf(
                    ExerciseOption("1", "Cat", "🐱", isCorrect = true),
                    ExerciseOption("2", "Dog", "🐶", isCorrect = false),
                    ExerciseOption("3", "Apple", "🍎", isCorrect = false)
                )
            ),
            Exercise(
                id = "ex2",
                type = ExerciseType.MULTIPLE_CHOICE,
                questionUz = "\"Mushuk\" inglizcha qanday?",
                correctAnswer = "Cat",
                options = listOf(
                    ExerciseOption("a", "Dog", isCorrect = false),
                    ExerciseOption("b", "Cat", isCorrect = true),
                    ExerciseOption("c", "Car", isCorrect = false)
                )
            )
        )
    )
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun LessonScreenPreview() {
    KidsEnglishTheme {
        LessonScreen(
            lesson = createSampleLessonC(),
            onBack = {},
            onLessonCompleted = {}
        )
    }
}
