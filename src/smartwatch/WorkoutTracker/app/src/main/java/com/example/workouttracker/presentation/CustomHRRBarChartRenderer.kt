package com.example.workouttracker.presentation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomHRRBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val peakHR: Float,
) : BarChartRenderer(chart, animator, viewPortHandler) {
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#a70005")
        style = Paint.Style.FILL
    }

    private val zonePaint = Paint().apply {
        color = Color.GREEN
        alpha = 75
    }

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        super.drawDataSet(c, dataSet, index)
        val buffer = mBarBuffers[index].buffer
        for (j in buffer.indices step 4) {
            val left = buffer[j]
            val top = buffer[j + 1]
            val right = buffer[j + 2]

            val x = (left + right) / 2
            val y = top

            c.drawCircle(x, y, 4f, circlePaint)
        }
    }

    override fun drawData(c: Canvas) {
        super.drawData(c)

        val contentRect = mViewPortHandler.contentRect
        val yellowZoneTop = getYPos((mChart as BarChart).axisLeft.axisMaximum)
        val lightGreenZoneTop = getYPos(peakHR - 13)
        val darkGreenZoneTop = getYPos(peakHR - 25)
        val darkGreenZoneBottom = getYPos((mChart as BarChart).axisLeft.axisMinimum)

        val yellowZonePaint = Paint().apply {
            color = 0x88ffff00.toInt()
            alpha = 50
        }

        val lightGreenZonePaint = Paint().apply {
            color = 0x8892d050.toInt()
            alpha = 50
        }

        val darkGreenZonePaint = Paint().apply {
            color = 0x88006400.toInt()
            alpha = 50
        }

        c.drawRect(contentRect.left, yellowZoneTop, contentRect.right, lightGreenZoneTop, yellowZonePaint)
        c.drawRect(contentRect.left, lightGreenZoneTop, contentRect.right, darkGreenZoneTop, lightGreenZonePaint)
        c.drawRect(contentRect.left, darkGreenZoneTop, contentRect.right, darkGreenZoneBottom, darkGreenZonePaint)
    }

    private fun getYPos(value: Float): Float {
        val yAxis = (mChart as BarChart).axisLeft
        val yAxisScale = mViewPortHandler.scaleY
        val contentRect = mViewPortHandler.contentRect
        val scaledValue = (value - yAxis.axisMinimum) / (yAxis.axisMaximum - yAxis.axisMinimum)
        return contentRect.bottom - scaledValue * contentRect.height() * yAxisScale
    }
}