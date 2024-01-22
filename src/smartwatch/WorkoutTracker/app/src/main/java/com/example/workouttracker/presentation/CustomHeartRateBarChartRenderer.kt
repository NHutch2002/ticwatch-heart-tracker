package com.example.workouttracker.presentation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler


data class Zone(val lowerBound: Float, val upperBound: Float, val color: Int)

class CustomHeartRateBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val maxHR: Float
) : BarChartRenderer(chart, animator, viewPortHandler) {
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#a70005")
        style = Paint.Style.FILL
    }

    private val zonePaint: Paint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val zones = listOf(
        Zone(0f, 0.5f * maxHR, Color.parseColor("#990099")),
        Zone(0.5f * maxHR, 0.6f * maxHR, Color.parseColor("#00b0f0")),
        Zone(0.6f * maxHR, 0.7f * maxHR, Color.parseColor("#92d050")),
        Zone(0.7f * maxHR, 0.8f * maxHR, Color.parseColor("#ffff00")),
        Zone(0.8f * maxHR, 0.9f * maxHR, Color.parseColor("#ffc000")),
        Zone(0.9f * maxHR, 1.0f * maxHR, Color.parseColor("#a70005"))
    )

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        super.drawDataSet(c, dataSet, index)

        if (dataSet == null) {
            return
        }

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

        for (zone in zones) {
            val top = getYPos(zone.upperBound)
            val bottom = getYPos(zone.lowerBound)
            zonePaint.color = zone.color
            zonePaint.alpha = 50
            c.drawRect(contentRect.left, top, contentRect.right, bottom, zonePaint)
        }
    }

    private fun getYPos(value: Float): Float {
        val yAxis = (mChart as BarChart).axisLeft
        val yAxisScale = mViewPortHandler.scaleY
        val contentRect = mViewPortHandler.contentRect
        val scaledValue = (value - yAxis.axisMinimum) / (yAxis.axisMaximum - yAxis.axisMinimum)
        return contentRect.bottom - scaledValue * contentRect.height() * yAxisScale
    }
}