package com.mindscape.app.ui.screens.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindscape.app.domain.model.Habit
import com.mindscape.app.domain.model.HabitCategory
import com.mindscape.app.ui.theme.*
import com.mindscape.app.ui.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    viewModel: HabitViewModel,
    onNavigateHome: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MindBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Top Title & Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Your Habits",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MindTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cultivate focus and tranquility through gentle consistency.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MindTextSecondary
                        )
                    }

                    // Smart Routine Quick Trigger Button in Top Header
                    IconButton(
                        onClick = viewModel::openSmartRoutineDialog,
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(elevation = 4.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(MindPrimaryAccentLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Smart Routine AI",
                            tint = MindPrimaryAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Card 1: Cultivate a New Habit Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MindLimeAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = MindLimeAccentDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "Cultivate a New Habit",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Habit Name Input Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Habit Name",
                                style = MaterialTheme.typography.labelLarge,
                                color = MindTextPrimary
                            )
                            OutlinedTextField(
                                value = state.newHabitName,
                                onValueChange = viewModel::onNameChange,
                                placeholder = { Text("e.g., Morning Journaling", color = MindTextMuted) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MindInputBackground,
                                    unfocusedContainerColor = MindInputBackground,
                                    focusedBorderColor = MindPrimaryAccent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                        }

                        // Category Selector Pills
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Category",
                                style = MaterialTheme.typography.labelLarge,
                                color = MindTextPrimary
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(HabitCategory.entries) { category ->
                                    val isSelected = category == state.selectedCategory
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) MindPrimaryAccent else MindSubtleContainer)
                                            .clickable { viewModel.onCategorySelect(category) }
                                            .padding(horizontal = 16.dp, vertical = 10.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            val icon = when (category) {
                                                HabitCategory.MINDFULNESS -> Icons.Default.SelfImprovement
                                                HabitCategory.MOVEMENT -> Icons.Default.DirectionsWalk
                                                HabitCategory.WELLNESS -> Icons.Default.WaterDrop
                                                HabitCategory.LEARNING -> Icons.Default.MenuBook
                                            }
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = if (isSelected) Color.White else MindTextSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = category.displayName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else MindTextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Frequency & Target Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Frequency", style = MaterialTheme.typography.labelLarge)
                                var expanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded }
                                ) {
                                    OutlinedTextField(
                                        value = state.selectedFrequency,
                                        onValueChange = {},
                                        readOnly = true,
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                        modifier = Modifier.menuAnchor(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = MindInputBackground,
                                            unfocusedContainerColor = MindInputBackground,
                                            focusedBorderColor = MindPrimaryAccent,
                                            unfocusedBorderColor = Color.Transparent
                                        )
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        listOf("Daily", "Weekdays Only", "Custom").forEach { freq ->
                                            DropdownMenuItem(
                                                text = { Text(freq) },
                                                onClick = {
                                                    viewModel.onFrequencySelect(freq)
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Target Goal", style = MaterialTheme.typography.labelLarge)
                                OutlinedTextField(
                                    value = state.targetValueText,
                                    onValueChange = viewModel::onTargetValueChange,
                                    placeholder = { Text("15") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MindInputBackground,
                                        unfocusedContainerColor = MindInputBackground,
                                        focusedBorderColor = MindPrimaryAccent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    singleLine = true
                                )
                            }
                        }

                        // Add Habit Button
                        Button(
                            onClick = viewModel::addHabit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add Habit",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Card 2: Weekly Consistency Stats Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Weekly Consistency",
                                style = MaterialTheme.typography.labelLarge,
                                color = MindTextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${state.weeklyConsistencyPercentage}%",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MindPrimaryAccent,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MindPrimaryAccentLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = MindPrimaryAccent,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Section Title: Current Focus
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Focus",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextTextButton(text = "Smart Generator", onClick = viewModel::openSmartRoutineDialog)
                }
            }

            // Habit Cards List
            items(state.habits) { habit ->
                HabitItemCard(
                    habit = habit,
                    onToggleComplete = { viewModel.toggleHabitCompletion(habit) },
                    onIncrement = { viewModel.incrementHabit(habit) },
                    onEdit = { viewModel.startEditingHabit(habit) },
                    onDelete = { viewModel.deleteHabit(habit) }
                )
            }

            // Dotted Add Another Focus Area Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.5.dp,
                            color = MindBorderColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.openSmartRoutineDialog() }
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MindPrimaryAccent
                        )
                        Text(
                            text = "Generate Routine with AI Heuristics",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MindPrimaryAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // --- SEPARATE FLOATING ACTION BUTTON FOR SMART ROUTINE GENERATOR ---
        ExtendedFloatingActionButton(
            onClick = viewModel::openSmartRoutineDialog,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(28.dp)),
            containerColor = MindPrimaryAccent,
            contentColor = Color.White,
            shape = RoundedCornerShape(28.dp),
            icon = {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Smart Routine",
                    tint = Color.White
                )
            },
            text = {
                Text(
                    text = "Smart Routine",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        )
    }

    // Smart Routine Generator Modal Dialog
    SmartRoutineDialog(
        isOpen = state.isSmartRoutineDialogOpen,
        promptText = state.smartRoutinePrompt,
        detectedIntent = state.detectedIntent,
        isGenerating = state.isGeneratingRoutine,
        lastResult = state.lastGeneratedResult,
        errorMessage = state.smartRoutineError,
        onPromptChange = viewModel::onSmartRoutinePromptChange,
        onGenerate = { prompt -> viewModel.generateSmartRoutine(prompt) },
        onDismiss = viewModel::dismissSmartRoutineDialog,
        onClearResult = viewModel::clearLastGeneratedResult
    )

    // Edit Modal Dialog if active
    state.editingHabit?.let { editing ->
        EditHabitDialog(
            habit = editing,
            onDismiss = viewModel::dismissEditModal,
            onSave = viewModel::saveEditedHabit
        )
    }
}

@Composable
fun HabitItemCard(
    habit: Habit,
    onToggleComplete: () -> Unit,
    onIncrement: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MindCardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Category Icon Badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            when (habit.category) {
                                HabitCategory.MINDFULNESS -> MindLimeAccent
                                HabitCategory.MOVEMENT -> MindPrimaryAccentLight
                                HabitCategory.WELLNESS -> Color(0xFFE0F2FE)
                                HabitCategory.LEARNING -> Color(0xFFFEF3C7)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (habit.category) {
                            HabitCategory.MINDFULNESS -> Icons.Default.SelfImprovement
                            HabitCategory.MOVEMENT -> Icons.Default.DirectionsWalk
                            HabitCategory.WELLNESS -> Icons.Default.WaterDrop
                            HabitCategory.LEARNING -> Icons.Default.MenuBook
                        },
                        contentDescription = null,
                        tint = MindPrimaryAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Category pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MindLimeAccent)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = habit.category.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MindTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Progress indicator pill
                        Text(
                            text = "${habit.currentValue}/${habit.targetValue} ${habit.targetUnit}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MindTextSecondary
                        )
                    }
                }
            }

            // Action buttons row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit", tint = MindTextMuted)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = MindTextMuted)
                }

                if (habit.targetUnit == "glasses" || habit.targetValue > 1) {
                    // Increment Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MindBorderColor, CircleShape)
                            .clickable { onIncrement() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increment",
                            tint = MindTextPrimary
                        )
                    }
                } else {
                    // Toggle Checkmark Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (habit.isCompleted) MindPrimaryAccent else MindSubtleContainer)
                            .clickable { onToggleComplete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Complete",
                            tint = if (habit.isCompleted) Color.White else MindTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditHabitDialog(
    habit: Habit,
    onDismiss: () -> Unit,
    onSave: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf(habit.name) }
    var targetText by remember { mutableStateOf(habit.targetValue.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Habit", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    label = { Text("Target (${habit.targetUnit})") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val target = targetText.toIntOrNull() ?: habit.targetValue
                    onSave(name, target)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MindTextSecondary)
            }
        }
    )
}

@Composable
fun TextTextButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MindPrimaryAccent,
        modifier = Modifier.clickable { onClick() }
    )
}
