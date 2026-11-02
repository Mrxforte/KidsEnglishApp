package com.kidsenglish.app

import com.kidsenglish.app.domain.model.Exercise
import com.kidsenglish.app.domain.model.ExerciseOption
import com.kidsenglish.app.domain.model.ExerciseType
import com.kidsenglish.app.domain.model.Lesson
import com.kidsenglish.app.lesson_engine.EngineLessonSection
import com.kidsenglish.app.lesson_engine.ExerciseAnswerFeedback
import com.kidsenglish.app.lesson_engine.LessonEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LessonEngineTest {

    private lateinit var sampleLesson: Lesson
    private lateinit var engine: LessonEngine

    @Before
    fun setup() {
        sampleLesson = Lesson(
            id = "test_lesson",
            topicId = "green_valley",
            lessonNumber = 1,
            title = "A — Apple",
            titleUz = "A — Olma",
            descriptionUz = "Test description",
            emoji = "🍎",
            exercises = listOf(
                Exercise(
                    id = "ex1",
                    type = ExerciseType.TAP_PICTURE,
                    questionUz = "Qaysi biri Apple?",
                    correctAnswer = "apple",
                    options = listOf(
                        ExerciseOption("opt1", "Apple", "🍎", isCorrect = true),
                        ExerciseOption("opt2", "Ball", "🏐", isCorrect = false)
                    )
                )
            )
        )
        engine = LessonEngine(sampleLesson)
    }

    @Test
    fun `initial state starts at WELCOME section`() {
        assertEquals(EngineLessonSection.WELCOME, engine.state.value.currentSection)
        assertEquals(0, engine.state.value.correctAnswersCount)
        assertEquals(0, engine.state.value.totalAnsweredCount)
    }

    @Test
    fun `moveToNextSection advances through sections sequentially`() {
        engine.moveToNextSection()
        assertEquals(EngineLessonSection.DISCOVER, engine.state.value.currentSection)

        engine.moveToNextSection()
        assertEquals(EngineLessonSection.UNDERSTAND, engine.state.value.currentSection)

        engine.moveToNextSection()
        assertEquals(EngineLessonSection.PRACTICE, engine.state.value.currentSection)
    }

    @Test
    fun `correct answer submission updates score and feedback`() {
        val exercise = sampleLesson.exercises.first()
        val correctOption = exercise.options.first { it.isCorrect }

        engine.submitAnswer(exercise, correctOption.id, isCorrect = true)

        assertEquals(1, engine.state.value.correctAnswersCount)
        assertEquals(1, engine.state.value.totalAnsweredCount)
        assertTrue(engine.state.value.feedback is ExerciseAnswerFeedback.Correct)
    }

    @Test
    fun `incorrect answer submission records error feedback`() {
        val exercise = sampleLesson.exercises.first()
        val incorrectOption = exercise.options.first { !it.isCorrect }

        engine.submitAnswer(exercise, incorrectOption.id, isCorrect = false)

        assertEquals(0, engine.state.value.correctAnswersCount)
        assertEquals(1, engine.state.value.totalAnsweredCount)
        assertTrue(engine.state.value.feedback is ExerciseAnswerFeedback.Incorrect)
    }

    @Test
    fun `completion awards 3 stars for 100 percent accuracy`() {
        val exercise = sampleLesson.exercises.first()
        engine.submitAnswer(exercise, "opt1", isCorrect = true)

        // Advance to completion
        engine.moveToNextSection() // DISCOVER
        engine.moveToNextSection() // UNDERSTAND
        engine.moveToNextSection() // PRACTICE
        engine.moveToNextSection() // MINI_GAME
        engine.moveToNextSection() // REVIEW
        engine.moveToNextSection() // COMPLETION

        assertEquals(EngineLessonSection.COMPLETION, engine.state.value.currentSection)
        assertEquals(3, engine.state.value.starsEarned)
    }
}
