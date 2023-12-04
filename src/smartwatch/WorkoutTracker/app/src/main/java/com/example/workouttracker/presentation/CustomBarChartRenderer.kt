package com.example.workouttracker.presentation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#a70005")
        style = Paint.Style.FILL
    }

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        super.drawDataSet(c, dataSet, index)

        val buffer = mBarBuffers[index].buffer
        for (j in buffer.indices step 4) {
            val left = buffer[j]
            val top = buffer[j + 1]
            val right = buffer[j + 2]

            // Calculate the center of the top of the bar
            val x = (left + right) / 2
            val y = top

            // Draw the circle on top of the bar
            c.drawCircle(x, y, 4f, circlePaint) // 10f is the radius of the circle
        }
    }
}