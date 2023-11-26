package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text

@Composable
fun TrackWorkoutPage(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(), // Added background color for visualization
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello Nathan!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "What are we working on today?", fontSize = 18.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("active_workout") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 185.toFloat(), saturation = 0.89.toFloat(), lightness = 0.79.toFloat())) ) {
                    Icon(
                        imageVector = Icons.Filled.DirectionsRun,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp)) // Added spacer for separation
                Text(text = "Running", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center)
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("active_workout") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 261.toFloat(), saturation = 1.toFloat(), lightness = 0.83.toFloat()))) {
                    Icon(
                        imageVector = Icons.Filled.DirectionsBike,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp)) // Added spacer for separation
                Text(text = "Cycling", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        }
    }
}