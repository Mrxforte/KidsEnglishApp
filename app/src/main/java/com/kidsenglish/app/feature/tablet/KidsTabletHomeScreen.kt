package com.kidsenglish.app.feature.tablet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kidsenglish.app.core.audio.AudioEffect
import com.kidsenglish.app.core.audio.AudioManager
import com.kidsenglish.app.core.designsystem.color.AppColors
import com.kidsenglish.app.core.designsystem.dimens.Dimens

data class TabletAppItem(
    val id: String,
    val title: String,
    val emoji: String,
    val cardColor: Color,
    val routeKey: String
)

/**
 * Interactive Kids Toy Tablet Home Screen styled exactly like the reference image:
 * Yellow bear-eared tablet frame enclosing a bright sky-blue screen with 12 delightful app cards,
 * status bar, funny sound effects, and balloon animation.
 */
@Composable
fun KidsTabletHomeScreen(
    audioManager: AudioManager,
    onAppSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Start cheerful background music
    LaunchedEffect(Unit) {
        audioManager.startMusic()
    }

    val apps = listOf(
        TabletAppItem("phone", "Telefon", "📞", Color(0xFFE3F2FD), "phone"),
        TabletAppItem("chat", "Suhbat", "💬", Color(0xFFFFFDE7), "chat"),
        TabletAppItem("art", "Chizma", "🎨", Color(0xFFFFEBEE), "art"),
        TabletAppItem("cups", "Top top", "🥤", Color(0xFFF3E5F5), "cups"),
        TabletAppItem("rockets", "Raketa", "🚀", Color(0xFFE0F7FA), "rockets"),
        TabletAppItem("animals", "Hayvonlar", "🦁", Color(0xFFFFF3E0), "animals"),
        TabletAppItem("shapes", "Shakllar", "🔺", Color(0xFFE8F5E9), "shapes"),
        TabletAppItem("abc", "ABC Sayohat", "🔤", Color(0xFFFFF9C4), "abc_roadmap"), // Opens 3D Roadmap!
        TabletAppItem("balloons", "Shar Portlatish", "🎈", Color(0xFFFFEBEE), "balloons"),
        TabletAppItem("cars", "Moshinlar", "🚗", Color(0xFFE1F5FE), "cars"),
        TabletAppItem("puzzles", "Topishmoq", "🧩", Color(0xFFEDE7F6), "puzzles"),
        TabletAppItem("fruits", "Mevalar", "🍉", Color(0xFFE8F5E9), "fruits")
    )

    // Floating balloon bounce animation
    val infiniteTransition = rememberInfiniteTransition(label = "balloonBounce")
    val balloonOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "balloonOffset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFBA68C8), // Deep purple starry background
                        Color(0xFF7B1FA2),
                        Color(0xFF4A148C)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Starry background sparkles
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(Color.White.copy(alpha = 0.6f), radius = 3.dp.toPx(), center = Offset(size.width * 0.15f, size.height * 0.1f))
            drawCircle(Color.White.copy(alpha = 0.8f), radius = 4.dp.toPx(), center = Offset(size.width * 0.85f, size.height * 0.15f))
            drawCircle(Color.White.copy(alpha = 0.5f), radius = 2.5.dp.toPx(), center = Offset(size.width * 0.2f, size.height * 0.8f))
            drawCircle(Color.White.copy(alpha = 0.7f), radius = 3.5.dp.toPx(), center = Offset(size.width * 0.8f, size.height * 0.85f))
        }

        // 🐻 Bear-Eared Yellow Tablet Frame
        Surface(
            shape = RoundedCornerShape(42.dp),
            color = Color(0xFFFFC107), // Golden yellow bear frame
            shadowElevation = 24.dp,
            modifier = Modifier
                .fillMaxSize(0.94f)
                .padding(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Bear Ears on top corners
                Canvas(modifier = Modifier.matchParentSize()) {
                    val earRadius = 54.dp.toPx()
                    // Top-Left Ear
                    drawCircle(Color(0xFFFFB300), radius = earRadius, center = Offset(50f, 20f))
                    drawCircle(Color(0xFFFFD54F), radius = earRadius * 0.65f, center = Offset(50f, 20f))
                    // Top-Right Ear
                    drawCircle(Color(0xFFFFB300), radius = earRadius, center = Offset(size.width - 50f, 20f))
                    drawCircle(Color(0xFFFFD54F), radius = earRadius * 0.65f, center = Offset(size.width - 50f, 20f))
                }

                // Inner Screen Container (Bright Sky Blue)
                Surface(
                    shape = RoundedCornerShape(34.dp),
                    color = Color(0xFF4FC3F7),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 22.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        // Tablet Status Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "10:15",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NetworkCell,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.BatteryFull,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // 4x3 Grid of App Cards matching reference image
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(apps) { app ->
                                TabletAppCard(
                                    app = app,
                                    onClick = {
                                        audioManager.playSfx(AudioEffect.POP)
                                        audioManager.playSfx(AudioEffect.CORRECT)
                                        onAppSelected(app.routeKey)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Red Balloon at bottom right corner just like reference image
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-24).dp, y = balloonOffsetY.dp)
                .size(72.dp)
                .shadow(12.dp, CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFF8A80), Color(0xFFD32F2F)),
                        center = Offset(20f, 20f),
                        radius = 80f
                    ),
                    CircleShape
                )
                .border(3.dp, Color.White, CircleShape)
                .clickable {
                    audioManager.playSfx(AudioEffect.STAR)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🎈", fontSize = 36.sp)
        }
    }
}

@Composable
fun TabletAppCard(
    app: TabletAppItem,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .testTag("tablet_app_${app.id}")
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(app.cardColor, Color.White)
                    )
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = app.emoji,
                    fontSize = 42.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = app.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}
