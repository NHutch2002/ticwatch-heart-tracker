package com.example.workouttracker.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text

@Composable
fun EndWorkoutPage(navController: NavController, maxHeartRate: Float) {
    val context = LocalContext.current
    val viewModel: HeartRateMonitorViewModel = viewModel()
    val HRRActive = remember { mutableStateOf(false) }

    val heartRates by viewModel.heartRates.observeAsState(emptyList())
    val heartRateRecovery by viewModel.heartRateRecovery.observeAsState(0)

    LaunchedEffect(Unit){
        viewModel.startHeartRateMonitoring(context, HRRActive, maxHeartRate)
    }

    // Progress goes from 0 to 1 over 20 seconds
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 20_000, easing = LinearEasing)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgress(
            progress = progress.value,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color(0xFF9CF2F9),
            strokeWidth = 8.dp
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text="End Workout Page")
            Button(onClick = { navController.navigate("landing_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
            }
            Text(text = "Return to home page")
            Text("Heart Rate Recovery: $heartRateRecovery")
        }
    }
}
