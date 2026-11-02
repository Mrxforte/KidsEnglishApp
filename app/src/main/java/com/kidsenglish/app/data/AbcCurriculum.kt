package com.kidsenglish.app.data

import com.kidsenglish.app.domain.model.Example
import com.kidsenglish.app.domain.model.Exercise
import com.kidsenglish.app.domain.model.ExerciseOption
import com.kidsenglish.app.domain.model.ExerciseType
import com.kidsenglish.app.domain.model.Lesson
import com.kidsenglish.app.domain.model.VocabularyItem

/**
 * Complete data-driven 26-level ABC Curriculum (A through Z) with Uzbek localization,
 * core vocabulary triplets, examples, and automated interactive exercise generation.
 */
object AbcCurriculum {

    private data class RawData(
        val level: Int,
        val letter: String,
        val titleUz: String,
        val emoji: String,
        val w1En: String, val w1Uz: String, val w1Emoji: String,
        val w2En: String, val w2Uz: String, val w2Emoji: String,
        val w3En: String, val w3Uz: String, val w3Emoji: String
    )

    private val rawLevels = listOf(
        RawData(1, "A", "A — Apple (Olma)", "🍎", "Apple", "Olma", "🍎", "Ant", "Chumoli", "🐜", "Arm", "Qo‘l", "💪"),
        RawData(2, "B", "B — Ball (To‘p)", "🏐", "Ball", "Koptok", "🏐", "Banana", "Banan", "🍌", "Bird", "Qush", "🐦"),
        RawData(3, "C", "C — Cat (Mushuk)", "🐱", "Cat", "Mushuk", "🐱", "Car", "Mashina", "🚗", "Cake", "Tort", "🎂"),
        RawData(4, "D", "D — Dog (It)", "🐶", "Dog", "It", "🐶", "Duck", "O‘rdak", "🦆", "Doll", "Qo‘g‘irchoq", "🧸"),
        RawData(5, "E", "E — Egg (Tuxum)", "🥚", "Egg", "Tuxum", "🥚", "Ear", "Quloq", "👂", "Elephant", "Fil", "🐘"),
        RawData(6, "F", "F — Fish (Baliq)", "🐟", "Fish", "Baliq", "🐟", "Frog", "Qurbaqa", "🐸", "Fan", "Ventilyator", "🌀"),
        RawData(7, "G", "G — Goat (Echki)", "🐐", "Goat", "Echki", "🐐", "Girl", "Qiz", "👧", "Gift", "Sovg‘a", "🎁"),
        RawData(8, "H", "H — Hat (Shlyapa)", "🎩", "Hat", "Shlyapa", "🎩", "Hand", "Qo‘l", "✋", "House", "Uy", "🏠"),
        RawData(9, "I", "I — Ice (Muz)", "🧊", "Ice", "Muz", "🧊", "Ink", "Siyoh", "🖋️", "Insect", "Hasharot", "🐞"),
        RawData(10, "J", "J — Jam (Murabbo)", "🍓", "Jam", "Murabbo", "🍓", "Juice", "Sharbat", "🧃", "Jacket", "Kurtka", "🧥"),
        RawData(11, "K", "K — Key (Kalit)", "🔑", "Key", "Kalit", "🔑", "Kite", "Varrak", "🪁", "King", "Qirol", "👑"),
        RawData(12, "L", "L — Lion (Sher)", "🦁", "Lion", "Sher", "🦁", "Leaf", "Barg", "🍃", "Lamp", "Chiroq", "💡"),
        RawData(13, "M", "M — Moon (Oy)", "🌙", "Moon", "Oy", "🌙", "Milk", "Sut", "🥛", "Monkey", "Maymun", "🐵"),
        RawData(14, "N", "N — Nose (Burun)", "👃", "Nose", "Burun", "👃", "Nest", "In", "🪺", "Net", "To‘r", "🥅"),
        RawData(15, "O", "O — Orange (Apelsin)", "🍊", "Orange", "Apelsin", "🍊", "Owl", "Boyo‘g‘li", "🦉", "Octopus", "Sakkizoyoq", "🐙"),
        RawData(16, "P", "P — Pig (Cho‘chqa)", "🐷", "Pig", "Cho‘chqa", "🐷", "Pen", "Ruchka", "🖊️", "Panda", "Panda", "🐼"),
        RawData(17, "Q", "Q — Queen (Malika)", "👑", "Queen", "Malika", "👸", "Quilt", "Ko‘rpa", "🛌", "Question", "Savol", "❓"),
        RawData(18, "R", "R — Rabbit (Quyon)", "🐰", "Rabbit", "Quyon", "🐰", "Rain", "Yomg‘ir", "🌧️", "Robot", "Robot", "🤖"),
        RawData(19, "S", "S — Sun (Quyosh)", "☀️", "Sun", "Quyosh", "☀️", "Star", "Yulduz", "⭐", "Sock", "Paypoq", "🧦"),
        RawData(20, "T", "T — Tiger (Yo‘lbars)", "🐅", "Tiger", "Yo‘lbars", "🐅", "Tree", "Daraxt", "🌳", "Toy", "O‘yinchoq", "🧸"),
        RawData(21, "U", "U — Umbrella (Soyabon)", "☂️", "Umbrella", "Soyabon", "☂️", "Up", "Yuqoriga", "⬆️", "Uncle", "Amaki", "👨"),
        RawData(22, "V", "V — Van (Mikroavtobus)", "🚐", "Van", "Mikroavtobus", "🚐", "Vest", "Nimcha", "🦺", "Violin", "Skripka", "🎻"),
        RawData(23, "W", "W — Water (Suv)", "💧", "Water", "Suv", "💧", "Window", "Deraza", "🪟", "Whale", "Kit", "🐋"),
        RawData(24, "X", "X — Box (Quti)", "📦", "Box", "Quti", "📦", "Fox", "Tulki", "🦊", "Six", "Olti", "6️⃣"),
        RawData(25, "Y", "Y — Yellow (Sariq)", "🟡", "Yellow", "Sariq", "💛", "Yo-yo", "Yo-yo", "🪀", "Yak", "Qo‘tos", "🐃"),
        RawData(26, "Z", "Z — Zebra (Zebra)", "🦓", "Zebra", "Zebra", "🦓", "Zoo", "Hayvonot bog‘i", "🦒", "Zip", "Fermuar", "👖")
    )

