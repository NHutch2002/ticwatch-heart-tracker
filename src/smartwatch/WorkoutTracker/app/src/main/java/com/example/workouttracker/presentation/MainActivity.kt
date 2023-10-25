package com.example.workouttracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Icon
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ButtonDefaults


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutApp()
        }
    }
}

@Composable
fun WorkoutApp() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(navController, startDestination = "landing_page"){
        composable("landing_page") { LandingPage(navController) }
        composable("track_workout") { TrackWorkoutPage(navController) }
        composable("view_history") { ViewHistoryPage(navController) }
    }
}


@Composable
fun LandingPage(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(), // Added background color for visualization
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello Nathan!", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("track_workout") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 27.toFloat(), saturation = 1.toFloat(), lightness = 0.63.toFloat())) ) {
                    Icon(imageVector = Icons.Filled.LocalFireDepartment, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(8.dp)) // Added spacer for separation
                Text(text = "Start a Workout", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("view_history") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 27.toFloat(), saturation = 1.toFloat(), lightness = 0.63.toFloat()))) {
                    Icon(imageVector = Icons.Filled.Assignment, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(8.dp)) // Added spacer for separation
                Text(text = "Workout History", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun TrackWorkoutPage(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(), // Added background color for visualization
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello Nathan!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "What are we working on today?", fontSize = 18.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("track_workout") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 185.toFloat(), saturation = 0.89.toFloat(), lightness = 0.79.toFloat())) ) {
                    Icon(imageVector = Icons.Filled.DirectionsRun, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(6.dp)) // Added spacer for separation
                Text(text = "Running", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center)
            }
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("view_history") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsl(hue = 261.toFloat(), saturation = 1.toFloat(), lightness = 0.83.toFloat()))) {
                    Icon(imageVector = Icons.Filled.DirectionsBike, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(6.dp)) // Added spacer for separation
                Text(text = "Cycling", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min), fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun ViewHistoryPage(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "View Workout History Page")
    }

}


//val context = LocalContext.current
//    var heartRate by remember { mutableStateOf<Float?>(null) }
//
//    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
//
//    val heartRateListener = remember(sensorManager, heartRateSensor) {
//        object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent) {
//                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
//                    heartRate = event.values[0]
//                }
//            }
//
//            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
//        }
//    }
//
//    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//        if (isGranted) {
//            sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
//        } else {
//            // Handle the case where the user denies the permission request.
//        }
//    }
//
//    DisposableEffect(Unit) {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
//            sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
//        }
//
//        onDispose {
//            sensorManager.unregisterListener(heartRateListener)
//        }
//    }
//    Box(
//        modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center
//    ) {
//        Text("Heart Rate: ${heartRate?.let { "${it.roundToInt()} BPM" } ?: "Reading heart rate, please wait..."}")
//    }