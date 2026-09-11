package com.mindscape.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindscape.app.domain.model.Habit
import com.mindscape.app.ui.screens.checkin.WeatherIconItem
import com.mindscape.app.ui.theme.*
import com.mindscape.app.ui.viewmodel.CalendarDay
import com.mindscape.app.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

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

                    // Avatar Thumbnail
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFDE047))
                            .clickable { onNavigateToProfile() },
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

        // Greeting Header
        item {
            Column {
                Text(
                    text = "Good morning, ${state.userProfile.userName.split(" ").firstOrNull() ?: "Lonath"}.",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Take a deep breath. You're doing great.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MindTextSecondary
                )
            }
        }

        // Calendar Date Picker Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.calendarDays) { day ->
                    CalendarDayItem(
                        day = day,
                        onSelect = { viewModel.onSelectDay(day) }
                    )
                }
            }
        }

        // Quick Check-In Banner Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "How are you feeling today?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Weather Icons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WeatherIconItem(icon = Icons.Default.Thunderstorm, label = "Stormy", score = 1, currentScore = state.latestCheckIn?.moodScore ?: 3)
                        WeatherIconItem(icon = Icons.Default.Grain, label = "Rainy", score = 2, currentScore = state.latestCheckIn?.moodScore ?: 3)
                        WeatherIconItem(icon = Icons.Default.WbCloudy, label = "Okay", score = 3, currentScore = state.latestCheckIn?.moodScore ?: 3)
                        WeatherIconItem(icon = Icons.Default.WbSunny, label = "Sunny", score = 4, currentScore = state.latestCheckIn?.moodScore ?: 3)
                        WeatherIconItem(icon = Icons.Default.LightMode, label = "Radiant", score = 5, currentScore = state.latestCheckIn?.moodScore ?: 3)
                    }

                    // Check In Button
                    Button(
                        onClick = onNavigateToCheckIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (state.latestCheckIn != null) "Update Check-In" else "Check In",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }

        // Today's Adaptive Habits Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Adaptive Habits",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MindPrimaryAccentLight)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Tailored",
                        style = MaterialTheme.typography.labelSmall,
                        color = MindPrimaryAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Today's Habits Items List
        items(state.habits) { habit ->
            AdaptiveHabitCard(
                habit = habit,
                onToggleComplete = { viewModel.toggleHabitCompletion(habit) }
            )
        }
    }
}

@Composable
fun CalendarDayItem(
    day: CalendarDay,
    onSelect: () -> Unit
) {
    val isSelected = day.isSelected
    val isPast = day.isPast

    // Background color: Selected -> Primary Purple, Past -> Light subtle Gray, Today/Future -> Card Surface
    val backgroundColor = when {
        isSelected -> MindPrimaryAccent
        isPast -> Color(0xFFF1F3F5)
        else -> MindCardSurface
    }

    // Day name label color
    val dayNameColor = when {
        isSelected -> Color.White.copy(alpha = 0.85f)
        isPast -> Color(0xFFA0A7B1)
        day.isToday -> MindPrimaryAccent
        else -> MindTextSecondary
    }

    // Day number text color
    val dayNumberColor = when {
        isSelected -> Color.White
        isPast -> Color(0xFF8E95A2)
        day.isToday -> MindPrimaryAccent
        else -> MindTextPrimary
    }

    Box(
        modifier = Modifier
            .width(64.dp)
            .height(84.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .clickable { onSelect() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = day.dayName,
                style = MaterialTheme.typography.labelMedium,
                color = dayNameColor,
                fontWeight = if (day.isToday && !isSelected) FontWeight.Bold else FontWeight.Medium
            )
            Text(
                text = day.dayNumber.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = if (isSelected || day.isToday) FontWeight.ExtraBold else FontWeight.Bold,
                color = dayNumberColor
            )
        }
    }
}

@Composable
fun AdaptiveHabitCard(
    habit: Habit,
    onToggleComplete: () -> Unit
) {
    if (habit.isCompleted) {
        // Checked Item Style (Lime/Sage Card Surface)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleComplete() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindLimeAccent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MindTextPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = MindLimeAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MindTextPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    } else {
        // Unchecked Item Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindCardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Checkbox Circle
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MindSubtleContainer)
                            .clickable { onToggleComplete() },
                        contentAlignment = Alignment.Center
                    ) {
                        // Unchecked empty
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = habit.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MindTextPrimary
                        )
                    }

                    if (habit.isAdapted) {
                        // ENERGY-ADJUSTED Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MindLavenderTag)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ENERGY-ADJUSTED",
                                style = MaterialTheme.typography.labelSmall,
                                color = MindPrimaryAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (habit.isAdapted && !habit.adaptationReason.isNullOrEmpty()) {
                    Text(
                        text = habit.adaptationReason,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MindTextSecondary,
                        modifier = Modifier.padding(start = 46.dp)
                    )
                }
            }
        }
    }
}