    fun getLesson(levelNumber: Int): Lesson {
        val raw = rawLevels.find { it.level == levelNumber } ?: rawLevels.first()

        val vocab = listOf(
            VocabularyItem(id = "${raw.letter.lowercase()}_1", english = raw.w1En, uzbek = raw.w1Uz, emoji = raw.w1Emoji, exampleSentence = "This is a ${raw.w1En.lowercase()}.", exampleSentenceUz = "Bu ${raw.w1Uz.lowercase()}."),
            VocabularyItem(id = "${raw.letter.lowercase()}_2", english = raw.w2En, uzbek = raw.w2Uz, emoji = raw.w2Emoji, exampleSentence = "I see a ${raw.w2En.lowercase()}.", exampleSentenceUz = "Men ${raw.w2Uz.lowercase()}ni ko‘raman."),
            VocabularyItem(id = "${raw.letter.lowercase()}_3", english = raw.w3En, uzbek = raw.w3Uz, emoji = raw.w3Emoji, exampleSentence = "Look at the ${raw.w3En.lowercase()}.", exampleSentenceUz = "${raw.w3Uz}ga qarang.")
        )

        val examples = listOf(
            Example(raw.w1En, raw.w1Uz, raw.w1Emoji),
            Example(raw.w2En, raw.w2Uz, raw.w2Emoji),
            Example(raw.w3En, raw.w3Uz, raw.w3Emoji)
        )

        // Automated multi-type exercises
        val exercises = listOf(
            Exercise(
                id = "ex_${raw.letter}_1",
                type = ExerciseType.TAP_PICTURE,
                questionUz = "Qaysi biri \"${raw.w1En}\"?",
                correctAnswer = raw.w1En.lowercase(),
                options = listOf(
                    ExerciseOption("1", raw.w1En, raw.w1Emoji, isCorrect = true),
                    ExerciseOption("2", "Ball", "🏐", isCorrect = false),
                    ExerciseOption("3", "Cat", "🐱", isCorrect = false)
                )
            ),
            Exercise(
                id = "ex_${raw.letter}_2",
                type = ExerciseType.MULTIPLE_CHOICE,
                questionUz = "\"${raw.w2Uz}\" inglizcha qanday?",
                correctAnswer = raw.w2En,
                options = listOf(
                    ExerciseOption("a", raw.w3En, isCorrect = false),
                    ExerciseOption("b", raw.w2En, isCorrect = true),
                    ExerciseOption("c", raw.w1En.reversed(), isCorrect = false)
                )
            )
        )

        return Lesson(
            id = "abc_${raw.letter.lowercase()}",
            topicId = "funny_abc",
            lessonNumber = raw.level,
            title = "${raw.letter} — ${raw.w1En}",
            titleUz = "${raw.letter} — ${raw.w1Uz}",
            descriptionUz = "Bugun biz ${raw.letter} harfini va \"${raw.w1En}\" so‘zini o‘rganamiz. Tayyormisiz?",
            emoji = raw.emoji,
            vocabulary = vocab,
            examples = examples,
            exercises = exercises,
            estimatedMinutes = 5,
            difficulty = 1,
            version = 1
        )
    }

    fun getAllLessons(): List<Lesson> {
        return (1..26).map { getLesson(it) }
    }
}

fun getLessonForLevel(level: Int): Lesson {
    return AbcCurriculum.getLesson(level.coerceIn(1, 26))
}
