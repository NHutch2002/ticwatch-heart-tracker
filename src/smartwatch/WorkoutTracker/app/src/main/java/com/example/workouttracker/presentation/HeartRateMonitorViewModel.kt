package com.example.workouttracker.presentation

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
import kotlin.math.roundToInt

class HeartRateMonitorViewModel(application: Application) : ViewModel() {
    val heartRate = MutableStateFlow<Float?>(null)
    val heartRateList = MutableLiveData<List<Int>>(emptyList())

    private var sensorManager: SensorManager? = null
    private var heartRateListener: SensorEventListener? = null

    private val _progress = MutableLiveData(0f)
    val progress: LiveData<Float> get() = _progress

    private val _heartRateRecovery = MutableLiveData<Int>()
    val heartRateRecovery: LiveData<Int> get() = _heartRateRecovery

    fun startMonitoring(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Log.v("HRR", "Sensor Registered")
        Log.v("HRR", "Sensor Manager: $sensorManager")
        val heartRateSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        heartRateListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    Log.v("HRR", "Heart Rate: ${event.values[0]}")
                    heartRate.value = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager?.registerListener(
            heartRateListener,
            heartRateSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stopMonitoring() {
        sensorManager?.unregisterListener(heartRateListener)
        Log.v("HRR", "Monitoring Stopped")
    }

    fun startHRRMeasurement() {
        viewModelScope.launch {
            Log.v("HRR", "Starting Measurement")
            val startTime = System.currentTimeMillis()
            while (true) {
                val elapsedMillis = System.currentTimeMillis() - startTime
                _progress.value = elapsedMillis / 60000f
                if (elapsedMillis >= 60000L) {
                    break
                }
                heartRate.value?.let {
                    heartRateList.value = heartRateList.value?.plus(it.roundToInt()) // Append current heart rate to the list
                }
                delay(10L)
            }
            Log.v("HRR", "Measurement Complete - ${_heartRateRecovery.value.toString()}")
            stopMonitoring()
        }
    }
}