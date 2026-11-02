package com.kidsenglish.app.ui.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun MeningYolimScreen(
    onBackClick: () -> Unit = {},
    onLevelClick: (Int) -> Unit = {},
    totalStars: Int = 128,
    streakDays: Int = 7
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB), // och ko'k osmon
                        Color(0xFF98D8A0), // yashil
                        Color(0xFF7CB342)
                    )
                )
            )
    ) {
        // ===== TOP BAR =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
                .zIndex(10f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Orqaga tugma
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
                    .testTag("back_button")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Orqaga", tint = Color(0xFF2196F3))
            }

            // Yulduz va streak
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Yulduz
                Row(
                    modifier = Modifier
                        .background(Color(0xFFFFC107), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Yulduzlar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$totalStars", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(" +", color = Color.White, fontSize = 14.sp)
                }

                // Streak
                Row(
                    modifier = Modifier
                        .background(Color(0xFFFF5722), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Whatshot,
                        contentDescription = "Kunlik ketma-ketlik",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$streakDays kun", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // ===== SCROLLABLE CONTENT BODY =====
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // ===== SARLAVHA =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Mening yo‘lim",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            blurRadius = 8f
                        )
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sariq banner
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFFD54F), Color(0xFFFFB300))
                            ),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⭐ Bilim olamida oldinga sayohat! ⭐",
                        color = Color(0xFF5D4037),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // ===== YO‘L VA LEVELLAR =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(580.dp)
                    .padding(top = 16.dp, bottom = 40.dp)
            ) {
                // Level 1 - Apple (chap yuqori)
                LevelNode(
                    number = 1,
                    title = "Salomlashamiz!",
                    subtitle = "Apple",
                    emoji = "🍎",
                    isCompleted = true,
                    isLocked = false,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 40.dp, y = 20.dp),
                    onClick = { onLevelClick(1) }
                )

                // Level 2 - Ball (o‘ng yuqori)
                LevelNode(
                    number = 2,
                    title = "O‘yin va ranglar!",
                    subtitle = "Ball",
                    emoji = "🏐",
                    isCompleted = true,
                    isLocked = false,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-40).dp, y = 80.dp),
                    onClick = { onLevelClick(2) }
                )

                // Level 3 - Cat (o‘rtada)
                LevelNode(
                    number = 3,
                    title = "Men va hayvonlar",
                    subtitle = "Cat",
                    emoji = "🐱",
                    isCompleted = true,
                    isLocked = false,
                    isSpecial = true, // sariq porloq
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 50.dp, y = (-20).dp),
                    onClick = { onLevelClick(3) }
                )

                // Level 4 - Dog
                LevelNode(
                    number = 4,
                    title = "Do‘stlar dunyosi",
                    subtitle = "Dog",
                    emoji = "🐶",
                    isCompleted = false,
                    isLocked = false,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-30).dp, y = 60.dp),
                    onClick = { onLevelClick(4) }
                )

                // Level 5 - Locked
                LevelNode(
                    number = 5,
                    title = "Ovqat va taomlar",
                    subtitle = "(Yopiq)",
                    emoji = "🔒",
                    isCompleted = false,
                    isLocked = true,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 60.dp, y = (-80).dp),
                    onClick = { }
                )

                // Level 6 - Locked
                LevelNode(
                    number = 6,
                    title = "Maktab va kundalik",
                    subtitle = "(Yopiq)",
                    emoji = "🔒",
                    isCompleted = false,
                    isLocked = true,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-20).dp),
                    onClick = { }
                )
            }

            // Pastki dekorativ hayvonlar (oddiy)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🦊",
                    fontSize = 48.sp
                )
                Text(
                    text = "🐿️",
                    fontSize = 48.sp
                )
            }
        }
    }
}

@Composable
fun LevelNode(
    number: Int,
    title: String,
    subtitle: String,
    emoji: String,
    isCompleted: Boolean,
    isLocked: Boolean,
    isSpecial: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .testTag("level_node_$number")
            .clickable(enabled = !isLocked) { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(if (isSpecial) 90.dp else 80.dp)
                .shadow(8.dp, CircleShape)
                .background(
                    when {
                        isLocked -> Color(0xFF9E9E9E)
                        isSpecial -> Color(0xFFFFD54F)
                        isCompleted -> Color(0xFF4CAF50)
                        else -> Color(0xFF2196F3)
                    },
                    CircleShape
                )
                .border(
                    width = 4.dp,
                    color = Color.White,
                    shape = CircleShape
                )
        ) {
            if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Yopiq",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            } else {
                Text(text = emoji, fontSize = 36.sp)
            }

            // Checkmark
            if (isCompleted && !isLocked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .size(24.dp)
                        .background(Color(0xFF4CAF50), CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$number. $title",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20),
            textAlign = TextAlign.Center
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = Color(0xFF33691E),
            textAlign = TextAlign.Center
        )
    }
}
