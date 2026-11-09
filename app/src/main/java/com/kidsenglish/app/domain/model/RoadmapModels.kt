package com.kidsenglish.app.domain.model

import androidx.annotation.StringRes

/**
 * Visual color theme identifier for a Topic Category card in the 3x6 grid.
 */
enum class TopicColorStyle {
    PURPLE,
    ROYAL_BLUE,
    GREEN,
    ORANGE,
    MAGENTA,
    LAVENDER
}

/**
 * Icon identifier for each of the 18 Topic Categories in the Horizontal Roadmap template.
 */
enum class TopicIconType {
    ABC_LETTERS,
    COLOURS_PALETTE,
    NUMBERS_123,
    CLOTHES_SHIRT,
    PETS_HOUSE,
    ZOO_ELEPHANT,
    SWEET_CAKE,
    FRUITS_APPLE,
    CITY_BUILDINGS,
    SCHOOL_CAP,
    FOOD_PLATE,
    SPORTS_BALLS,
    FAMILY_GROUP,
    ACTIONS_RUN,
    HOUSE_HOME,
    FURNITURE_CHAIR,
    CALENDAR_GRID,
    BODY_SENSES
}

/**
 * Custom procedural illustration scene identifier for each Activity Card on the Horizontal Roadmap.
 */
enum class ActivitySceneType {
    ICE_CREAM_COLORING,
    SWEET_SHOP_COUNTER,
    COW_SHADOW_MATCH,
    MONSTER_FACE_STICKERS,
    YUMMY_FOOD_TABLE,
    SUPERMARKET_CART,
    NUMBER_BAKERY_DONUTS,
    ROOM_MYSTERY_QUIZ,
    CITY_POLICE_PATROL,
    KITCHEN_MAGNIFIER_2,
    DOG_MICROPHONE_SPEAK,
    ABC_TRACING_BOARD,
    CRANE_PINK_WORD,
    COLOR_SPEECH_BUBBLES,
    SPELL_RED_BLOCKS,
    MIC_COLOR_CIRCLES
}

/**
 * Domain model representing one of the 18 Topic Categories in the 3x6 Horizontal Grid.
 */
data class RoadmapTopicCategory(
    val id: String,
    @StringRes val titleRes: Int,
    val customTitle: String? = null,
    val iconType: TopicIconType,
    val colorStyle: TopicColorStyle,
    val wordsLearnedProgress: Float,
    val targetSectionIndex: Int,
    val tiltDegrees: Float,
    val isLocked: Boolean = true
)

/**
 * Domain model representing an illustrated Activity Card inside a Horizontal Roadmap section.
 */
data class RoadmapActivityCard(
    val id: String,
    val topicId: String,
    @StringRes val titleRes: Int,
    val sceneType: ActivitySceneType,
    val isLocked: Boolean,
    val isCompleted: Boolean,
    val isLargeBoard: Boolean = false,
    val tiltDegrees: Float = -1.2f,
    val letterChar: Char? = null,
    val phonicsWord: String? = null,
    val phonicsEmoji: String? = null,
    val customTitleText: String? = null
)

/**
 * Domain model representing a full horizontal section on the Levels Roadmap
 * (e.g., FREE Starter Section, ABC Section, COLOURS Section).
 */
data class RoadmapHorizontalSection(
    val id: String,
    @StringRes val bannerTitleRes: Int,
    val iconType: TopicIconType,
    val isFreeBanner: Boolean,
    val wordsLearnedProgress: Float,
    val activities: List<RoadmapActivityCard>
)
