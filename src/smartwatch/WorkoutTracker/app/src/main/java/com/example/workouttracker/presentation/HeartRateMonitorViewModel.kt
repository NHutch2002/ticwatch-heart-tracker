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
                    heartRateList.value = heartRateList.value?.plus(it.roundToInt()) // Append current heart rate to the list
                }
                delay(10L)
            }
            Log.v("HRR", "Measurement Complete - ${_heartRateRecovery.value.toString()}")
            stopMonitoring()
        }
    }

    fun toggleIsPaused(){

        Log.v("HRR", "Toggling isPaused - ${isPaused.value}")

        isPaused.value = !isPaused.value

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
                while (true) {
                    val elapsedMillis = System.currentTimeMillis() - startTime
                    _progress.value = elapsedMillis / 60000f
                    if (elapsedMillis >= 60000L) {
                        break
                    }
                    if (!isPaused.value) {
                        midWorkoutHRR.value = emptyList()
                        Log.v("HRR", "Measurement Cancelled")
                        return@launch
                    }
                    heartRate.value?.let {
                        midWorkoutHRR.value = midWorkoutHRR.value?.plus(it.roundToInt()) // Append current heart rate to the list
                    }
                    delay(10L)
                }
                heartRateList.value = midWorkoutHRR.value
                midWorkoutHRR.value = midWorkoutHRR.value?.plus(-1)
                calculatingHRR.value = false
                CoroutineScope(Dispatchers.IO).launch {
                    workout = workoutDao.getAllWorkouts().first().last()
                    workout.HRRs = workout.HRRs.plus(midWorkoutHRR.value!!)
                    workoutDao.updateWorkout(workout)
                }
            }
        }
    }
    // Pass the value of isPaused into a new function in here called startMidHRRMeasurement()
    // This function will keep track of HRR values in a temporary variable while active
    // while keeping an eye out for an updated isPaused value. This could potentially be done with a LiveData
    // but need to check the functionality of that.
    // Once the function has complete that measurement, we can then add the list of HRR to a new variable
    // that is passed into the database.
    // Need to figure out how these HRR values are going to be represented on the EndWorkout Page.
    // Could be a combination of horizontal sliding and lazycolumn
    // Finally also need to take into account the maxHeartRate, saving this value as the first value in the HRR
    // measurement. This needs to be reset after each HRR measurement to ensure fresh measurements each time.



    // Make a workout in the database at the start. Update the last workout when the user navigates to the end workout screen.
    // This will mean that the mid workout HRR measurements can always be added straight away to the last db entry
}