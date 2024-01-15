package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EndWorkoutPage(navController: NavController, viewModel: HeartRateMonitorViewModel) {
    val pagerState = rememberPagerState { 3 }

    val calculatingHRR = viewModel.calculatingHRR.collectAsState()

    val heartRates = remember { mutableStateListOf<Int>() }
    var time: Long = 0

    val db = AppDatabase.getInstance(LocalContext.current)
    val workoutDao = db.workoutDao()

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val workout = workoutDao.getAllWorkouts().first().last()
            heartRates.addAll(workout.heartRates)
            time = workout.time
        }
    }

    LaunchedEffect(Unit){
        Log.v("WorkoutReview", "Waiting for workout to be updated")
        if (!calculatingHRR.value){
            viewModel.startHRRMeasurementIfStationary()
        }
        var isFinished = false
        while (!isFinished){
            isFinished = viewModel.progress.value!! >= 1f
            delay(1000)
        }
        viewModel.stopMonitoring()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> HRRPage(viewModel)
                1 -> HeartRateReport(heartRates)
                2 -> ReturnHomePage(navController, time)
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
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}


@Composable
fun HRRPage(viewModel: HeartRateMonitorViewModel) {

    val heartRateRecoverySamples by viewModel.heartRateList.observeAsState()
    val heartRate by viewModel.heartRate.collectAsState()
    val progress by viewModel.progress.observeAsState()
    val measurementCompleted = progress!! >= 1f

    val animationDuration = 2000
    val textOpacity by animateFloatAsState(
        targetValue = if (measurementCompleted) 0f else 1f,
        animationSpec = TweenSpec(durationMillis = animationDuration),
        label = ""
    )
    val textTranslationY by animateDpAsState(
        targetValue = if (measurementCompleted) 100.dp else 0.dp,
        animationSpec = TweenSpec(durationMillis = animationDuration),
        label = ""
    )
    val graphTranslationY by animateDpAsState(
        targetValue = if (measurementCompleted) 0.dp else 100.dp,
        animationSpec = TweenSpec(durationMillis = animationDuration),
        label = ""
    )
    val graphOpacity by animateFloatAsState(
        targetValue = if (measurementCompleted) 1f else 0f,
        animationSpec = TweenSpec(durationMillis = animationDuration),
        label = ""
    )

    val circularProgressScale by animateFloatAsState(
        targetValue = if (measurementCompleted) 3f else 1f, // Scale up when completed
        animationSpec = TweenSpec(durationMillis = animationDuration),
        label = ""
    )
    val circularProgressOpacity by animateFloatAsState(
        targetValue = if (measurementCompleted) 0f else 1f, // Fade out when completed
        animationSpec = TweenSpec(durationMillis = animationDuration),
        label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgress(
            progress = progress!!,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = circularProgressScale,
                    scaleY = circularProgressScale,
                    alpha = circularProgressOpacity
                )
                .fillMaxSize()
                .padding(10.dp),
            color = Color(0xFF9CF2F9),
            strokeWidth = 6.dp
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = !measurementCompleted) {
                Column(
                    modifier = Modifier
                        .alpha(textOpacity)
                        .graphicsLayer { translationY = -textTranslationY.toPx() },
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text="Measuring \n Heart Rate Recovery", fontSize = 14.sp, textAlign = TextAlign.Center)
                    Text(text= "${(progress!! * 100).toInt()}%", fontSize = 64.sp, color = Color(0xFF9CF2F9))
                    if (heartRate == null){
                        Text(text = "Current Heart Rate", fontSize = 14.sp, textAlign = TextAlign.Center)
                        Text(text = "Reading...", fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                    else{
                        Text(text="Current Heart Rate", fontSize = 14.sp, textAlign = TextAlign.Center)
                        Text(text="${heartRate!!.toInt()} BPM", fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
            AnimatedVisibility(visible = measurementCompleted) {
                Column(
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = graphOpacity
                            translationY = -graphTranslationY.toPx()
                        },
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(measurementCompleted){
                        HRRBarChart(heartRateRecoverySamples)
                    }
                }
            }
        }
    }
}

@Composable
fun ReturnHomePage(navController: NavController, time: Long) {

    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Time: %02d:%02d:%02d".format(hours, minutes, seconds))
        Spacer(modifier = Modifier.size(8.dp))
        Button(
            onClick = { navController.navigate("main_menu") },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray
            ),
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
