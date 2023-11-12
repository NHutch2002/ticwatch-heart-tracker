package com.example.workouttracker.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun MonitorAccelerometer(isPaused: MutableState<Boolean>) {
    val context = LocalContext.current
    var acceleration by remember { mutableStateOf(0f) }
    val measurements = remember { mutableStateListOf<Float>() }
    val HRRActive = remember { mutableStateOf(false) }


    val viewModel: HeartRateMonitorViewModel = viewModel()

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)




    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val accelerometerListener = remember(sensorManager, accelerometer) {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val magnitude = sqrt((x * x + y * y + z * z).toDouble()) - 9.81
                    acceleration = magnitude.toFloat()

                    measurements.add(acceleration)

                    // If the list has more than 100 elements, remove the oldest one
                    if (measurements.size > 100) {
                        measurements.removeAt(0)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        onDispose {
            sensorManager.unregisterListener(accelerometerListener)
        }
    }

    val averageAcceleration = measurements.average()


    if (averageAcceleration < 3) {
        if (!isPaused.value){
            vibrator.vibrate(vibrationEffect)
        }
        isPaused.value = true
//        if (!HRRActive.value){
//            HRRActive.value = true
//            viewModel.startHeartRateMonitoring(context, HRRActive)
//        }
    }
    else{
        if (isPaused.value){
            vibrator.vibrate(vibrationEffect)
        }
        isPaused.value = false
    }
}

