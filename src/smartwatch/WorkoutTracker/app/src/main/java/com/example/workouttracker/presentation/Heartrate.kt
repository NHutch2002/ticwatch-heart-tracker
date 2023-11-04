package com.example.workouttracker.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.wear.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Button
import kotlin.math.roundToInt

@Composable
fun HeartRate() {
    var heartRate by remember { mutableStateOf<Float?>(null) }
    var showRequestPermissionButton by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)


    val heartRateListener = remember(sensorManager, heartRateSensor) {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    heartRate = event.values[0]
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            // Handle the case where the user denies the permission request.
        }
    }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
            sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
        }

        onDispose {
            sensorManager.unregisterListener(heartRateListener)
        }
    }


    if (showRequestPermissionButton) {
        Button(onClick = { requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS) }) {
            Text("Request Permission")
        }
    }
    else {
        Text("${heartRate?.let { "${it.roundToInt()}" } ?: "..."} BPM", color = Color(0xFF9CF2F9))
    }

}
