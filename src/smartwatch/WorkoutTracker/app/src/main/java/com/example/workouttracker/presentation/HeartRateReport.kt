package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.compose.material.Text
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt


@Composable
fun HeartRateReport(heartRates: List<Int>) {
    val entries = arrayListOf<BarEntry>()

    val totalReadings = heartRates.size
    val bucketInterval = (totalReadings - 2) / 28
    val normalisedEntries = arrayListOf<Int>()
    val excessReadingsPerBucket = (totalReadings - 2) % 28

    if (heartRates.size > 30){
        normalisedEntries.add(heartRates.first())

        var currentIndex = 1
        for (i in 1..28){
            val start = currentIndex
            val end = start + bucketInterval + if (i <= excessReadingsPerBucket) 1 else 0
            val average = heartRates.subList(start, end).average()
            normalisedEntries.add(average.roundToInt())
            currentIndex = end
        }

        normalisedEntries.add(heartRates.last())

        for (i in 0..29){
            entries.add(BarEntry(i.toFloat(), normalisedEntries[i].toFloat()))
        }
    }

    else {
        normalisedEntries.addAll(heartRates)
        for (i in normalisedEntries.indices){
            entries.add(BarEntry(i.toFloat(), normalisedEntries[i].toFloat()))
        }
    }

    Log.v("HeartRateReport", "Normalised entries: ${normalisedEntries.toList()}")


    val dataset = BarDataSet(entries, "Heart Rate")
    dataset.setDrawValues(false)
    dataset.color = android.graphics.Color.DKGRAY

    val barData = BarData(dataset)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "HR Range",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF111111))
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (normalisedEntries.isNotEmpty()){
                Text(
                    text = "${normalisedEntries.min()} - ${normalisedEntries.max()} BPM",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            else{
                Text(
                    text = "No heart rate data",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(90.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BPM",
                    modifier = Modifier.graphicsLayer(rotationZ = -90f),
                    style = TextStyle(fontSize = 8.sp, color = Color.White)
                )
                AndroidView(
                    { context ->
                        BarChart(context).apply {
                            data = barData
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.textColor = android.graphics.Color.WHITE
                            xAxis.setLabelCount(2, true)
                            xAxis.axisMinimum = 0f
                            xAxis.axisMaximum = 29f
                            xAxis.valueFormatter = object : ValueFormatter() {
                                override fun getFormattedValue(value: Float): String {
                                    return when (value) {
                                        0f -> "0s"
                                        29f -> "60s"
                                        else -> ""
                                    }
                                }
                            }
                            data.barWidth = 0.7f

                            xAxis.setDrawLabels(true)
                            xAxis.setDrawAxisLine(false)
                            xAxis.setDrawGridLines(false)
                            axisLeft.textColor = android.graphics.Color.WHITE
                            axisRight.isEnabled = false
                            legend.isEnabled = false
                            description.isEnabled = false
                            animateX(2000)

                            val customRenderer =
                                CustomBarChartRenderer(this, animator, viewPortHandler)
                            renderer = customRenderer

                            invalidate()
                        }
                    }, modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Text(
                text = "Time (s)",
                style = TextStyle(fontSize = 8.sp, color = Color.White),
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

