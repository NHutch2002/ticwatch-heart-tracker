package com.example.workouttracker.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.math.sqrt

class HeartRateMonitorViewModel : ViewModel() {
    private val _heartRates = MutableLiveData<List<Float>>()
    val heartRates: LiveData<List<Float>> get() = _heartRates

    fun startHeartRateMonitoring(context: Context, HRRActive: MutableState<Boolean>): Int {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        val heartRateMeasurements = mutableListOf<Float>()
        var testHeartRate = 0

        val heartRateListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    heartRateMeasurements.add(event.values[0])
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        // Register the listener
        sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)

        // Wait for one minute in a new coroutine, then unregister the listener
        viewModelScope.launch {
            Log.v("HRR", "Starting Measurement")
            delay(5000)
            sensorManager.unregisterListener(heartRateListener)
            _heartRates.value = heartRateMeasurements
            testHeartRate = heartRateMeasurements.average().toInt()
            Log.v("HRR", "Completed - $testHeartRate")
            HRRActive.value = false
        }
        return testHeartRate

    }
}
