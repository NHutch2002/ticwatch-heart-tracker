package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun WorkoutDeletionSplashscreen(navController: NavController) {

    val db = AppDatabase.getInstance(LocalContext.current)
    val workoutDao = db.workoutDao()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(2000)
        coroutineScope.launch(Dispatchers.IO) {
            workoutDao.deleteAllWorkouts()
            withContext(Dispatchers.Main) {
                navController.navigate("landing_page")
            }
        }
    }

    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "Workouts\nSuccessfully\nDeleted!",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}