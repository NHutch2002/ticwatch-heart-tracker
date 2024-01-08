package com.example.workouttracker.presentation

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }

    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return if (value.isEmpty()) {
            emptyList()
        } else {
            value.split(",").mapNotNull { it.toIntOrNull() }
        }
    }
}