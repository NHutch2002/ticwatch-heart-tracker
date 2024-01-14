package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutReviewPage(navController: NavController, workoutId: String) {

    val db = AppDatabase.getInstance(LocalContext.current)
    val workoutDao = db.workoutDao()
    val workout = remember { mutableStateOf(Workout(null, LocalDate.MIN, emptyList(), emptyList())) }

    val HRRValues = workout.value.HRRs

    val pagerState = rememberPagerState { 3 }

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.IO).launch{
            workout.value = workoutDao.getWorkoutById(workoutId.toInt())
            Log.v("WorkoutReview", workout.value.heartRates.toString())
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> HRRReviewPage(HRRValues)
                1 -> HRReviewReport()
                2 -> ReturnHistoryPage(navController)
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
fun HRRReviewPage(HRRValues: List<Int>){
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (HRRValues.isEmpty()){

        }
        else {
            HRRBarChart(HRRValues)
        }

    }
}


@Composable
fun HRReviewReport() {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "HR Report")
    }
}


@Composable
fun ReturnHistoryPage(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Third Page")
        Button(
            onClick = { navController.navigate("view_history") },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF9CF2F9)
            )
        ) {

            Text(text = "Return to workout history page")
        }
    }
}




