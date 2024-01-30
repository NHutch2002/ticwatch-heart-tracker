package com.example.workouttracker.presentation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomHRRCompareBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
) : BarChartRenderer(chart, animator, viewPortHandler) {
    private val circlePaint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#a70005")
        style = Paint.Style.FILL
    }

    private val circlePaint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0500a7")
        style = Paint.Style.FILL
    }

    override fun drawData(c: Canvas) {
        for (i in mChart.barData.dataSets.indices) {
            val dataSet = mChart.barData.getDataSetByIndex(i)
            if (dataSet.isVisible) {
                drawDataSet(c, dataSet, i)
            }
        }

        for (i in mChart.barData.dataSets.indices) {
            val dataSet = mChart.barData.getDataSetByIndex(i)
            if (dataSet.isVisible) {
                drawCircles(c, dataSet, i)
            }
        }
    }

    private fun drawCircles(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val buffer = mBarBuffers[index].buffer
        val paint = if (index == 0) circlePaint1 else circlePaint2
        for (j in buffer.indices step 4) {
            val left = buffer[j]
            val top = buffer[j + 1]
            val right = buffer[j + 2]

            val x = (left + right) / 2
            val y = top

            c.drawCircle(x, y, 4f, paint)
        }
    }
}