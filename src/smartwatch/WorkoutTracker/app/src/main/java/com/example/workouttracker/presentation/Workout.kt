package com.example.workouttracker.presentation

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val date: LocalDate,
    var activeTime: Long,
    var totalTime: Long,
    var heartRates: List<Int>,
    var HRRs: List<Int>,
    var calories: Int = 0
)

