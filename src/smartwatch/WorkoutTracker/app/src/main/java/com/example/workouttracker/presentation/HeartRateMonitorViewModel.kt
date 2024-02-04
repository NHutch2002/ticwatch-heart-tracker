package com.example.workouttracker.presentation

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.VibrationEffect
import android.os.Vibrator
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
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlin.math.sqrt


class HeartRateMonitorViewModel(application: Application) : ViewModel() {

    val heartRate = MutableStateFlow<Float?>(null)
    val heartRateList = MutableLiveData<List<Int>>(emptyList())

    private var sensorManager: SensorManager? = null
    private var heartRateListener: SensorEventListener? = null
    private var accelerometerListener: SensorEventListener? = null

    private val _progress = MutableLiveData(0f)
    val progress: LiveData<Float> get() = _progress

    var isPaused = MutableStateFlow(true)
    private var midWorkoutHRR = MutableLiveData<List<Int>>(emptyList())

    var calculatingHRR = MutableStateFlow(false)

    val db = AppDatabase.getInstance(application)
    private val workoutDao = db.workoutDao()
    private val userDao = db.userDao()

    var peakHeartRate = MutableLiveData(0)

    private var calculateCalories = MutableLiveData(false)
    var caloriesBurned = MutableLiveData(0F)

    private val age = MutableLiveData(0)



    fun startMonitoring(context: Context) {

        CoroutineScope(Dispatchers.IO).launch{
            val user = userDao.getUserById("user")
            val today = LocalDate.now()
            val birthdate = user.birthday
            withContext(Dispatchers.Main){
                age.value = today.year - birthdate.year

                if (today.monthValue < birthdate.monthValue ||
                    (today.monthValue == birthdate.monthValue && today.dayOfMonth < birthdate.dayOfMonth)) {
                    age.value = age.value?.minus(1)
                }
            }
        }

        calculateCalories.value = true
        calculateCaloriesBurned()
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Log.v("HRR", "Sensor Registered")
        Log.v("HRR", "Sensor Manager: $sensorManager")
        val heartRateSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        heartRateListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    Log.v("HRR", "Heart Rate: ${event.values[0]}")
                    heartRate.value = event.values[0]
                    if (event.values[0] > peakHeartRate.value!!) {
                        peakHeartRate.value = event.values[0].roundToInt()
                    }
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
        calculateCalories.value = false
        sensorManager?.unregisterListener(heartRateListener)
        Log.v("HRR", "Monitoring Stopped")
    }

    fun toggleIsPaused(context: Context){
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)

        vibrator.vibrate(vibrationEffect)

        isPaused.value = !isPaused.value
        val maxHR = (211 - (0.64 * age.value!!).roundToInt()).toFloat()

        Log.v("HRR", "Toggling isPaused - ${isPaused.value}")

        Log.v("HRR", "Heart Rate: ${heartRate.value}")
        Log.v("HRR", "Max HR: $maxHR")
        Log.v("HRR", "Age: ${age.value}")
        if (heartRate.value != null && age.value != 0) {
            if (isPaused.value && heartRate.value!! >= (maxHR * 0.5)) {
                startHRRMeasurementIfStationary()
            }
        }
    }

    fun monitorAccelerometer(context: Context) {
        var acceleration: Float
        val measurements = mutableListOf<Float>()

        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerometerListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val magnitude = sqrt((x * x + y * y + z * z).toDouble()) - 9.81
                    acceleration = magnitude.toFloat()

                    measurements.add(acceleration)

                    if (measurements.size > 300) {
                        measurements.removeAt(0)
                    }

                    val averageAcceleration = measurements.average()

                    if (averageAcceleration < 2.75) {
                        Log.v("HRR", "isPaused: ${isPaused.value}")
                        if (!isPaused.value){
                            Log.v("HRR", "isPaused: ${isPaused.value}")
                            toggleIsPaused(context)
                        }
                        isPaused.value = true
                    }
                    else{
                        if (isPaused.value){
                            toggleIsPaused(context)
                        }
                        isPaused.value = false
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            accelerometerListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        Log.v("HRR", "Accelerometer Registered")
    }

    fun stopAccelerometer(){
        Log.v("HeartRateMonitorViewModel", "stopAccelerometer called")
        if (sensorManager == null) {
            Log.v("HeartRateMonitorViewModel", "sensorManager is null")
        } else if (accelerometerListener == null) {
            Log.v("HeartRateMonitorViewModel", "accelerometerListener is null")
        } else {
            sensorManager?.unregisterListener(accelerometerListener)
            Log.v("HeartRateMonitorViewModel", "accelerometerListener unregistered ${
                sensorManager?.unregisterListener(
                    accelerometerListener
                )
            
            }")
        }
    }

    private fun calculateCaloriesBurned(){
        viewModelScope.launch {
            var user: User
            var age: Int
            CoroutineScope(Dispatchers.IO).launch{
                user = userDao.getUserById("user")
                val today = LocalDate.now()
                val birthdate = user.birthday

                age = today.year - birthdate.year

                if (today.monthValue < birthdate.monthValue ||
                    (today.monthValue == birthdate.monthValue && today.dayOfMonth < birthdate.dayOfMonth)) {
                    age -= 1
                }
                CoroutineScope(Dispatchers.Main).launch{
                    Log.v("Calorie Calculation", "Gender is ${user.gender}")
                    while (calculateCalories.value!!){

                        if (user.gender == "male") {
                            if (heartRate.value != null){
                                caloriesBurned.value = caloriesBurned.value?.plus(
                                    ((-55.0969F + (0.6309F * heartRate.value!!) + (0.1988F * user.weight) + (0.2017F * age)) / 4.184F / 60)
                                )
                            }
                        }

                        else if (user.gender == "female") {
                            if (heartRate.value != null){
                                caloriesBurned.value = caloriesBurned.value?.plus(
                                    ((-20.4022F + (0.4472F * heartRate.value!!) - (0.1263F * user.weight) + (0.074F * age)) / 4.184F / 60)
                                )
                            }

                        }

                        else{
                            if (heartRate.value != null){
                                caloriesBurned.value = caloriesBurned.value?.plus(
                                    ((-55.0969F + (0.6309F * heartRate.value!!) + (0.1988F * user.weight) + (0.2017F * age)) / 4.184F / 60) + ((-20.4022F + (0.4472F * heartRate.value!!) - (0.1263F * user.weight) + (0.074F * age)) / 4.184F / 60) / 2
                                )
                            }
                        }
                        delay(1000L)
                    }
                }
            }
        }
    }

    fun startHRRMeasurementIfStationary() {
        _progress.value = 0f
        midWorkoutHRR.value = midWorkoutHRR.value?.plus(peakHeartRate.value!!)
        Log.v("HRR", "Added Peak Heart Rate to midWorkoutHRR: ${midWorkoutHRR.value}")
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
                        if (tempHeartRates.size >= 25) {
                            midWorkoutHRR.value = midWorkoutHRR.value?.plus(tempHeartRates.first())
                            tempHeartRates.clear()
                        }
                    }
                    delay(10L)
                }

                heartRateList.value = midWorkoutHRR.value
                midWorkoutHRR.value = midWorkoutHRR.value?.plus(-1)
                peakHeartRate.value = 0
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

    fun setMaxProgress(){
        _progress.value = 1f
    }
}