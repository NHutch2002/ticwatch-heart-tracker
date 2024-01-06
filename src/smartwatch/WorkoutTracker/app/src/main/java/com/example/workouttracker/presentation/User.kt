package com.example.workouttracker.presentation

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val name: String,
    val birthday: LocalDate,
    var weight: Int,
)
