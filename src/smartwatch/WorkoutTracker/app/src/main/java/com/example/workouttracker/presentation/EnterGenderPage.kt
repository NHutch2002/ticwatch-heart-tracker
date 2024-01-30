package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material.icons.outlined.Done
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EnterGenderPage(navController: NavController){
    var isMale by remember { mutableStateOf(false) }
    var isFemale by remember { mutableStateOf(false) }
    var isOther by remember { mutableStateOf(false) }

    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val user = userDao.getAllUsers().first()
            if (user.isNotEmpty()){
                Log.v("EnterGenderPage", "user: ${user.first().gender}")
                when (user.first().gender) {
                    "male" -> {
                        isMale = true
                        isFemale = false
                        isOther = false
                    }
                    "female" -> {
                        isMale = false
                        isFemale = true
                        isOther = false
                    }
                    "other" -> {
                        isMale = false
                        isFemale = false
                        isOther = true
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Please Select\nYour Gender",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Male", color = Color.White, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        isMale = true
                        isFemale = false
                        isOther = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isMale) Color.LightGray else Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Male,
                        contentDescription = null,
                        tint = if (isMale) Color.Black else Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Female", color = Color.White, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        isMale = false
                        isFemale = true
                        isOther = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isFemale) Color.LightGray else Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Female,
                        contentDescription = null,
                        tint = if (isFemale) Color.Black else Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Other", color = Color.White, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        isMale = false
                        isFemale = false
                        isOther = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isOther) Color.LightGray else Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Transgender,
                        contentDescription = null,
                        tint = if (isOther) Color.Black else Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val user = userDao.getUserById("user")
                    if (isMale){
                        user.gender = "male"
                    }
                    else if (isFemale){
                        user.gender = "female"
                    }
                    else{
                        user.gender = "other"
                    }
                    userDao.updateUser(user)
                    val users = userDao.getAllUsers()
                    withContext(Dispatchers.Main) {
                        if (users.first().isNotEmpty()){
                            navController.navigate("landing_page")
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF49a72f)
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}