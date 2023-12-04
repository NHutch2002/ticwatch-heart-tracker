//package com.example.workouttracker.presentation
//
//import android.content.Context
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.os.PowerManager
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.*
//
//class HeartRateMonitorViewModel : ViewModel() {
//    private val _heartRates = MutableLiveData<List<Float>>()
//    val heartRates: LiveData<List<Float>> get() = _heartRates
//
//    private val _heartRateRecovery = MutableLiveData<Int>()
//    val heartRateRecovery: LiveData<Int> get() = _heartRateRecovery
//
//    private val _progress = MutableLiveData(0f)
//    val progress: LiveData<Float> get() = _progress
//
//    fun startHeartRateMonitoring(context: Context, maxHeartRate: Float) {
//        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
//        val heartRateMeasurements = mutableListOf<Float>()
//
//        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
//        val wakeLock = powerManager.newWakeLock(
//            PowerManager.PARTIAL_WAKE_LOCK,
//            "MyApp::HeartRateMonitorWakeLock"
//        )
//
//        wakeLock.acquire(60 * 1000L /* 1 minute */)
//
//        val heartRateListener = object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent) {
//                Log.v("WakeLock", wakeLock.isHeld.toString())
//                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
//                    heartRateMeasurements.add(event.values[0])
//                    _heartRates.value = heartRateMeasurements // Update _heartRates here
//                    Log.v("HRR Measurements Size", heartRateMeasurements.size.toString())
//                    if (heartRateMeasurements.size > 5) {
//                        val minOfLastTen = heartRateMeasurements.takeLast(5).minOrNull() ?: 0f
//                        Log.v("HRR Intermediate", minOfLastTen.toString())
//                        _heartRateRecovery.value = (maxHeartRate - minOfLastTen).toInt()
//                    }
//                }
//            }
//
//
//            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
//        }
//
//        sensorManager.registerListener(
//            heartRateListener,
//            heartRateSensor,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )
//
//        viewModelScope.launch {
//
//            Log.v("HRR", "Starting Measurement")
//            val startTime = System.currentTimeMillis()
//            while (true) {
//                val elapsedMillis = System.currentTimeMillis() - startTime
//                _progress.value = elapsedMillis / 60000f
//                if (elapsedMillis >= 60000L) {
//                    break
//                }
//                delay(10L)
//            }
//            Log.v("HRR", "Measurement Complete - ${_heartRateRecovery.value.toString()}")
//            sensorManager.unregisterListener(heartRateListener)
//            _heartRates.value = heartRateMeasurements
//            if (wakeLock.isHeld) {
//                wakeLock.release()
//            }
//        }
//    }
//}
//
