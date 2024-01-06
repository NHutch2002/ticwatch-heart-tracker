package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable
fun ProfileViewPage(navController: NavController) {
    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    var user by remember { mutableStateOf(User("", LocalDate.MIN, 0)) }

    val coroutineScope = rememberCoroutineScope()

    val today = LocalDate.now()
    val birthdate = user.birthday // assuming this is the LocalDate object from the database

    var age = today.year - birthdate.year

    if (today.monthValue < birthdate.monthValue ||
        (today.monthValue == birthdate.monthValue && today.dayOfMonth < birthdate.dayOfMonth)) {
        age -= 1
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val fetchedUser = userDao.getUserById("user")
            withContext(Dispatchers.Main) {
                user = fetchedUser
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Age: $age years", color = Color.White, fontSize = 20.sp)
        Text(text = "Weight: ${user.weight} kg", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.size(16.dp))
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { navController.navigate("profile_deletion_splashscreen") },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Delete Info", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center
            )
        }
    }
}