package com.mindscape.app.ui.screens.checkin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindscape.app.ui.theme.*
import com.mindscape.app.ui.viewmodel.CheckInViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel,
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    if (state.isCheckInComplete) {
        // Render Image 6: Check-In Completed Modal Screen
        CheckInCompleteView(
            state = state,
            onToggleFactor = viewModel::toggleFactor,
            onDone = {
                viewModel.dismissCompletionModal()
                onNavigateToHome()
            },
            onNoteChanged = viewModel::onQuickNoteChange
        )
    } else {
        // Render Interactive Mood & Energy Slider Card / Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MindBackground)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with Back & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateToHome,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MindCardSurface)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MindTextPrimary)
                }

                IconButton(
                    onClick = onNavigateToHome,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MindCardSurface)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MindTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Interactive Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "How are you feeling today?",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )

                    // Weather Icons Row above slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WeatherIconItem(icon = Icons.Default.Thunderstorm, label = "Stormy", score = 1, currentScore = state.moodScore)
                        WeatherIconItem(icon = Icons.Default.Grain, label = "Rainy", score = 2, currentScore = state.moodScore)
                        WeatherIconItem(icon = Icons.Default.WbCloudy, label = "Okay", score = 3, currentScore = state.moodScore)
                        WeatherIconItem(icon = Icons.Default.WbSunny, label = "Sunny", score = 4, currentScore = state.moodScore)
                        WeatherIconItem(icon = Icons.Default.LightMode, label = "Radiant", score = 5, currentScore = state.moodScore)
                    }

                    // Mood Selected Label
                    Text(
                        text = state.moodLabel,
                        style = MaterialTheme.typography.titleMedium,
                        color = MindPrimaryAccent,
                        fontWeight = FontWeight.Bold
                    )

                    // Interactive Slider
                    Slider(
                        value = state.sliderValue,
                        onValueChange = viewModel::onSliderValueChange,
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = MindPrimaryAccent,
                            activeTrackColor = MindPrimaryAccentLight,
                            inactiveTrackColor = MindSubtleContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Check In Action CTA
                    Button(
                        onClick = viewModel::submitCheckIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Check In",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckInCompleteView(
    state: com.mindscape.app.ui.viewmodel.CheckInUiState,
    onToggleFactor: (String) -> Unit,
    onDone: () -> Unit,
    onNoteChanged: (String) -> Unit
) {
    var showNoteInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MindBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card: CHECK-IN COMPLETE
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MindCardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MindLimeAccent)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "CHECK-IN COMPLETE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MindLimeAccentDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Illustration Box
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MindPrimaryAccentLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbCloudy,
                        contentDescription = null,
                        tint = MindPrimaryAccent,
                        modifier = Modifier.size(56.dp)
                    )
                }

                // Title
                Text(
                    text = "Mood Logged: ${state.moodLabel}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Thank you for checking in with yourself today. Gentle consistency leads to mindful living.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MindTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Section: WHAT'S CONTRIBUTING TODAY?
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindCardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "WHAT'S CONTRIBUTING TODAY?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MindTextPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Contributing Factor Pills
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val factors = listOf(
                        "Sleep" to "😴",
                        "Work" to "💼",
                        "Health" to "🏃",
                        "Social" to "👥",
                        "Solitude" to "🌿"
                    )

                    factors.forEach { (factorName, emoji) ->
                        val isSelected = state.selectedFactors.contains(factorName)
                        val bgColor = when {
                            isSelected && factorName == "Sleep" -> MindPrimaryAccent
                            isSelected && factorName == "Solitude" -> MindLimeAccent
                            isSelected -> MindPrimaryAccent
                            else -> MindSubtleContainer
                        }
                        val textColor = when {
                            isSelected && factorName == "Solitude" -> MindLimeAccentDark
                            isSelected -> Color.White
                            else -> MindTextPrimary
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(bgColor)
                                .clickable { onToggleFactor(factorName) }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "$emoji $factorName",
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Section: PLAN UPDATED (Smart Adaptation Card)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindCardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "PLAN UPDATED",
                    style = MaterialTheme.typography.labelLarge,
                    color = MindMagentaTag,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = state.adaptationResult?.summaryMessage
                        ?: "We've automatically eased your daily habits to match your current energy level.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MindTextPrimary
                )

                // Render adapted habit tag callouts
                state.adaptationResult?.adaptedHabits?.filter { it.isAdapted }?.forEach { habit ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MindLavenderTag)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsWalk,
                                contentDescription = null,
                                tint = MindPrimaryAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${habit.name} reduced to ${habit.targetValue} ${habit.targetUnit}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MindPrimaryAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Quick Note Expandable Input
        AnimatedVisibility(visible = showNoteInput) {
            OutlinedTextField(
                value = state.quickNote,
                onValueChange = onNoteChanged,
                placeholder = { Text("How are you feeling in detail...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
        }

        // CTA Button: Done & View Today's Plan
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
        ) {
            Text(
                text = "Done & View Today's Plan",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        TextButton(onClick = { showNoteInput = !showNoteInput }) {
            Text(
                text = if (showNoteInput) "Hide Note" else "Add a Quick Note",
                style = MaterialTheme.typography.labelLarge,
                color = MindTextSecondary
            )
        }
    }
}

@Composable
fun WeatherIconItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    score: Int,
    currentScore: Int
) {
    val isSelected = score == currentScore
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isSelected) MindPrimaryAccentLight else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MindPrimaryAccent else MindTextMuted,
                modifier = Modifier.size(26.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MindPrimaryAccent else MindTextMuted,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
