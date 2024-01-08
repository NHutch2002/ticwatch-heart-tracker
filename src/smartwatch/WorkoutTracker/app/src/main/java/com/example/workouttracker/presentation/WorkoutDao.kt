package com.example.workouttracker.presentation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workout WHERE id = :id")
    fun getWorkoutById(id: Int): Workout

    @Insert
    fun insertWorkout(workout: Workout)

    @Update
    fun updateWorkout(workout: Workout)

    @Delete
    fun deleteWorkout(workout: Workout)
}