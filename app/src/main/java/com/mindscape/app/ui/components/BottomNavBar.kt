package com.mindscape.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mindscape.app.ui.theme.MindCardSurface
import com.mindscape.app.ui.theme.MindPrimaryAccent
import com.mindscape.app.ui.theme.MindTextMuted

enum class NavTab {
    HOME,
    HABITS,
    ANALYTICS,
    PROFILE
}

@Composable
fun BottomNavBar(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(34.dp)),
            shape = RoundedCornerShape(34.dp),
            color = MindCardSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Tab
                NavBarItem(
                    icon = if (currentTab == NavTab.HOME) Icons.Default.Home else Icons.Outlined.Home,
                    isSelected = currentTab == NavTab.HOME,
                    onClick = { onTabSelected(NavTab.HOME) }
                )

                // Habits Tab
                NavBarItem(
                    icon = if (currentTab == NavTab.HABITS) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                    isSelected = currentTab == NavTab.HABITS,
                    onClick = { onTabSelected(NavTab.HABITS) }
                )

                // Spacer for Center Floating FAB
                Spacer(modifier = Modifier.width(48.dp))

                // Analytics Tab
                NavBarItem(
                    icon = if (currentTab == NavTab.ANALYTICS) Icons.Default.AutoAwesome else Icons.Outlined.AutoAwesome,
                    isSelected = currentTab == NavTab.ANALYTICS,
                    onClick = { onTabSelected(NavTab.ANALYTICS) }
                )

                // Profile Tab
                NavBarItem(
                    icon = if (currentTab == NavTab.PROFILE) Icons.Default.Person else Icons.Outlined.Person,
                    isSelected = currentTab == NavTab.PROFILE,
                    onClick = { onTabSelected(NavTab.PROFILE) }
                )
            }
        }

        // Floating Action Button (+) centered over the bar
        Box(
            modifier = Modifier
                .offset(y = (-14).dp)
                .size(56.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(MindPrimaryAccent)
                .clickable { onFabClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Quick Check-In",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun NavBarItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MindPrimaryAccent else MindTextMuted,
            modifier = Modifier.size(24.dp)
        )
    }
}
