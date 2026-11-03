package com.kidsenglish.app.core.constants

/**
 * Centralized application-wide constants for configuration, animation timing,
 * preferences storage, and UI test tags.
 */
object AppConstants {

    // DataStore Preferences file name used by AppPreferencesDataSource
    const val PREFERENCES_NAME = "kids_english_preferences"

    // Daily sticker goal shown in the Horizontal Roadmap HUD ("0/6")
    const val DAILY_STICKER_TARGET = 6

    // Splash Screen: Entrance animation duration in milliseconds
    const val SPLASH_ENTRANCE_DURATION_MS = 650

    // Splash Screen: Gentle floating bob animation duration in milliseconds
    const val SPLASH_FLOAT_ANIMATION_DURATION_MS = 1600

    // Splash Screen: Shimmering star rotation duration in milliseconds
    const val SPLASH_STAR_PULSE_DURATION_MS = 1300

    // Splash Screen: Initialization step delay before unlocking roadmap navigation
    const val SPLASH_INIT_STEP_DELAY_MS = 450L

    // Roadmap Animations: Card floating, rocking, and character animation durations
    const val CARD_FLOAT_DURATION_MS = 1800
    const val CARD_TILT_ROCK_DURATION_MS = 2200
    const val CHAR_BOUNCE_DURATION_MS = 1400
    const val CHAR_WAVE_DURATION_MS = 950
    const val WATERMARK_CHAR_FLOAT_DURATION_MS = 2800
    const val PROGRESS_ANIMATION_DURATION_MS = 700

    // UI Test Tags for deterministic Robolectric & Compose testing
    object TestTags {
        // Splash Screen Test Tags
        const val SPLASH_SCREEN_ROOT = "splash_screen_root"
        const val SPLASH_BADGE = "splash_badge"
        const val SPLASH_MASCOT = "splash_mascot"
        const val SPLASH_TITLE = "splash_title"
        const val SPLASH_SUBTITLE = "splash_subtitle"
        const val SPLASH_LOADING_INDICATOR = "splash_loading_indicator"

        // Horizontal Roadmap Screen Test Tags
        const val ROADMAP_SCREEN_ROOT = "roadmap_screen_root"
        const val HUD_GRID_TOGGLE_BUTTON = "hud_grid_toggle_button"
        const val HUD_SETTINGS_BUTTON = "hud_settings_button"
        const val HUD_STICKERS_BADGE = "hud_stickers_badge"
        const val HUD_MODE_RIBBON_BUTTON = "hud_mode_ribbon_button"
        const val TOPICS_GRID_CONTAINER = "topics_grid_container"
        const val HORIZONTAL_ROADMAP_ROW = "horizontal_roadmap_row"
    }
}
