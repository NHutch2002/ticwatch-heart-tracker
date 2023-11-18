package com.example.workouttracker.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text

@Composable
fun EndWorkoutPage(navController: NavController, maxHeartRate: Float) {
    val context = LocalContext.current
    val viewModel: HeartRateMonitorViewModel = viewModel()

    val heartRates by viewModel.heartRates.observeAsState(emptyList())
    val heartRateRecovery by viewModel.heartRateRecovery.observeAsState(0)
    val progress by viewModel.progress.observeAsState(0f)


    LaunchedEffect(Unit){
        viewModel.startHeartRateMonitoring(context, maxHeartRate)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgress(
            progress = progress,
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
            if (progress < 1){
                Text(text="Calculating \n Heart Rate Recovery", fontSize = 16.sp, textAlign = TextAlign.Center)
                Text(text= "${(progress * 100).toInt()}%", fontSize = 64.sp, color = Color(0xFF9CF2F9))
                if (heartRates.isEmpty()){
                    Text(text = "Current Heartrate: Reading...")
                }
                else{
                    Text(text="Current Heartrate: \n ${heartRates.takeLast(1)[0].toInt()} BPM", fontSize = 16.sp, textAlign = TextAlign.Center)
                }
            }
            else{
                Button(onClick = { navController.navigate("landing_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
                }
                Text(text = "Return to home page")
                Text("Heart Rate Recovery: $heartRateRecovery")
            }
        }
    }
}
