package com.example.workouttracker.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.activity.result.contract.ActivityResultContracts






class MainActivity : ComponentActivity() {

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.BODY_SENSORS)

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                // All permissions are granted
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!allPermissionsGranted()) {
            requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
        }

        setContent {
            WorkoutApp()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}



@Composable
fun WorkoutApp() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(navController, startDestination = "landing_page"){
        composable("landing_page") { LandingPage(navController) }
        composable("main_menu") { MainMenuPage(navController) }
        composable("user_settings") { UserSettingsPage(navController) }
        composable("profile_creator") { EnterBirthdayPage(navController) }
        composable("profile_weight") { EnterWeightPage(navController) }
        composable("profile_gender") { EnterGenderPage(navController) }
        composable("profile_view") { ProfileViewPage(navController) }
        composable("user_are_you_sure") { UserAreYouSure(navController) }
        composable("workout_are_you_sure") { WorkoutAreYouSure(navController) }
        composable("workout_deletion_splashscreen") { WorkoutDeletionSplashscreen(navController) }
        composable("profile_deletion_splashscreen") { ProfileDeletionSplashscreen(navController) }
        composable("required_fields_splashscreen") { RequiredFieldsSplashScreen(navController) }
        composable("heart_rate_info_screen") { HeartRateInfoScreen() }
        composable("hrr_info_screen") { HRRInfoScreen(navController) }
        composable("track_workout") { TrackWorkoutPage(navController) }
        composable("view_history") { ViewHistoryPage(navController) }
        composable("workout_session") { WorkoutSession(navController) }
        composable("workout_review/{workoutId}") { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            if (workoutId != null) {
                WorkoutReviewPage(navController, workoutId)
            } else {
                Log.v("WorkoutApp", "workoutId was null")
            }
        }
        composable("hrr_comparison/{hrr1}/{hrr2}") { backStackEntry ->
            val hrr1 = backStackEntry.arguments?.getString("hrr1")
            val hrr2 = backStackEntry.arguments?.getString("hrr2")
            if (hrr1 != null && hrr2 != null) {
                HRRComparisonPage(navController, hrr1, hrr2)
            } else {
                Log.v("WorkoutApp", "workout1Id or workout2Id was null")
            }
        }
        composable("choose_compare/{heartRateRecoverySamples}") { backStackEntry ->
            val heartRateRecoverySamples = backStackEntry.arguments?.getString("heartRateRecoverySamples")
            if (heartRateRecoverySamples != null) {
                ChooseComparePage(navController, heartRateRecoverySamples)
            } else {
                Log.v("WorkoutApp", "heartRateRecoverySamples was null")
            }
        }

    }
}










