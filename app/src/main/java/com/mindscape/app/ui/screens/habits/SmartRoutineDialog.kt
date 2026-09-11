package com.mindscape.app.ui.screens.habits

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mindscape.app.domain.engine.RoutineGenerationResult
import com.mindscape.app.domain.engine.RoutineIntent
import com.mindscape.app.ui.theme.*

private data class SuggestionPrompt(
    val title: String,
    val icon: String,
    val prompt: String
)

private val suggestionPrompts = listOf(
    SuggestionPrompt(title = "Low Energy", icon = "⚡", prompt = "Create a daily routine for low energy and fatigue"),
    SuggestionPrompt(title = "Deep Focus", icon = "🎯", prompt = "Focus and study routine for exams and productivity"),
    SuggestionPrompt(title = "Stress Relief", icon = "🧘", prompt = "Calm mindfulness routine for anxiety and stress relief"),
    SuggestionPrompt(title = "Fitness Boost", icon = "🏃", prompt = "High energy workout and physical fitness routine"),
    SuggestionPrompt(title = "Sleep & Wind Down", icon = "🌙", prompt = "Evening routine to relax and improve sleep"),
    SuggestionPrompt(title = "Balanced Daily", icon = "🌿", prompt = "Balanced daily wellness routine")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartRoutineDialog(
    isOpen: Boolean,
    promptText: String,
    detectedIntent: RoutineIntent?,
    isGenerating: Boolean,
    lastResult: RoutineGenerationResult?,
    errorMessage: String?,
    onPromptChange: (String) -> Unit,
    onGenerate: (String?) -> Unit,
    onDismiss: () -> Unit,
    onClearResult: () -> Unit
) {
    if (!isOpen) return

    Dialog(
        onDismissRequest = { if (!isGenerating) onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MindCardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Sparkle Icon and Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(MindPrimaryAccentLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MindPrimaryAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Smart Routine Generator",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MindTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Offline Heuristic Routine Engine",
                                style = MaterialTheme.typography.labelSmall,
                                color = MindTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MindTextMuted
                        )
                    }
                }

                // If habits were just generated, show result summary
                if (lastResult != null) {
                    RoutineSuccessView(
                        result = lastResult,
                        onAddMore = onClearResult,
                        onDone = onDismiss
                    )
                } else {
                    // Prompt Input
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "What routine do you need today?",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MindTextPrimary
                        )

                        OutlinedTextField(
                            value = promptText,
                            onValueChange = onPromptChange,
                            placeholder = {
                                Text(
                                    text = "e.g., Create a routine for low energy or burnout...",
                                    color = MindTextMuted,
                                    fontSize = 14.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 84.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MindInputBackground,
                                unfocusedContainerColor = MindInputBackground,
                                focusedBorderColor = MindPrimaryAccent,
                                unfocusedBorderColor = MindBorderColor
                            ),
                            maxLines = 3
                        )
                    }

                    // Live Detected Intent Badge
                    AnimatedVisibility(
                        visible = detectedIntent != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        detectedIntent?.let { intent ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MindLimeAccent.copy(alpha = 0.7f))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = MindLimeAccentDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Matched Intent: ${intent.title}",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MindTextPrimary
                                        )
                                        Text(
                                            text = intent.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MindTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Quick Suggestion Chips Row
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Quick Presets",
                            style = MaterialTheme.typography.labelMedium,
                            color = MindTextSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(suggestionPrompts) { suggestion ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MindSubtleContainer)
                                        .border(1.dp, MindBorderColor, RoundedCornerShape(16.dp))
                                        .clickable {
                                            onPromptChange(suggestion.prompt)
                                            onGenerate(suggestion.prompt)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = suggestion.icon, fontSize = 14.sp)
                                        Text(
                                            text = suggestion.title,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MindTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Error message if any
                    errorMessage?.let { err ->
                        Text(
                            text = err,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Generate Button
                    Button(
                        onClick = { onGenerate(null) },
                        enabled = !isGenerating && promptText.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MindPrimaryAccent,
                            disabledContainerColor = MindPrimaryAccent.copy(alpha = 0.4f)
                        )
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Analyzing & Generating...",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate Routine & Save",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineSuccessView(
    result: RoutineGenerationResult,
    onAddMore: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(MindLimeAccent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MindLimeAccentDark,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = "Routine Created & Saved!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MindTextPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Added ${result.insertedCount} habits for '${result.intent.title}' directly to your local database.",
            style = MaterialTheme.typography.bodyMedium,
            color = MindTextSecondary,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        // List of created habits with proper clean alignment
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MindSubtleContainer)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            result.createdHabits.forEach { habit ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = MindPrimaryAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = habit.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MindTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${habit.targetValue} ${habit.targetUnit}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MindTextSecondary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onAddMore,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MindPrimaryAccent)
            ) {
                Text(
                    text = "Generate Another",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onDone,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MindPrimaryAccent)
            ) {
                Text(
                    text = "Done",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
