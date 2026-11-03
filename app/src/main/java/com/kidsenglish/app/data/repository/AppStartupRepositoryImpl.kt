package com.kidsenglish.app.data.repository

import com.kidsenglish.app.core.resources.AppStrings
import com.kidsenglish.app.data.preferences.AppPreferencesDataSource
import com.kidsenglish.app.domain.model.ActivitySceneType
import com.kidsenglish.app.domain.model.AppStartupConfig
import com.kidsenglish.app.domain.model.RoadmapActivityCard
import com.kidsenglish.app.domain.model.RoadmapHorizontalSection
import com.kidsenglish.app.domain.model.RoadmapTopicCategory
import com.kidsenglish.app.domain.model.TopicColorStyle
import com.kidsenglish.app.domain.model.TopicIconType
import com.kidsenglish.app.domain.repository.AppStartupRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

/**
 * Production implementation of [AppStartupRepository] backed by [AppPreferencesDataSource]
 * and providing the structured Horizontal Levels Roadmap Template curriculum.
 */
@Singleton
class AppStartupRepositoryImpl @Inject constructor(
    private val preferencesDataSource: AppPreferencesDataSource
) : AppStartupRepository {

    override fun observeStartupConfig(): Flow<AppStartupConfig> {
        return preferencesDataSource.startupConfigFlow
    }

    override suspend fun recordAppLaunch(): AppStartupConfig {
        return preferencesDataSource.incrementLaunchCount()
    }

    override suspend fun completeActivityAndEarnSticker(activityId: String) {
        preferencesDataSource.recordActivityCompleted(activityId)
    }

    override suspend fun updateAudioPreferences(
        soundEffectsEnabled: Boolean,
        backgroundMusicEnabled: Boolean
    ) {
        preferencesDataSource.setAudioPreferences(
            soundEffectsEnabled = soundEffectsEnabled,
            backgroundMusicEnabled = backgroundMusicEnabled
        )
    }

    override fun getTopicCategories(completedIds: Set<String>): List<RoadmapTopicCategory> {
        val baseBonus = (completedIds.size * 0.08f).coerceAtMost(0.6f)

        val topicNames = listOf(
            // Initial 18 topics matching Screenshot 1
            "ABC Alphabet", "Colours & Paint", "123 Numbers", "Clothes & Style", "Pets & House", "Zoo Animals",
            "Sweet Cake", "Fruits & Apples", "City & Buildings", "School & Learning", "Food & Meals", "Sports & Games",
            "Family & Friends", "Action Verbs", "House & Home", "Furniture Types", "Calendar & Days", "Body & Senses",

            // Topics 19 - 50: Nature, Food & Everyday Life
            "Vegetables", "Drinks & Juices", "2D Shapes", "3D Shapes", "Phonics Sounds", "Farm Animals",
            "Sea World", "Wild Birds", "Garden Insects", "Toys & Dolls", "Vehicles & Transport", "Jobs & Careers",
            "Weather Forecast", "Seasons & Climate", "Space & Rockets", "Solar Planets", "Dinosaurs & Fossils",
            "Fairytales & Magic", "Feelings & Emotions", "Tools & Workshop", "Musical Instruments", "Science Lab",
            "Clock & Time", "Money & Coins", "Opposites & Pairs", "Rhyming Words", "Sight Words Level 1",
            "Sight Words Level 2", "Kitchenware", "Table Setting", "Bathroom Hygiene", "Backyard & Lawn",

            // Topics 51 - 100: Nature, Adventures & Hobbies
            "Garden Flowers", "Forest Trees", "High Mountains", "Sandy Beach", "Circus Magic",
            "Camping Fun", "Outdoor Picnic", "Cooking Basics", "Baking Sweets", "Painting & Art",
            "Paper Origami", "Handicrafts", "Birthday Party", "Christmas Fun", "New Year Wishes",
            "Halloween Spooky", "Easter Eggs", "Daily Greetings", "Good Manners", "Safety Rules",
            "Traffic Signal", "Children Hospital", "Fire Station", "Police Officers", "City Library",
            "Airport Flights", "Train Station", "Supermarket Mart", "Toy Kingdom", "Sweet Bakery",
            "Family Restaurant", "Pet Shop Care", "Auto Mechanics", "Building Construction", "Solar System",
            "Stars & Galaxies", "Volcanoes & Lava", "Rocks & Minerals", "Water Cycle", "Animal Baby Names",
            "Animal Habitats", "Animal Sounds", "Camouflage Secrets", "Deep Blue Ocean", "Tropical Jungle",

            // Topics 101 - 150: Science, Culture & Words
            "Savannah Animals", "Bugs & Honey Bees", "Pine Trees & Cones", "Berries & Cherries", "Farm Harvest",
            "Spices & Flavors", "Breads & Buns", "Fast Food Meals", "Pizza & Pasta", "Soup & Salads",
            "Breakfast Treats", "Lunch Plates", "Dinner Special", "Electronic Appliances", "Bedroom Decor",
            "Gardening Tools", "School Supplies", "Music & Melodies", "Orchestra Band", "Winter Sports",
            "Summer Sports", "Water Swimming", "Team Games", "Board Games", "Puzzle Solving",
            "Drawing & Sketching", "Singing & Chants", "Dancing Styles", "Gymnastics Moves", "Cycling & Bikes",
            "Space Shuttle", "Astronaut Mission", "Ancient Castles", "Knights & Shields", "Pirate Ships",
            "Egyptian Pyramids", "World Explorers", "Global Landmarks", "Suspension Bridges", "City Skyscrapers",

            // Topics 151 - 200: Community, Tech, Logic & General English
            "Highways & Roads", "Underground Subway", "Sailing Boats", "Helicopters", "Heavy Duty Trucks",
            "Special Vehicles", "Solar Powered Cars", "Electric Bullet Train", "Cable Car Ride", "Space Drones",
            "Friendly Robots", "Computers & Tech", "Smart Devices", "Logic & Patterns", "Memory Match",
            "Color Mixing Game", "Shadow Matching", "Maze Solver", "Find Differences", "Tangram Shapes",
            "Dot to Dot Fun", "Letter Tracing", "Word Search", "Crossword Kids", "Spelling Bee",
            "Picture Dictionary", "English Dialogues", "Uzbek Words", "World Flags", "Friendly Neighbors",
            "Community Helpers", "Firefighter Hero", "Police Patrol", "Doctor & Nurse", "Teacher Class",
            "Master Chef", "Farm Producer", "Airline Pilot", "Space Scientist", "Creative Artist",
            "Music Composer", "Champion Athlete", "Story Writer", "Civil Engineer", "Home Builder",
            "Dental Care", "Veterinarian", "Seafood Fisherman", "Park Ranger", "Library Curator"
        )

        val colors = TopicColorStyle.entries
        val icons = TopicIconType.entries

        return topicNames.mapIndexed { index, name ->
            val colorStyle = colors[index % colors.size]
            val iconType = icons[index % icons.size]
            val tilt = if (index % 2 == 0) -1.2f - (index % 3) * 0.1f else 1.1f + (index % 3) * 0.1f
            val progress = ((index * 3 + 12) % 80) / 100f
            val targetSection = index % 4

            val stringRes = when (index) {
                0 -> AppStrings.Roadmap.TOPIC_ABC
                1 -> AppStrings.Roadmap.TOPIC_COLOURS
                2 -> AppStrings.Roadmap.TOPIC_NUMBERS
                3 -> AppStrings.Roadmap.TOPIC_CLOTHES
                4 -> AppStrings.Roadmap.TOPIC_PETS
                5 -> AppStrings.Roadmap.TOPIC_ZOO
                6 -> AppStrings.Roadmap.TOPIC_SWEETS
                7 -> AppStrings.Roadmap.TOPIC_FRUITS
                8 -> AppStrings.Roadmap.TOPIC_CITY
                9 -> AppStrings.Roadmap.TOPIC_SCHOOL
                10 -> AppStrings.Roadmap.TOPIC_FOOD
                11 -> AppStrings.Roadmap.TOPIC_SPORTS
                12 -> AppStrings.Roadmap.TOPIC_FAMILY
                13 -> AppStrings.Roadmap.TOPIC_ACTIONS
                14 -> AppStrings.Roadmap.TOPIC_HOUSE
                15 -> AppStrings.Roadmap.TOPIC_FURNITURE
                16 -> AppStrings.Roadmap.TOPIC_CALENDAR
                17 -> AppStrings.Roadmap.TOPIC_BODY
                else -> AppStrings.Roadmap.TOPIC_ABC
            }

            RoadmapTopicCategory(
                id = "topic_${index + 1}",
                titleRes = stringRes,
                customTitle = if (index >= 18) name else null,
                iconType = iconType,
                colorStyle = colorStyle,
                wordsLearnedProgress = progress,
                targetSectionIndex = targetSection,
                tiltDegrees = tilt
            )
        }
    }

    override fun getHorizontalSections(completedIds: Set<String>): List<RoadmapHorizontalSection> {
        return listOf(
            // Section 0: FREE Starter Games (Screenshots 2 & 5)
            RoadmapHorizontalSection(
                id = "section_free",
                bannerTitleRes = AppStrings.Roadmap.FREE_SECTION_TITLE,
                iconType = TopicIconType.ABC_LETTERS,
                isFreeBanner = true,
                wordsLearnedProgress = 0.45f,
                activities = listOf(
                    RoadmapActivityCard(
                        id = "free_ice_cream",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_ICE_CREAM_PAINT,
                        sceneType = ActivitySceneType.ICE_CREAM_COLORING,
                        isLocked = false,
                        isCompleted = "free_ice_cream" in completedIds,
                        tiltDegrees = -1.3f
                    ),
                    RoadmapActivityCard(
                        id = "free_yummy_table",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_YUMMY_TABLE,
                        sceneType = ActivitySceneType.YUMMY_FOOD_TABLE,
                        isLocked = false,
                        isCompleted = "free_yummy_table" in completedIds,
                        tiltDegrees = 1.1f
                    ),
                    RoadmapActivityCard(
                        id = "free_sweet_shop",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_SWEET_SHOP,
                        sceneType = ActivitySceneType.SWEET_SHOP_COUNTER,
                        isLocked = false,
                        isCompleted = "free_sweet_shop" in completedIds,
                        tiltDegrees = 1.0f
                    ),
                    RoadmapActivityCard(
                        id = "free_supermarket",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_SUPERMARKET,
                        sceneType = ActivitySceneType.SUPERMARKET_CART,
                        isLocked = false,
                        isCompleted = "free_supermarket" in completedIds,
                        tiltDegrees = -1.2f
                    ),
                    RoadmapActivityCard(
                        id = "free_cow_shadow",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_ANIMAL_SHADOW,
                        sceneType = ActivitySceneType.COW_SHADOW_MATCH,
                        isLocked = false,
                        isCompleted = "free_cow_shadow" in completedIds,
                        tiltDegrees = -1.1f
                    ),
                    RoadmapActivityCard(
                        id = "free_number_bakery",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_NUMBER_BAKERY,
                        sceneType = ActivitySceneType.NUMBER_BAKERY_DONUTS,
                        isLocked = false,
                        isCompleted = "free_number_bakery" in completedIds,
                        tiltDegrees = 1.3f
                    ),
                    RoadmapActivityCard(
                        id = "free_monster",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_FUNNY_MONSTER,
                        sceneType = ActivitySceneType.MONSTER_FACE_STICKERS,
                        isLocked = false,
                        isCompleted = "free_monster" in completedIds,
                        tiltDegrees = 1.2f
                    ),
                    RoadmapActivityCard(
                        id = "free_room_mystery",
                        topicId = "section_free",
                        titleRes = AppStrings.Roadmap.ACTIVITY_ROOM_MYSTERY,
                        sceneType = ActivitySceneType.ROOM_MYSTERY_QUIZ,
                        isLocked = false,
                        isCompleted = "free_room_mystery" in completedIds,
                        tiltDegrees = -1.0f
                    )
                )
            ),
            // Section 1: City, Kitchen, Speaking & Giant ABC Tracing Board (Screenshot 3)
            RoadmapHorizontalSection(
                id = "section_abc_board",
                bannerTitleRes = AppStrings.Roadmap.TOPIC_ABC,
                iconType = TopicIconType.ABC_LETTERS,
                isFreeBanner = false,
                wordsLearnedProgress = 0.38f,
                activities = listOf(
                    RoadmapActivityCard(
                        id = "abc_city_patrol",
                        topicId = "section_abc_board",
                        titleRes = AppStrings.Roadmap.ACTIVITY_CITY_PATROL,
                        sceneType = ActivitySceneType.CITY_POLICE_PATROL,
                        isLocked = false,
                        isCompleted = "abc_city_patrol" in completedIds,
                        tiltDegrees = -1.2f
                    ),
                    RoadmapActivityCard(
                        id = "abc_dog_voice",
                        topicId = "section_abc_board",
                        titleRes = AppStrings.Roadmap.ACTIVITY_VOICE_REPEAT,
                        sceneType = ActivitySceneType.DOG_MICROPHONE_SPEAK,
                        isLocked = false,
                        isCompleted = "abc_dog_voice" in completedIds,
                        tiltDegrees = 1.4f
                    ),
                    RoadmapActivityCard(
                        id = "abc_kitchen_find",
                        topicId = "section_abc_board",
                        titleRes = AppStrings.Roadmap.ACTIVITY_KITCHEN_FIND,
                        sceneType = ActivitySceneType.KITCHEN_MAGNIFIER_2,
                        isLocked = false,
                        isCompleted = "abc_kitchen_find" in completedIds,
                        tiltDegrees = 1.1f
                    ),
                    RoadmapActivityCard(
                        id = "abc_tracing_board",
                        topicId = "section_abc_board",
                        titleRes = AppStrings.Roadmap.ACTIVITY_ABC_TRACING,
                        sceneType = ActivitySceneType.ABC_TRACING_BOARD,
                        isLocked = false,
                        isCompleted = "abc_tracing_board" in completedIds,
                        isLargeBoard = true,
                        tiltDegrees = -0.8f
                    )
                )
            ),
            // Section 2: COLOURS Section with Unlocked & Padlocked Cards (Screenshot 4)
            RoadmapHorizontalSection(
                id = "section_colours",
                bannerTitleRes = AppStrings.Roadmap.TOPIC_COLOURS,
                iconType = TopicIconType.COLOURS_PALETTE,
                isFreeBanner = false,
                wordsLearnedProgress = 0.28f,
                activities = listOf(
                    RoadmapActivityCard(
                        id = "col_ice_cream",
                        topicId = "section_colours",
                        titleRes = AppStrings.Roadmap.ACTIVITY_ICE_CREAM_PAINT,
                        sceneType = ActivitySceneType.ICE_CREAM_COLORING,
                        isLocked = false,
                        isCompleted = "col_ice_cream" in completedIds,
                        tiltDegrees = 1.2f
                    ),
                    RoadmapActivityCard(
                        id = "col_bubbles",
                        topicId = "section_colours",
                        titleRes = AppStrings.Roadmap.ACTIVITY_COLOR_BUBBLES,
                        sceneType = ActivitySceneType.COLOR_SPEECH_BUBBLES,
                        isLocked = true,
                        isCompleted = false,
                        tiltDegrees = -1.1f
                    ),
                    RoadmapActivityCard(
                        id = "col_sweet_shop",
                        topicId = "section_colours",
                        titleRes = AppStrings.Roadmap.ACTIVITY_SWEET_SHOP,
                        sceneType = ActivitySceneType.SWEET_SHOP_COUNTER,
                        isLocked = false,
                        isCompleted = "col_sweet_shop" in completedIds,
                        tiltDegrees = -1.0f
                    ),
                    RoadmapActivityCard(
                        id = "col_spell_red",
                        topicId = "section_colours",
                        titleRes = AppStrings.Roadmap.ACTIVITY_SPELL_CONE,
                        sceneType = ActivitySceneType.SPELL_RED_BLOCKS,
                        isLocked = true,
                        isCompleted = false,
                        tiltDegrees = 1.3f
                    ),
                    RoadmapActivityCard(
                        id = "col_crane_pink",
                        topicId = "section_colours",
                        titleRes = AppStrings.Roadmap.ACTIVITY_CRANE_WORDS,
                        sceneType = ActivitySceneType.CRANE_PINK_WORD,
                        isLocked = true,
                        isCompleted = false,
                        tiltDegrees = 1.1f
                    ),
                    RoadmapActivityCard(
                        id = "col_mic_circles",
                        topicId = "section_colours",
                        titleRes = AppStrings.Roadmap.ACTIVITY_VOICE_REPEAT,
                        sceneType = ActivitySceneType.MIC_COLOR_CIRCLES,
                        isLocked = true,
                        isCompleted = false,
                        tiltDegrees = -1.2f
                    )
                )
            ),
            // Section 3: 123 NUMBERS Section
            RoadmapHorizontalSection(
                id = "section_numbers",
                bannerTitleRes = AppStrings.Roadmap.TOPIC_NUMBERS,
                iconType = TopicIconType.NUMBERS_123,
                isFreeBanner = false,
                wordsLearnedProgress = 0.22f,
                activities = listOf(
                    RoadmapActivityCard(
                        id = "num_bakery",
                        topicId = "section_numbers",
                        titleRes = AppStrings.Roadmap.ACTIVITY_NUMBER_BAKERY,
                        sceneType = ActivitySceneType.NUMBER_BAKERY_DONUTS,
                        isLocked = false,
                        isCompleted = "num_bakery" in completedIds,
                        tiltDegrees = -1.2f
                    ),
                    RoadmapActivityCard(
                        id = "num_kitchen_2",
                        topicId = "section_numbers",
                        titleRes = AppStrings.Roadmap.ACTIVITY_KITCHEN_FIND,
                        sceneType = ActivitySceneType.KITCHEN_MAGNIFIER_2,
                        isLocked = false,
                        isCompleted = "num_kitchen_2" in completedIds,
                        tiltDegrees = 1.1f
                    ),
                    RoadmapActivityCard(
                        id = "num_supermarket",
                        topicId = "section_numbers",
                        titleRes = AppStrings.Roadmap.ACTIVITY_SUPERMARKET,
                        sceneType = ActivitySceneType.SUPERMARKET_CART,
                        isLocked = true,
                        isCompleted = false,
                        tiltDegrees = 1.3f
                    ),
                    RoadmapActivityCard(
                        id = "num_mystery",
                        topicId = "section_numbers",
                        titleRes = AppStrings.Roadmap.ACTIVITY_ROOM_MYSTERY,
                        sceneType = ActivitySceneType.ROOM_MYSTERY_QUIZ,
                        isLocked = true,
                        isCompleted = false,
                        tiltDegrees = -1.0f
                    )
                )
            )
        )
    }
}
