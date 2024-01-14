package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
fun EnterWeightPage(navController: NavController){
    var isWeightDropdownExpanded by remember { mutableStateOf(false) }
    var selectedWeight by remember { mutableIntStateOf(70) }

    val weights = (30..200).toList()

    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Please Enter\nYour Weight",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ){
            Button(
                onClick = { isWeightDropdownExpanded = true },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray
                )
            ) {
                Text(text = selectedWeight.toString())
            }
            Text(text = "kg", color = Color.White, modifier = Modifier.padding(start = 8.dp))

            DropdownMenu(
                expanded = isWeightDropdownExpanded,
                onDismissRequest = { isWeightDropdownExpanded = false }
            ) {
                weights.forEach { weight ->
                    DropdownMenuItem(onClick = {
                        selectedWeight = weight
                        isWeightDropdownExpanded = false
                    }) {
                        Text(text = weight.toString(), color = Color.Black)
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val user = userDao.getUserById("user")
                    user.weight = selectedWeight
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