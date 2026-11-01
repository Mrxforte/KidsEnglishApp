package com.kidsenglish.app.domain.model

/**
 * Full lesson content – versioned and reusable.
 */
data class Lesson(
    val id: String,
    val topicId: String,
    val lessonNumber: Int,
    val title: String,                 // "C — Cat"
    val titleUz: String,               // "C — Mushuk"
    val descriptionUz: String,
    val emoji: String = "🐱",
    val learningObjectives: List<String> = emptyList(),
    val vocabulary: List<VocabularyItem> = emptyList(),
    val examples: List<Example> = emptyList(),
    val exercises: List<Exercise> = emptyList(),
    val estimatedMinutes: Int = 5,
    val difficulty: Int = 1,
    val version: Int = 1
)

data class VocabularyItem(
    val id: String,
    val english: String,
    val uzbek: String,
    val emoji: String,
    val audioKey: String? = null,      // for TTS / cached audio
    val exampleSentence: String? = null,
    val exampleSentenceUz: String? = null
)

data class Example(
    val english: String,
    val uzbek: String,
    val emoji: String? = null
)

enum class ExerciseType {
    IMAGE_SELECT,
    WORD_MATCH,
    LISTEN_CHOOSE,
    TAP_PICTURE,
    MISSING_LETTER,
    MULTIPLE_CHOICE,
    TRUE_FALSE
}

data class Exercise(
    val id: String,
    val type: ExerciseType,
    val questionUz: String,
    val questionEnglish: String? = null,
    val correctAnswer: String,
    val options: List<ExerciseOption> = emptyList(),
    val audioKey: String? = null,
    val emoji: String? = null
)

data class ExerciseOption(
    val id: String,
    val text: String,
    val emoji: String? = null,
    val isCorrect: Boolean = false
)

/**
 * Progress for a single lesson
 */
data class LessonProgress(
    val lessonId: String,
    val starsEarned: Int = 0,
    val isCompleted: Boolean = false,
    val correctAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val lastPlayedAt: Long = 0L
)
