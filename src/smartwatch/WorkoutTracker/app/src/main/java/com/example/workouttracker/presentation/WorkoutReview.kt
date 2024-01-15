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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.Alignment
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutReviewPage(navController: NavController, workoutId: String) {

    val db = AppDatabase.getInstance(LocalContext.current)
    val workoutDao = db.workoutDao()
    val workout = remember { mutableStateOf(Workout(null, LocalDate.MIN, 0, emptyList(), emptyList())) }
    

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
                0 -> HRRReviewPage(workout.value.HRRs)
                1 -> HeartRateReport(workout.value.heartRates)
                2 -> ReturnHistoryPage(navController, workout.value.time, workout.value.date)
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

    val splitHRRs = splitListBySeparator(HRRValues, -1)

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (HRRValues.isEmpty()){
            Text(text = "Please wait...")
        }
        else {
            Log.v("Review", splitHRRs.last().last().toString())
                HRRBarChart(splitHRRs.last())
        }

    }
}

@Composable
fun ReturnHistoryPage(navController: NavController, time: Long, date: LocalDate){
    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val workoutDate = date.format(dateFormatter)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Date: $workoutDate")
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "Duration: %02d:%02d:%02d".format(hours, minutes, seconds))
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





