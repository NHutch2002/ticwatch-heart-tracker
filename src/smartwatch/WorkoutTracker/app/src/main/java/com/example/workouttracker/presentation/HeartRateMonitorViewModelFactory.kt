package com.example.workouttracker.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HeartRateMonitorViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeartRateMonitorViewModel::class.java)) {
            return HeartRateMonitorViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}