package com.example.workouttracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 8.dp else 4.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color.White else Color.Gray)
    )
}