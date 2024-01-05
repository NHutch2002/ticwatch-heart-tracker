package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Icon
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ButtonDefaults

@Composable
fun MainMenuPage(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { navController.navigate("track_workout") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.hsl(
                            hue = 27.toFloat(),
                            saturation = 1.toFloat(),
                            lightness = 0.63.toFloat()
                        )
                    ),
                    modifier = Modifier.size(54.dp)
                ) {                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Start a Workout", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { navController.navigate("view_history") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.hsl(
                            hue = 27.toFloat(),
                            saturation = 1.toFloat(),
                            lightness = 0.63.toFloat()
                        )
                    ),
                    modifier = Modifier.size(54.dp)
                ) {
                Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Health History", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { navController.navigate("user_settings") },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray
                ),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Settings", modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}
