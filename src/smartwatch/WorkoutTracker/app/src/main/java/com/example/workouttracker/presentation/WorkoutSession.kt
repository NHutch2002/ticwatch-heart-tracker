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

    var isEndWorkoutScreen by rememberSaveable { mutableStateOf(false) }

    if (isEndWorkoutScreen) {
        val stopIntent = Intent(application, HeartRateMonitorService::class.java)
        application.stopService(stopIntent)

        EndWorkoutPage(navController, viewModel)
    } else {
        val startIntent = Intent(application, HeartRateMonitorService::class.java)
        application.startService(startIntent)

        ActiveWorkoutPage(navController, viewModel) {
            isEndWorkoutScreen = true
        }
    }
}