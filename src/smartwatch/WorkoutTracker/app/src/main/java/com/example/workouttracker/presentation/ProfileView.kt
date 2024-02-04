package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import java.util.Locale

@Composable
fun ProfileViewPage(navController: NavController) {
    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    var user by remember { mutableStateOf(User("", LocalDate.MIN, 0, "")) }

    val coroutineScope = rememberCoroutineScope()

    val today = LocalDate.now()
    val birthdate = user.birthday

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
        Text(text = "Age: $age years", color = Color.White, fontSize = 16.sp)
        Text(text = "Weight: ${user.weight} kg", color = Color.White, fontSize = 16.sp)
        Text(text = "Gender: ${user.gender.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.size(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { navController.navigate("profile_creator") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Edit", modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { navController.navigate("user_are_you_sure") },
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
                    text = "Delete", modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center
                )
            }
        }
    }
}