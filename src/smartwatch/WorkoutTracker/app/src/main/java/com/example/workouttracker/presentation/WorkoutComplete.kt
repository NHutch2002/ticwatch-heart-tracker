package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.workouttracker.presentation.HeartRate
import com.example.workouttracker.presentation.HeartRateMonitorViewModel
import kotlin.math.roundToInt

@Composable
fun EndWorkoutPage(navController: NavController) {
    val context = LocalContext.current
    val viewModel: HeartRateMonitorViewModel = viewModel()
    val HRRActive = remember { mutableStateOf(false) }

    viewModel.startHeartRateMonitoring(context, HRRActive)

    val heartRates by viewModel.heartRates.observeAsState(emptyList())
    val averageHeartRate = heartRates.average().toInt()



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="End Workout Page")
        Button(onClick = { navController.navigate("landing_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
        }
        Text(text = "Return to home page")
        Text("Average Heart Rate: $averageHeartRate")
        Text(HeartRate()?.let { "${it.roundToInt()} BPM" } ?: "Reading...", color = Color(0xFF9CF2F9))
    }
}