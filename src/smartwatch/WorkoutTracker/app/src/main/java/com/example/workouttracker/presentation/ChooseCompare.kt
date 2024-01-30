package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.first
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

// NEED TO ADD DATES TO THE HRR BUTTONS AS A POINT OF REFERENCE
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ChooseComparePage(navController: NavController, hrr1: String) {
    val db = AppDatabase.getInstance(LocalContext.current)
    val workoutDao = db.workoutDao()
    val workouts = remember { mutableStateOf(listOf<Workout>()) }
    val HRRs = remember { mutableStateOf(listOf<List<Int>>()) }

    LaunchedEffect(Unit) {
        workouts.value = workoutDao.getAllWorkouts().first()
        workouts.value.forEach { workout ->
            HRRs.value += splitListBySeparator(workout.HRRs, -1)
        }
    }

    val pagerState = rememberPagerState(initialPage = 0)


    Box(modifier = Modifier.fillMaxSize()) {
        if (HRRs.value.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No workouts to show",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            HorizontalPager(state = pagerState, count = ceil(HRRs.value.size / 2.0).toInt()) { page ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = "Please select a HRR\nmeasurement to compare",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val startIndex = page * 2
                        val endIndex = (page + 1) * 2
                        val HRRsOnPage = HRRs.value.subList(startIndex, endIndex.coerceAtMost(HRRs.value.size))

                        HRRsOnPage.forEach { HRR ->
                            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = { navController.navigate("hrr_comparison/${hrr1}/${HRR}") },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Cyan
                                    )
                                ){
                                    Log.v("ChooseComparePage", "HRR: ${HRR.first().toInt()} ${HRR.last().toInt()} ${HRR.first().toInt() - HRR.last().toInt()}")
                                    Text(
                                        text = "${HRR.first().toInt() - HRR.last().toInt()}",
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate("main_menu")
                Log.v("ChooseComparePage", "main_menu")
                      },
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
        Spacer(modifier = Modifier.height(30.dp))
        if (workouts.value.size > 2) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(pagerState.pageCount) { index ->
                    PageIndicator(isSelected = pagerState.currentPage == index)
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
        }
        else{
            Spacer(modifier = Modifier.size(16.dp))
        }
    }

}