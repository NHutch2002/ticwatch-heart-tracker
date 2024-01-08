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
    fun toIntList(data: String): List<Int> {
        return data.split(",").map { it.toInt() }
    }
}