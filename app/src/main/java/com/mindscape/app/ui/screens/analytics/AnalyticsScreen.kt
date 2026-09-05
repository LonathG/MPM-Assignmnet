package com.mindscape.app.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindscape.app.ui.theme.*
import com.mindscape.app.ui.viewmodel.AnalyticsViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onNavigateToProfile: () -> Unit = {}
) {
    val summary by viewModel.summary.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MindBackground)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top App Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MindCardSurface)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MindTextPrimary)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MindCardSurface)
                    ) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = MindTextPrimary)
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFDE047)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Profile",
                            tint = MindTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // Title Header
        item {
            Column {
                Text(
                    text = "Insights",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gentle patterns from your past 30 days.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MindTextSecondary
                )
            }
        }

        // Card 1: WEEKLY OBSERVATION Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MindPrimaryAccentLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = MindPrimaryAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "WEEKLY OBSERVATION",
                            style = MaterialTheme.typography.labelLarge,
                            color = MindTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = summary.weeklyObservation,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MindTextPrimary,
                        lineHeight = 26.sp
                    )
                }
            }
        }

        // Card 2: CONSISTENCY 85% Dot Matrix Heatmap Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CONSISTENCY",
                                style = MaterialTheme.typography.labelMedium,
                                color = MindTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${summary.overallConsistency}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontSize = 36.sp,
                                    color = MindPrimaryAccent,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "%",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MindPrimaryAccent,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            Text(
                                text = "Overall completion rate",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MindTextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MindPrimaryAccentLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.TrendingUp,
                                contentDescription = null,
                                tint = MindPrimaryAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 7-Day Matrix Heatmap (Dot Grid matching Image 8)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val days = listOf("S", "M", "T", "W", "T", "F", "S")
                        days.forEachIndexed { index, day ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MindTextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                                // Vertical column of 3 dots per day
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val activeCount = when (index) {
                                        0 -> 1
                                        1, 2, 3 -> 3
                                        4 -> 2
                                        5 -> 2
                                        6 -> 3
                                        else -> 2
                                    }
                                    for (dotIndex in 2 downTo 0) {
                                        val isActive = dotIndex < activeCount
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(CircleShape)
                                                .background(if (isActive) MindPrimaryAccent else MindSubtleContainer)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Card 3: Mood Impact - Habit completion by emotion (Bar Chart)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Mood Impact",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Habit completion by emotion",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MindTextSecondary
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = MindTextMuted)
                        }
                    }

                    // 4 Column Bar Chart (Happy, Sad, Calm, Anxious)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val moodBars = listOf(
                            Triple("Happy", 0.75f, MindPrimaryAccent),
                            Triple("Sad", 0.40f, Color(0xFF2D3748)),
                            Triple("Calm", 0.85f, MindLimeAccent),
                            Triple("Anxious", 0.55f, MindMagentaTag)
                        )

                        moodBars.forEach { (emotion, fillRatio, color) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                // Container bar column
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .width(48.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(MindSubtleContainer),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    // Filled inner bar pill
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(fillRatio)
                                            .clip(RoundedCornerShape(24.dp))
                                            .background(color)
                                    )
                                }

                                Text(
                                    text = emotion,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MindTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
