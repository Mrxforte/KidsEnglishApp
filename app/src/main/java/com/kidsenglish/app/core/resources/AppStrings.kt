package com.kidsenglish.app.core.resources

import androidx.annotation.StringRes
import com.kidsenglish.app.R

/**
 * Centralized registry of all user-facing string resource identifiers.
 *
 * Ensures zero hardcoded text inside Composable functions and organizes string
 * references logically by feature while supporting Uzbek, English, and Russian localization.
 */
object AppStrings {

    /**
     * Global application identity strings.
     */
    object Global {
        // Global: Official application name displayed in system launcher and headers
        @StringRes val APP_NAME: Int = R.string.app_name
    }

    /**
     * Strings used by the Splash Screen feature.
     */
    object Splash {
        // Splash Screen: Main brand welcome title ("Kids English")
        @StringRes val WELCOME_TITLE: Int = R.string.splash_welcome

        // Splash Screen: Friendly educational subtitle below the main title
        @StringRes val SUBTITLE: Int = R.string.splash_subtitle

        // Splash Screen: Top decorative badge label for young learners
        @StringRes val BADGE_LABEL: Int = R.string.splash_badge

        // Splash Screen: Status text displayed while startup initialization is running
        @StringRes val LOADING_MESSAGE: Int = R.string.splash_loading

        // Splash Screen: Status text displayed when startup initialization completes
        @StringRes val READY_MESSAGE: Int = R.string.splash_ready

        // Splash Screen Accessibility: Content description for the central fox mascot illustration
        @StringRes val CD_MASCOT: Int = R.string.cd_splash_mascot

        // Splash Screen Accessibility: Content description for the application logo badge
        @StringRes val CD_LOGO: Int = R.string.cd_splash_logo
    }

    /**
     * Strings used by the Horizontal Levels Roadmap Template feature.
     */
    object Roadmap {
        // Roadmap HUD: Label for daily stickers counter ("Stickers today:")
        @StringRes val STICKERS_TODAY_LABEL: Int = R.string.roadmap_stickers_today

        // Roadmap HUD: Formatted counter for daily stickers ("%1$d/%2$d")
        @StringRes val STICKERS_COUNT_FORMAT: Int = R.string.roadmap_stickers_count

        // Roadmap Topic Banner: Label above the progress bar ("Words learned")
        @StringRes val WORDS_LEARNED_LABEL: Int = R.string.roadmap_words_learned

        // Roadmap Topic Banner: Free starter section title ("FREE")
        @StringRes val FREE_SECTION_TITLE: Int = R.string.roadmap_free_badge

        // Roadmap Top-Right Ribbon: Switch to all 18 topics 3x6 grid
        @StringRes val ALL_TOPICS_BADGE: Int = R.string.roadmap_all_topics_badge

        // Roadmap Top-Right Ribbon: Switch to horizontal adventure map
        @StringRes val ADVENTURE_BADGE: Int = R.string.roadmap_adventure_badge

        // Roadmap Feedback: Message shown when a locked card is tapped
        @StringRes val LOCKED_MESSAGE: Int = R.string.roadmap_locked_toast

        // Roadmap Feedback: Message shown when a sticker is earned
        @StringRes val STICKER_EARNED_MESSAGE: Int = R.string.roadmap_sticker_earned

        // Roadmap HUD Accessibility: Toggle between 3x6 grid and horizontal roadmap
        @StringRes val CD_GRID_TOGGLE: Int = R.string.cd_roadmap_grid_toggle

        // Roadmap HUD Accessibility: Settings hexagonal button
        @StringRes val CD_SETTINGS: Int = R.string.cd_roadmap_settings

        // Roadmap HUD Accessibility: Stickers ribbon badge
        @StringRes val CD_STICKERS: Int = R.string.cd_roadmap_stickers

        // Roadmap Card Accessibility: Padlock badge on locked cards
        @StringRes val CD_LOCKED: Int = R.string.cd_roadmap_locked

        // 18 Topic Categories in the 3x6 Horizontal Grid Template
        @StringRes val TOPIC_ABC: Int = R.string.topic_abc
        @StringRes val TOPIC_COLOURS: Int = R.string.topic_colours
        @StringRes val TOPIC_NUMBERS: Int = R.string.topic_numbers
        @StringRes val TOPIC_CLOTHES: Int = R.string.topic_clothes
        @StringRes val TOPIC_PETS: Int = R.string.topic_pets
        @StringRes val TOPIC_ZOO: Int = R.string.topic_zoo
        @StringRes val TOPIC_SWEETS: Int = R.string.topic_sweets
        @StringRes val TOPIC_FRUITS: Int = R.string.topic_fruits
        @StringRes val TOPIC_CITY: Int = R.string.topic_city
        @StringRes val TOPIC_SCHOOL: Int = R.string.topic_school
        @StringRes val TOPIC_FOOD: Int = R.string.topic_food
        @StringRes val TOPIC_SPORTS: Int = R.string.topic_sports
        @StringRes val TOPIC_FAMILY: Int = R.string.topic_family
        @StringRes val TOPIC_ACTIONS: Int = R.string.topic_actions
        @StringRes val TOPIC_HOUSE: Int = R.string.topic_house
        @StringRes val TOPIC_FURNITURE: Int = R.string.topic_furniture
        @StringRes val TOPIC_CALENDAR: Int = R.string.topic_calendar
        @StringRes val TOPIC_BODY: Int = R.string.topic_body

        // Illustrated Activity Cards in the Horizontal Roadmap
        @StringRes val ACTIVITY_ICE_CREAM_PAINT: Int = R.string.activity_ice_cream_paint
        @StringRes val ACTIVITY_SWEET_SHOP: Int = R.string.activity_sweet_shop
        @StringRes val ACTIVITY_ANIMAL_SHADOW: Int = R.string.activity_animal_shadow
        @StringRes val ACTIVITY_FUNNY_MONSTER: Int = R.string.activity_funny_monster
        @StringRes val ACTIVITY_YUMMY_TABLE: Int = R.string.activity_yummy_table
        @StringRes val ACTIVITY_SUPERMARKET: Int = R.string.activity_supermarket
        @StringRes val ACTIVITY_NUMBER_BAKERY: Int = R.string.activity_number_bakery
        @StringRes val ACTIVITY_ROOM_MYSTERY: Int = R.string.activity_room_mystery
        @StringRes val ACTIVITY_CITY_PATROL: Int = R.string.activity_city_patrol
        @StringRes val ACTIVITY_KITCHEN_FIND: Int = R.string.activity_kitchen_find
        @StringRes val ACTIVITY_VOICE_REPEAT: Int = R.string.activity_voice_repeat
        @StringRes val ACTIVITY_ABC_TRACING: Int = R.string.activity_abc_tracing
        @StringRes val ACTIVITY_CRANE_WORDS: Int = R.string.activity_crane_words
        @StringRes val ACTIVITY_COLOR_BUBBLES: Int = R.string.activity_color_bubbles
        @StringRes val ACTIVITY_SPELL_CONE: Int = R.string.activity_spell_cone

        // Interactive Modal & Settings Strings
        @StringRes val MODAL_START_ACTIVITY: Int = R.string.modal_start_activity
        @StringRes val MODAL_CLOSE: Int = R.string.modal_close
        @StringRes val SETTINGS_TITLE: Int = R.string.settings_title
        @StringRes val SETTINGS_SOUND_EFFECTS: Int = R.string.settings_sound_effects
        @StringRes val SETTINGS_BACKGROUND_MUSIC: Int = R.string.settings_background_music
    }
}
