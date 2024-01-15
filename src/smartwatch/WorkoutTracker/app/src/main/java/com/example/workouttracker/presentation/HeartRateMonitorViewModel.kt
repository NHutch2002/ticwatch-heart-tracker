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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var isPaused = MutableStateFlow(false)
    private var midWorkoutHRR = MutableLiveData<List<Int>>(emptyList())

    var calculatingHRR = MutableStateFlow(false)

    val db = AppDatabase.getInstance(application)
    val workoutDao = db.workoutDao()

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
                    heartRateList.value = heartRateList.value?.plus(it.roundToInt())
                }
                delay(10L)
            }
            Log.v("HRR", "Measurement Complete - ${_heartRateRecovery.value.toString()}")
            stopMonitoring()
        }
    }

    fun toggleIsPaused(){
        isPaused.value = !isPaused.value
        Log.v("HRR", "Toggling isPaused - ${isPaused.value}")
        if (isPaused.value){
            startHRRMeasurementIfStationary()
        }
    }

    // Remember to add a conditional so that heartRate needs to be a certain value before triggering a measurement e.g. >= 100bpm to prevent premature measurement
    fun startHRRMeasurementIfStationary() {
        _progress.value = 0f
        viewModelScope.launch {
            if (isPaused.value) {
                Log.v("HRR", "Starting Measurement")

                calculatingHRR.value = true

                var workout: Workout

                val startTime = System.currentTimeMillis()

                val tempHeartRates = mutableListOf<Int>()

                while (true) {
                    val elapsedMillis = System.currentTimeMillis() - startTime
                    _progress.value = elapsedMillis / 60000f
                    if (elapsedMillis >= 60000L) {
                        break
                    }
                    if (!isPaused.value) {
                        midWorkoutHRR.value = emptyList()
                        Log.v("HRR", "Measurement Cancelled")
                        calculatingHRR.value = false
                        return@launch
                    }
                    heartRate.value?.let {
                        tempHeartRates.add(it.roundToInt())
                        if (tempHeartRates.size >= 100) {
                            midWorkoutHRR.value = midWorkoutHRR.value?.plus(tempHeartRates.first())
                            tempHeartRates.clear()
                        }
                    }
                    delay(10L)
                }
                heartRateList.value = midWorkoutHRR.value
                midWorkoutHRR.value = midWorkoutHRR.value?.plus(-1)
                calculatingHRR.value = false
                Log.v("HRR", "Measurement Complete!!")
                CoroutineScope(Dispatchers.IO).launch {
                    workout = workoutDao.getAllWorkouts().first().last()
                    workout.HRRs = workout.HRRs.plus(midWorkoutHRR.value!!)
                    workoutDao.updateWorkout(workout)
                    withContext(Dispatchers.Main){
                        midWorkoutHRR.value = emptyList()
                    }
                }
            }
        }
    }
}