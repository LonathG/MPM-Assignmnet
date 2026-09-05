package com.mindscape.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WbSunny
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
import com.mindscape.app.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    val profile by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MindBackground)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Navigation Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MindCardSurface)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MindTextPrimary)
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MindCardSurface)
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = MindTextPrimary)
                }
            }
        }

        // Profile Avatar & User Info Card
        item {
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
                    // Avatar Thumbnail with Yellow Border
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFDE047))
                            .border(3.dp, MindLimeAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Avatar",
                            tint = MindTextPrimary,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    // User Name
                    Text(
                        text = profile.userName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Level Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MindLimeAccent)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = MindLimeAccentDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Mindful Explorer • Level ${profile.level}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MindLimeAccentDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Divider(color = MindBorderColor, modifier = Modifier.padding(vertical = 4.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(value = "${profile.streakDays}", label = "Days Streak")
                        StatDivider()
                        StatItem(value = "${profile.consistencyPercentage}%", label = "Consistency")
                        StatDivider()
                        StatItem(value = "${profile.completedSessions}", label = "Sessions")
                    }
                }
            }
        }

        // DAILY ROUTINE SETTINGS Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "DAILY ROUTINE SETTINGS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MindTextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    // Morning Check-In Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MindSubtleContainer)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MindLimeAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.WbSunny, contentDescription = null, tint = MindLimeAccentDark)
                            }

                            Column {
                                Text("Morning Check-In", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(profile.checkInTime, style = MaterialTheme.typography.bodyMedium, color = MindTextSecondary)
                            }
                        }

                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MindTextMuted)
                    }

                    // Smart Adaptive Goals Toggle Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MindSubtleContainer)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MindPrimaryAccentLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.Tune, contentDescription = null, tint = MindPrimaryAccent)
                            }

                            Column {
                                Text("Smart Adaptive Goals", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Auto-adjust targets", style = MaterialTheme.typography.bodyMedium, color = MindTextSecondary)
                            }
                        }

                        Switch(
                            checked = profile.smartAdaptiveGoalsEnabled,
                            onCheckedChange = viewModel::onToggleSmartAdaptiveGoals,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MindPrimaryAccent
                            )
                        )
                    }
                }
            }
        }

        // ACCOUNT Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MindCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "ACCOUNT",
                        style = MaterialTheme.typography.labelLarge,
                        color = MindTextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Outlined.Lock, contentDescription = null, tint = MindTextSecondary)
                            Text("Offline & Local Storage", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MindSubtleContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Encrypted", style = MaterialTheme.typography.labelSmall, color = MindTextSecondary)
                        }
                    }

                    Divider(color = MindBorderColor)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSignOut() },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFEF4444))
                        Text("Sign Out", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MindPrimaryAccent
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MindTextSecondary
        )
    }
}

@Composable
fun StatDivider() {
    Divider(
        modifier = Modifier
            .height(36.dp)
            .width(1.dp),
        color = MindBorderColor
    )
}
