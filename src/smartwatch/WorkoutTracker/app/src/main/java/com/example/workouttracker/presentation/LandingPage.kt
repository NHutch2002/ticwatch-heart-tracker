package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.first
@Composable
fun LandingPage(navController: NavController) {

    Log.v("LandingPage", "LandingPage")

    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    val users = userDao.getAllUsers()

    LaunchedEffect(users) {
        Log.v("LandingPage", "$users")
        if (users.first().isEmpty()){
            Log.v("LandingPage", "user.isEmpty()")
            navController.navigate("profile_creator")
        }
        else {
            Log.v("LandingPage", "user.isNotEmpty()")
            Log.v("LandingPage", users.first().toString())
            navController.navigate("main_menu")
        }
    }
}
