package com.kidsenglish.app.core.designsystem.icons

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Forest
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material.icons.rounded.SportsBasketball
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector
import com.kidsenglish.app.R

/**
 * Centralized registry of application icon references.
 *
 * Separates brand drawable icons, HUD navigation icons, and the 18 topic category icons
 * so that `Icons.*` references are never scattered across UI files.
 */
object AppIcons {

    // Splash Screen: Official application brand medallion icon (vector drawable)
    @DrawableRes val AppLogo: Int = R.drawable.ic_app_logo

    // Splash Screen: Twinkling star and sparkle icons
    val SparkleStar: ImageVector = Icons.Rounded.Star
    val MagicSparkle: ImageVector = Icons.Rounded.AutoAwesome

    // Horizontal Roadmap HUD Icons
    val GridMenu: ImageVector = Icons.Rounded.Apps
    val AdventureMap: ImageVector = Icons.Rounded.Map
    val SettingsGear: ImageVector = Icons.Rounded.Settings
    val PuzzlePiece: ImageVector = Icons.Rounded.Extension
    val CrownBadge: ImageVector = Icons.Rounded.WorkspacePremium
    val Padlock: ImageVector = Icons.Rounded.Lock
    val PlayAction: ImageVector = Icons.Rounded.PlayArrow
    val CloseAction: ImageVector = Icons.Rounded.Close
    val SoundWave: ImageVector = Icons.Rounded.VolumeUp
    val MusicNote: ImageVector = Icons.Rounded.MusicNote
    val Microphone: ImageVector = Icons.Rounded.Mic
    val MagnifyingGlass: ImageVector = Icons.Rounded.Search

    // 18 Topic Category Icons (matching Screenshot 1 3x6 grid)
    val TopicAbc: ImageVector = Icons.Rounded.SortByAlpha
    val TopicColours: ImageVector = Icons.Rounded.Palette
    val TopicNumbers: ImageVector = Icons.Rounded.Tag
    val TopicClothes: ImageVector = Icons.Rounded.Checkroom
    val TopicPets: ImageVector = Icons.Rounded.Pets
    val TopicZoo: ImageVector = Icons.Rounded.Forest
    val TopicSweets: ImageVector = Icons.Rounded.Cake
    val TopicFruits: ImageVector = Icons.Rounded.Eco
    val TopicCity: ImageVector = Icons.Rounded.LocationCity
    val TopicSchool: ImageVector = Icons.Rounded.School
    val TopicFood: ImageVector = Icons.Rounded.Restaurant
    val TopicSports: ImageVector = Icons.Rounded.SportsBasketball
    val TopicFamily: ImageVector = Icons.Rounded.FamilyRestroom
    val TopicActions: ImageVector = Icons.Rounded.DirectionsRun
    val TopicHouse: ImageVector = Icons.Rounded.Home
    val TopicFurniture: ImageVector = Icons.Rounded.Chair
    val TopicCalendar: ImageVector = Icons.Rounded.CalendarMonth
    val TopicBody: ImageVector = Icons.Rounded.Face
    val TopicFastFood: ImageVector = Icons.Rounded.Fastfood
}
