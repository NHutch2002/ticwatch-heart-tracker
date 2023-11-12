package com.example.workouttracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Icon
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material.ButtonDefaults




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
        composable("track_workout") { TrackWorkoutPage(navController) }
        composable("view_history") { ViewHistoryPage(navController) }
        composable("active_workout") { ActiveWorkoutPage(navController) }
        composable("end_workout/{maxHeartRate}", arguments = listOf(navArgument("maxHeartRate") { type = NavType.FloatType })) { backStackEntry ->
            val maxHeartRate = backStackEntry.arguments?.getFloat("maxHeartRate")
            if (maxHeartRate != null) {
                EndWorkoutPage(navController, maxHeartRate)
            }
        }
    }
}



@Composable
fun LandingPage(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(), // Added background color for visualization
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello Nathan!", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("track_workout") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 27.toFloat(), saturation = 1.toFloat(), lightness = 0.63.toFloat())) ) {
                    Icon(imageVector = Icons.Filled.LocalFireDepartment, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(8.dp)) // Added spacer for separation
                Text(text = "Start a Workout", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("view_history") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 27.toFloat(), saturation = 1.toFloat(), lightness = 0.63.toFloat()))) {
                    Icon(imageVector = Icons.Filled.Assignment, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(8.dp)) // Added spacer for separation
                Text(text = "Workout History", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }
    }
}







