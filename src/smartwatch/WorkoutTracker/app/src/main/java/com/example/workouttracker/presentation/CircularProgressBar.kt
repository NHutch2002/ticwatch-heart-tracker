package com.example.workouttracker.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = 2.dp
) {
    Canvas(modifier = modifier) {
        val innerRadius = size.minDimension / 2
        val halfSize = size / 2F
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        val startAngle = -90f
        val sweep = progress * 360f

        // Draw the progress bar with rounded cap
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweep,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        // Calculate the position of the end of the progress bar
        val endAngle = sweep - 90f
        val endX = halfSize.width + innerRadius * cos(endAngle * PI / 180f)
        val endY = halfSize.height + innerRadius * sin(endAngle * PI / 180f)

        // Draw a small white circle at the end of the progress bar
        drawCircle(
            color = Color.White,
            radius = strokeWidth.toPx(),
            center = Offset(endX.toFloat(), endY.toFloat())
        )
    }
}
