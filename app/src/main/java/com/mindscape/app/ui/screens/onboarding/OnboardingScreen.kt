package com.mindscape.app.ui.screens.onboarding

import androidx.compose.foundation.background
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
import com.mindscape.app.ui.viewmodel.OnboardingViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinishOnboarding: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MindBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Step Header Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MindPrimaryAccentLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = MindPrimaryAccent)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MindSubtleContainer)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Step ${state.step} of 2",
                    style = MaterialTheme.typography.labelMedium,
                    color = MindTextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (state.step == 1) {
            // STEP 1: Begin Your MindScape
            Text(
                text = "Begin Your\nMindScape",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 38.sp
            )

            Text(
                text = "Create your mindful profile to start personalized mood tracking and gentle daily routines.",
                style = MaterialTheme.typography.bodyMedium,
                color = MindTextSecondary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name Field
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("What should we call you?", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = state.name,
                            onValueChange = viewModel::onNameChange,
                            placeholder = { Text("Your preferred name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    }

                    // Email Field
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Email Address", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = viewModel::onEmailChange,
                            placeholder = { Text("hello@example.com") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    }

                    // Primary Focus Selection
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Primary Focus for MindScape", style = MaterialTheme.typography.labelLarge)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Mindfulness", "Reduce Stress", "Daily Habit", "Sleep Better").forEach { focus ->
                                val isSelected = focus == state.primaryFocus
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isSelected) MindPrimaryAccent else if (focus == "Reduce Stress") MindLimeAccent else MindSubtleContainer)
                                        .clickable { viewModel.onFocusSelect(focus) }
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = focus,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else MindTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Continue Button
                    Button(
                        onClick = viewModel::goToStep2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
                    ) {
                        Text("Create Account & Continue ->", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }
                }
            }
        } else {
            // STEP 2: Customise Your Routine
            Text(
                text = "Customise Your\nRoutine",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 38.sp
            )

            Text(
                text = "Pick 2-3 starter habits and choose when you'd like your gentle daily check-in.",
                style = MaterialTheme.typography.bodyMedium,
                color = MindTextSecondary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Choose Your Starter Habits", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Hydration", "Meditation", "Mindful Walk", "Read 10 Pages", "Gratitude Note").forEach { habit ->
                            val isSelected = state.starterHabits.contains(habit)
                            val bgColor = when {
                                isSelected && habit == "Hydration" -> MindLimeAccent
                                isSelected -> MindPrimaryAccent
                                else -> MindSubtleContainer
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(bgColor)
                                    .clickable { viewModel.toggleStarterHabit(habit) }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = habit,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected && habit != "Hydration") Color.White else MindTextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Divider(color = MindBorderColor)

                    Text("Daily Check-In Time", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MindSubtleContainer)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Time", style = MaterialTheme.typography.bodyLarge)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(state.checkInTime, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Finish CTA
                    Button(
                        onClick = { viewModel.completeOnboarding(onFinishOnboarding) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
                    ) {
                        Text("Finish & Enter MindScape", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }
                }
            }
        }
    }
}
