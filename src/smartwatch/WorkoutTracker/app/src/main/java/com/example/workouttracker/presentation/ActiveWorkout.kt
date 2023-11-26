package com.example.workouttracker.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActiveWorkoutPage(navController: NavController) {
    val pagerState = rememberPagerState { 2 }
    val isPaused = remember { mutableStateOf(false) }
    val time = remember { mutableLongStateOf(0L) }
    val maxHeartRate = remember { mutableFloatStateOf(0F) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000L)
            if (!isPaused.value) {
                time.longValue++
            }
        }
    }

    HeartRate(maxHeartRate)

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> WorkoutViewPage(time, isPaused, maxHeartRate)
                1 -> WorkoutSettingsPage(navController, isPaused, maxHeartRate)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    repeat(pagerState.pageCount) { index ->
                        PageIndicator(isSelected = pagerState.currentPage == index)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }



}

@Composable
fun WorkoutViewPage(time: MutableState<Long>, isPaused: MutableState<Boolean>, maxHeartRate: MutableFloatState) {
    val currentHeartRate = remember { mutableFloatStateOf(0F) }

    currentHeartRate.floatValue = HeartRate(maxHeartRate)

    MonitorAccelerometer(isPaused)


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Stopwatch(time)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "3.22km", color = Color(0xFF9CF2F9))
            Icon(
                imageVector = Icons.Filled.DirectionsRun,
                contentDescription = null,
                tint = Color(0xFF9CF2F9),
                modifier = Modifier.size(40.dp)
            )
            Text(text = "317kcal", color = Color(0xFF9CF2F9))
        }
        Text(
            if (currentHeartRate.floatValue <= 0) "Reading..." else "${currentHeartRate.floatValue.roundToInt()} BPM",
            color = Color(0xFF9CF2F9),
            fontSize = 20.sp)
    }
}

@Composable
fun WorkoutSettingsPage(navController: NavController, isPaused: MutableState<Boolean>, maxHeartRate: MutableFloatState) {

    Column (
        modifier = Modifier.fillMaxSize(),
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
                Button(onClick = { navController.navigate("end_workout/${maxHeartRate.floatValue}") },  colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                }
                Text("End", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)

            }
        }
    }
}