package com.example.workouttracker.presentation;

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HeartRateMonitorViewModel(application: Application) : ViewModel() {
    val heartRate = MutableStateFlow<Float?>(null)
    private val _heartRates = MutableLiveData<List<Float>>()
    val heartRates: LiveData<List<Float>> get() = _heartRates
    private val _heartRateRecovery = MutableLiveData<Int>()
    val heartRateRecovery: LiveData<Int> get() = _heartRateRecovery
    private val _progress = MutableLiveData(0f)
    val progress: LiveData<Float> get() = _progress
    private var isFinalReading = true

    fun startHeartRateMonitoring(context: Context, maxHeartRate: Float) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        val heartRateMeasurements = mutableListOf<Float>()

        val heartRateListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    heartRate.value = event.values[0]
                    heartRateMeasurements.add(event.values[0])
                    _heartRates.value = heartRateMeasurements
                    if (heartRateMeasurements.size > 5) {
                        val minOfLastTen = heartRateMeasurements.takeLast(5).minOrNull() ?: 0f
                        _heartRateRecovery.value = (maxHeartRate - minOfLastTen).toInt()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            heartRateListener,
            heartRateSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        viewModelScope.launch {
            while (true) {
                if (isFinalReading) {
                    val startTime = System.currentTimeMillis()
                    while (true) {
                        val elapsedMillis = System.currentTimeMillis() - startTime
                        _progress.value = elapsedMillis / 60000f
                        if (elapsedMillis >= 60000L) {
                            break
                        }
                        delay(10L)
                    }
                    sensorManager.unregisterListener(heartRateListener)
                    _heartRates.value = heartRateMeasurements
                    break
                }
                delay(10L)
            }
        }
    }

    fun finalHRRReading() {
        isFinalReading = false

    }
}
