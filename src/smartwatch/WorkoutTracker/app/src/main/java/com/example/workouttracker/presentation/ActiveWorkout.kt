package com.example.workouttracker.presentation

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActiveWorkoutPage(viewModel: HeartRateMonitorViewModel, endWorkout: () -> Unit) {
    val pagerState = rememberPagerState { 2 }
    val activeTime = remember { mutableLongStateOf(0L) }
    val totalTime = remember { mutableLongStateOf(0L) }
    val maxHeartRate = remember { mutableIntStateOf(0) }
    val heartRates = remember { mutableStateListOf<Int>() }

    val calories = viewModel.caloriesBurned.value
    val caloriesInt = calories?.roundToInt()

    val isPaused = viewModel.isPaused

    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.startMonitoring(context)
        viewModel.monitorAccelerometer(context)
    }
    Log.d("ActiveWorkoutPage", "ViewModel instantiated: $viewModel")

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000L)
            if (!isPaused.value) {
                activeTime.longValue++
            }
            totalTime.longValue++
        }
    }

    val currentDate = LocalDate.now()

    val db = AppDatabase.getInstance(LocalContext.current)
    val workoutDao = db.workoutDao()

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val workout = Workout(null, currentDate, 0, 0, emptyList(), emptyList())
            workoutDao.insertWorkout(workout)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> WorkoutViewPage(activeTime, totalTime, maxHeartRate, viewModel, heartRates, caloriesInt)
                1 -> WorkoutSettingsPage(endWorkout, heartRates, workoutDao, activeTime, totalTime, caloriesInt, viewModel, context)
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
fun WorkoutViewPage(
    activeTime: MutableState<Long>,
    totalTime: MutableState<Long>,
    maxHeartRate: MutableIntState,
    viewModel: HeartRateMonitorViewModel,
    heartRates: MutableList<Int>,
    caloriesInt: Int?
) {

    val heartRate by viewModel.heartRate.collectAsState()
    Log.d("WorkoutViewPage", "Heart rate: $heartRate")
    val heartRateRounded = heartRate?.roundToInt()


    LaunchedEffect(Unit) {
        while (true) {
            val heartRateForDatabase = viewModel.heartRate.value?.roundToInt()
            if (heartRateForDatabase != null){
                heartRates.add(heartRateForDatabase)
                Log.v("WorkoutViewPage", "Heart Rate Added: $heartRateForDatabase")
                delay(5000L)
            }
            else{
                delay(1000L)
            }
        }
    }

    LaunchedEffect(heartRate){
        if (heartRate != null && heartRate!! > maxHeartRate.intValue){
            maxHeartRate.intValue = heartRate!!.roundToInt()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Stopwatch(activeTime, true)
            Stopwatch(totalTime, false)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Peak HR",
                    color = Color(0xFF9CF2F9),
                    fontSize = 12.sp
                )
                Text(
                    if (maxHeartRate.intValue == 0) "-- BPM" else "${maxHeartRate.intValue} BPM",
                    color = Color(0xFF9CF2F9),
                    fontSize = 16.sp
                )
            }
            Icon(
                imageVector = Icons.Filled.DirectionsRun,
                contentDescription = null,
                tint = Color(0xFF9CF2F9),
                modifier = Modifier.size(40.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(
                    text = "Calories",
                    color = Color(0xFF9CF2F9),
                    fontSize = 12.sp
                )
                Text(
                    if (caloriesInt == null) "--kcal" else "${caloriesInt}kcal",
                    color = Color(0xFF9CF2F9)
                )
            }
        }
        Text(
            if (heartRateRounded == null ) "Reading..." else if ( heartRateRounded <= 0) "Sensor\nError" else "$heartRateRounded BPM",
            color = Color(0xFF9CF2F9),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WorkoutSettingsPage(
    endWorkout: () -> Unit,
    heartRates: MutableList<Int>,
    workoutDao: WorkoutDao,
    activeTime: MutableState<Long>,
    totalTime: MutableState<Long>,
    caloriesInt: Int?,
    viewModel: HeartRateMonitorViewModel,
    context: Context
) {

    val workout = remember { mutableStateOf(Workout(null, LocalDate.MIN, 0, 0 ,emptyList(), emptyList())) }

    val isPaused by viewModel.isPaused.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { viewModel.toggleIsPaused(context) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                        contentDescription = if (isPaused) "Resume" else "Pause"
                    )
                }
                Text(
                    if (isPaused) "Resume" else "Pause",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center
                )

            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        Log.v("WorkoutSettingsPage", "Heart rates: ${heartRates.toList()}")
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.stopAccelerometer()
                            workout.value = workoutDao.getAllWorkouts().first().last()
                            workout.value.heartRates = heartRates.toList()
                            workout.value.activeTime = activeTime.value
                            workout.value.totalTime = totalTime.value
                            if (caloriesInt != null) {
                                workout.value.calories = caloriesInt
                            }
                            workoutDao.updateWorkout(workout.value)
                            Log.v("WorkoutSettingsPage", "Inserted workout: $workout")
                            endWorkout()
                        }

                              },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                }
                Text(
                    "End", modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center
                )

            }
        }
    }
}