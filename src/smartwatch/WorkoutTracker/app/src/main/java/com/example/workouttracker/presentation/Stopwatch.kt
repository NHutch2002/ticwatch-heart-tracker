package com.example.workouttracker.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun Stopwatch(time: MutableState<Long>) {

    val hours = time.value / 3600
    val minutes = (time.value % 3600) / 60
    val seconds = time.value % 60

    Text(
        text = "%02d:%02d:%02d".format(hours, minutes, seconds),
        color = Color(0xFF9CF2F9),
        fontSize = 20.sp
    )
}

