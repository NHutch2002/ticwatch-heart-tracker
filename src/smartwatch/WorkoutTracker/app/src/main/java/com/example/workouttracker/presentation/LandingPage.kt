package com.example.workouttracker.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.first
@Composable
fun LandingPage(navController: NavController) {


    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    val users = userDao.getAllUsers()

    LaunchedEffect(users) {
        if (users.first().isEmpty()){
            navController.navigate("profile_creator")
        }
        else {
            navController.navigate("main_menu")
        }
    }
}
