package com.example.workouttracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.widget.ArcLayout.LayoutParams.VerticalAlignment
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


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
        composable("end_workout") { EndWorkoutPage(navController) }
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
                    Icon(imageVector = Icons.Filled.DirectionsRun, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(6.dp)) // Added spacer for separation
                Text(text = "Running", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center)
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("active_workout") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 261.toFloat(), saturation = 1.toFloat(), lightness = 0.83.toFloat()))) {
                    Icon(imageVector = Icons.Filled.DirectionsBike, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(6.dp)) // Added spacer for separation
                Text(text = "Cycling", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun ViewHistoryPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="Workout History Page")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActiveWorkoutPage(navController: NavController) {
    val pagerState = rememberPagerState { 2 } // Replace 2 with your actual page count
    val isPaused = remember { mutableStateOf(false) }

    Box {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> FirstPage(isPaused)
                1 -> SecondPage(navController, isPaused)
            }
        }

        // Page indicators
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent), // Add a background color to make sure the indicators are visible
            contentAlignment = Alignment.BottomCenter
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.9f)) // Pushes the indicators towards the bottom
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                repeat(pagerState.pageCount) { index ->
                    PageIndicator(isSelected = pagerState.currentPage == index)
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Adds some space at the bottom
        }
    }
}


@Composable
fun PageIndicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 16.dp else 12.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color.White else Color.Gray)
    )
}

@Composable
fun FirstPage(isPaused: MutableState<Boolean>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Stopwatch(isPaused)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "3.22km", color = Color(0xFF9CF2F9))
            Icon(
                imageVector = Icons.Filled.DirectionsRun,
                contentDescription = null,
                tint = Color(0xFF9CF2F9)
            )
            Text(text = "317kcal", color = Color(0xFF9CF2F9))
        }
        HeartRate()
    }
}

@Composable
fun SecondPage(navController: NavController, isPaused: MutableState<Boolean>) {

    Column (
        modifier = Modifier.fillMaxSize(), // Added background color for visualization
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { isPaused.value = !isPaused.value },  colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
                    Icon(
                        imageVector = if (isPaused.value) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                        contentDescription = if (isPaused.value) "Resume" else "Pause"
                    )
                }
                Text(if (isPaused.value) "Resume" else "Pause", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)

            }
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("end_workout") },  colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                }
                Text("End", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)

            }
        }
    }
}

@Composable
fun EndWorkoutPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="End Workout Page")
        Button(onClick = { navController.navigate("landing_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {

        }
        Text(text = "Return to home page")
    }
}