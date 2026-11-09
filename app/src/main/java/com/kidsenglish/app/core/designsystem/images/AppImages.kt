package com.kidsenglish.app.core.designsystem.images

import androidx.annotation.DrawableRes
import com.kidsenglish.app.R
import com.kidsenglish.app.domain.model.TopicIconType

/**
 * Centralized registry of application illustration and image drawable references.
 *
 * Prevents hardcoded `R.drawable.*` references inside Composable screens and
 * organizes visual assets by feature and topic theme.
 */
object AppImages {

    // Splash Screen: Main friendly fox mascot vector illustration
    @DrawableRes val SplashMascot: Int = R.drawable.img_splash_mascot

    // Splash Screen: Brand book-and-star logo emblem
    @DrawableRes val BrandEmblem: Int = R.drawable.ic_app_logo

    // Roadmap Screen: Main cartoon landscape background illustration with volcano, lake, path and palm trees
    @DrawableRes val RoadmapBackground: Int = R.drawable.img_roadmap_bg

    // Roadmap Screen: Animated 3D Letter 'A' with purple wizard hat illustration for the 1st unlocked lesson
    @DrawableRes val LetterAWizard: Int = R.drawable.img_letter_a_wizard

    // Roadmap Theme Pictures: Multi-layered vector scene illustrations for topic & activity cards
    @DrawableRes val ThemeAbc: Int = R.drawable.img_theme_abc
    @DrawableRes val ThemeAnimals: Int = R.drawable.img_theme_animals
    @DrawableRes val ThemeSweets: Int = R.drawable.img_theme_sweets
    @DrawableRes val ThemeCity: Int = R.drawable.img_theme_city
    @DrawableRes val ThemeHome: Int = R.drawable.img_theme_home
    @DrawableRes val ThemeSpace: Int = R.drawable.img_theme_space

    /**
     * Resolves a theme-based illustration drawable for any [TopicIconType].
     */
    @DrawableRes
    fun resolveTopicThemeDrawable(iconType: TopicIconType): Int = when (iconType) {
        TopicIconType.ABC_LETTERS,
        TopicIconType.COLOURS_PALETTE,
        TopicIconType.NUMBERS_123,
        TopicIconType.SCHOOL_CAP,
        TopicIconType.CALENDAR_GRID -> ThemeAbc

        TopicIconType.PETS_HOUSE,
        TopicIconType.ZOO_ELEPHANT -> ThemeAnimals

        TopicIconType.SWEET_CAKE,
        TopicIconType.FRUITS_APPLE,
        TopicIconType.FOOD_PLATE -> ThemeSweets

        TopicIconType.CITY_BUILDINGS,
        TopicIconType.SPORTS_BALLS,
        TopicIconType.ACTIONS_RUN -> ThemeCity

        TopicIconType.CLOTHES_SHIRT,
        TopicIconType.FAMILY_GROUP,
        TopicIconType.HOUSE_HOME,
        TopicIconType.FURNITURE_CHAIR -> ThemeHome

        TopicIconType.BODY_SENSES -> ThemeSpace
    }
}
