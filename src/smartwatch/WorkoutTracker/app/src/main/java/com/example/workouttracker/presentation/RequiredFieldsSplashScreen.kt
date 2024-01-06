package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RequiredFieldsSplashScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(2000)
        coroutineScope.launch(Dispatchers.Main) {
            navController.navigate("profile_view")
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "Please Fill\nOut All\nRequired Fields!",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}