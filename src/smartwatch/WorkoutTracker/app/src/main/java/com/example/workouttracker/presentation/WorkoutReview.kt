package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun WorkoutReviewPage(navController: NavController, workoutId: String) {

    val db = AppDatabase.getInstance(LocalContext.current)
    val workoutDao = db.workoutDao()
    val workout = remember { mutableStateOf(Workout(null, LocalDate.MIN, emptyList(), emptyList())) }

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.IO).launch{
            workout.value = workoutDao.getWorkoutById(workoutId.toInt())
            Log.v("WorkoutReview", workout.value.heartRates.toString())
        }
    }


    Column {
        Text(text = workout.value.date.toString())
        Text(workout.value.id.toString())
    }

}