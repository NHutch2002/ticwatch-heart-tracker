package com.example.workouttracker.presentation

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: LocalDate,
    var heartRates: List<Int>,
    var HRRs: List<Int>,
)

