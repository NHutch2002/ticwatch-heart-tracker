package com.example.workouttracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.SwipeDismissableNavHost





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutApp()
        }
    }
}

@Composable
fun WorkoutApp() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(navController, startDestination = "landing_page"){
        composable("landing_page") { LandingPage(navController) }
        composable("main_menu") {MainMenuPage(navController)}
        composable("user_settings") { UserSettingsPage(navController) }
        composable("profile_creator") { EnterNamePage(navController) }
        composable("profile_view") { ProfileViewPage(navController) }
        composable("profile_deletion_splashscreen") { ProfileDeletionSplashscreen(navController) }
        composable("required_fields_splashscreen") { RequiredFieldsSplashScreen(navController) }
        composable("track_workout") { TrackWorkoutPage(navController) }
        composable("view_history") { ViewHistoryPage(navController) }
        composable("workout_session") { WorkoutSession(navController) }
    }
}










