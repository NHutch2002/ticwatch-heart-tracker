package com.example.workouttracker.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Stopwatch(isPaused: MutableState<Boolean>) {
    var time by remember { mutableLongStateOf(0L) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000L)
            if (!isPaused.value) {
                time++
            }
        }
    }

    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60

    Text(text = "%02d:%02d:%02d".format(hours, minutes, seconds), color = Color(0xFF9CF2F9))
}

