package com.kidsenglish.app.lesson_engine

import com.kidsenglish.app.domain.model.Exercise
import com.kidsenglish.app.domain.model.Lesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class EngineLessonSection {
    WELCOME,
    DISCOVER,
    UNDERSTAND,
    PRACTICE,
    MINI_GAME,
    REVIEW,
    COMPLETION
}

sealed class ExerciseAnswerFeedback {
    object Idle : ExerciseAnswerFeedback()
    data class Correct(val message: String = "Ajoyib!") : ExerciseAnswerFeedback()
    data class Incorrect(val correctAnswer: String) : ExerciseAnswerFeedback()
}

data class LessonEngineState(
    val currentSection: EngineLessonSection = EngineLessonSection.WELCOME,
    val currentExerciseIndex: Int = 0,
    val correctAnswersCount: Int = 0,
    val totalAnsweredCount: Int = 0,
    val feedback: ExerciseAnswerFeedback = ExerciseAnswerFeedback.Idle,
    val starsEarned: Int = 0,
    val progressPercent: Float = 0.05f
)

/**
 * Reusable, decoupled state machine that drives educational lesson progression,
 * exercise validation, and star scoring.
 */
class LessonEngine(val lesson: Lesson) {

    private val _state = MutableStateFlow(LessonEngineState())
    val state: StateFlow<LessonEngineState> = _state.asStateFlow()

    fun moveToNextSection() {
        val current = _state.value.currentSection
        val nextSection = when (current) {
            EngineLessonSection.WELCOME -> EngineLessonSection.DISCOVER
            EngineLessonSection.DISCOVER -> EngineLessonSection.UNDERSTAND
            EngineLessonSection.UNDERSTAND -> EngineLessonSection.PRACTICE
            EngineLessonSection.PRACTICE -> EngineLessonSection.MINI_GAME
            EngineLessonSection.MINI_GAME -> EngineLessonSection.REVIEW
            EngineLessonSection.REVIEW -> {
                // Calculate final stars (1..3)
                val total = maxOf(_state.value.totalAnsweredCount, 1)
                val accuracy = _state.value.correctAnswersCount.toFloat() / total
                val stars = when {
                    accuracy >= 0.85f -> 3
                    accuracy >= 0.50f -> 2
                    else -> 1
                }
                _state.value = _state.value.copy(starsEarned = stars)
                EngineLessonSection.COMPLETION
            }
            EngineLessonSection.COMPLETION -> EngineLessonSection.COMPLETION
        }

        val progress = when (nextSection) {
            EngineLessonSection.WELCOME -> 0.05f
            EngineLessonSection.DISCOVER -> 0.20f
            EngineLessonSection.UNDERSTAND -> 0.38f
            EngineLessonSection.PRACTICE -> 0.55f
            EngineLessonSection.MINI_GAME -> 0.75f
            EngineLessonSection.REVIEW -> 0.90f
            EngineLessonSection.COMPLETION -> 1.0f
        }

        _state.value = _state.value.copy(
            currentSection = nextSection,
            progressPercent = progress,
            feedback = ExerciseAnswerFeedback.Idle
        )
    }

    fun submitAnswer(exercise: Exercise, selectedOptionId: String, isCorrect: Boolean) {
        val updatedCorrect = if (isCorrect) _state.value.correctAnswersCount + 1 else _state.value.correctAnswersCount
        val updatedTotal = _state.value.totalAnsweredCount + 1

        val feedback = if (isCorrect) {
            ExerciseAnswerFeedback.Correct()
        } else {
            ExerciseAnswerFeedback.Incorrect(exercise.correctAnswer)
        }

        _state.value = _state.value.copy(
            correctAnswersCount = updatedCorrect,
            totalAnsweredCount = updatedTotal,
            feedback = feedback
        )
    }

    fun clearFeedback() {
        _state.value = _state.value.copy(feedback = ExerciseAnswerFeedback.Idle)
    }
}
