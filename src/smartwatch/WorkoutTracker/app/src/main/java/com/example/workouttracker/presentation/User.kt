package com.example.workouttracker.presentation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val name: String,
    val age: Int,
    val weight: Int,
)
