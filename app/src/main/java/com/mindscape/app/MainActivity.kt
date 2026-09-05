package com.mindscape.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindscape.app.data.local.database.MindScapeDatabase
import com.mindscape.app.data.repository.CheckInRepositoryImpl
import com.mindscape.app.data.repository.HabitRepositoryImpl
import com.mindscape.app.data.repository.UserProfileRepositoryImpl
import com.mindscape.app.domain.engine.SmartAdaptationEngine
import com.mindscape.app.domain.usecase.GetAnalyticsUseCase
import com.mindscape.app.domain.usecase.ManageHabitsUseCase
import com.mindscape.app.domain.usecase.PerformCheckInUseCase
import com.mindscape.app.ui.components.BottomNavBar
import com.mindscape.app.ui.components.NavTab
import com.mindscape.app.ui.screens.analytics.AnalyticsScreen
import com.mindscape.app.ui.screens.checkin.CheckInScreen
import com.mindscape.app.ui.screens.habits.HabitsScreen
import com.mindscape.app.ui.screens.home.HomeScreen
import com.mindscape.app.ui.screens.onboarding.OnboardingScreen
import com.mindscape.app.ui.screens.profile.ProfileScreen
import com.mindscape.app.ui.theme.MindScapeTheme
import com.mindscape.app.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room DB and Repositories
        val database = MindScapeDatabase.getDatabase(applicationContext)
        val habitRepository = HabitRepositoryImpl(database.habitDao())
        val checkInRepository = CheckInRepositoryImpl(database.dailyCheckInDao())
        val userProfileRepository = UserProfileRepositoryImpl(database.userProfileDao())

        // Initialize Domain Use Cases
        val manageHabitsUseCase = ManageHabitsUseCase(habitRepository)
        val performCheckInUseCase = PerformCheckInUseCase(
            checkInRepository = checkInRepository,
            habitRepository = habitRepository,
            userProfileRepository = userProfileRepository,
            adaptationEngine = SmartAdaptationEngine()
        )
        val getAnalyticsUseCase = GetAnalyticsUseCase(
            habitRepository = habitRepository,
            checkInRepository = checkInRepository
        )

        setContent {
            MindScapeTheme {
                val navController = rememberNavController()

                // ViewModels
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.Factory(manageHabitsUseCase, checkInRepository, userProfileRepository)
                )
                val habitViewModel: HabitViewModel = viewModel(
                    factory = HabitViewModel.Factory(manageHabitsUseCase)
                )
                val checkInViewModel: CheckInViewModel = viewModel(
                    factory = CheckInViewModel.Factory(performCheckInUseCase)
                )
                val analyticsViewModel: AnalyticsViewModel = viewModel(
                    factory = AnalyticsViewModel.Factory(getAnalyticsUseCase)
                )
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModel.Factory(userProfileRepository)
                )
                val onboardingViewModel: OnboardingViewModel = viewModel(
                    factory = OnboardingViewModel.Factory(userProfileRepository)
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val currentTab = when (currentRoute) {
                    "home" -> NavTab.HOME
                    "habits" -> NavTab.HABITS
                    "analytics" -> NavTab.ANALYTICS
                    "profile" -> NavTab.PROFILE
                    else -> NavTab.HOME
                }

                val showBottomBar = currentRoute in listOf("home", "habits", "analytics", "profile")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(
                                currentTab = currentTab,
                                onTabSelected = { tab ->
                                    val destination = when (tab) {
                                        NavTab.HOME -> "home"
                                        NavTab.HABITS -> "habits"
                                        NavTab.ANALYTICS -> "analytics"
                                        NavTab.PROFILE -> "profile"
                                    }
                                    navController.navigate(destination) {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onFabClick = {
                                    navController.navigate("checkin")
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "home"
                        ) {
                            composable("home") {
                                HomeScreen(
                                    viewModel = homeViewModel,
                                    onNavigateToCheckIn = { navController.navigate("checkin") },
                                    onNavigateToProfile = { navController.navigate("profile") }
                                )
                            }
                            composable("habits") {
                                HabitsScreen(
                                    viewModel = habitViewModel,
                                    onNavigateHome = { navController.navigate("home") }
                                )
                            }
                            composable("analytics") {
                                AnalyticsScreen(
                                    viewModel = analyticsViewModel,
                                    onNavigateToProfile = { navController.navigate("profile") }
                                )
                            }
                            composable("profile") {
                                ProfileScreen(
                                    viewModel = profileViewModel,
                                    onNavigateBack = { navController.popBackStack() },
                                    onSignOut = { navController.navigate("onboarding") }
                                )
                            }
                            composable("checkin") {
                                CheckInScreen(
                                    viewModel = checkInViewModel,
                                    onNavigateToHome = { navController.navigate("home") }
                                )
                            }
                            composable("onboarding") {
                                OnboardingScreen(
                                    viewModel = onboardingViewModel,
                                    onFinishOnboarding = {
                                        navController.navigate("home") {
                                            popUpTo("onboarding") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
