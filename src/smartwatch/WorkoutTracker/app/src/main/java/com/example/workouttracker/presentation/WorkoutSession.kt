package com.example.workouttracker.presentation

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun WorkoutSession(navController: NavController) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: HeartRateMonitorViewModel = viewModel(factory = HeartRateMonitorViewModelFactory(application))

    // Use a rememberSaveable to remember which screen (ActiveWorkout or EndWorkout) is currently displayed
    var isEndWorkoutScreen by rememberSaveable { mutableStateOf(false) }

    // Display the ActiveWorkout or EndWorkout screen based on the value of isEndWorkoutScreen
    if (isEndWorkoutScreen) {
        // Stop the HeartRateMonitorService when the EndWorkoutPage is displayed
        val stopIntent = Intent(application, HeartRateMonitorService::class.java)
        application.stopService(stopIntent)

        EndWorkoutPage(navController, viewModel)
    } else {
        // Start the HeartRateMonitorService when the ActiveWorkoutPage is displayed
        val startIntent = Intent(application, HeartRateMonitorService::class.java)
        application.startService(startIntent)

        ActiveWorkoutPage(navController, viewModel) {
            // When the active workout ends, switch to the EndWorkout screen
            isEndWorkoutScreen = true
        }
    }
}