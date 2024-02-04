package com.example.workouttracker.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt


@Composable
fun HeartRateReport(heartRates: List<Int>?, age: Int, totalTime: Long, navController: NavController) {

    val safeHeartRates = heartRates?: emptyList()

    val entries = arrayListOf<BarEntry>()

    val totalReadings = safeHeartRates.size
    val bucketInterval = (totalReadings - 2) / 28
    val normalisedEntries = arrayListOf<Int>()
    val excessReadingsPerBucket = (totalReadings - 2) % 28

    if (safeHeartRates.isNotEmpty()){
        val averageHeartRate = safeHeartRates.average().roundToInt()

        if (safeHeartRates.size > 30){
            normalisedEntries.add(safeHeartRates.first())

            var currentIndex = 1
            for (i in 1..28){
                val start = currentIndex
                val end = start + bucketInterval + if (i <= excessReadingsPerBucket) 1 else 0
                val average = safeHeartRates.subList(start, end).average()
                normalisedEntries.add(average.roundToInt())
                currentIndex = end
            }

            normalisedEntries.add(safeHeartRates.last())

            for (i in 0..29){
                entries.add(BarEntry(i.toFloat(), normalisedEntries[i].toFloat()))
            }
        }

        else {
            normalisedEntries.addAll(safeHeartRates)
            for (i in normalisedEntries.indices){
                entries.add(BarEntry(i.toFloat(), normalisedEntries[i].toFloat()))
            }
        }


        val dataset = BarDataSet(entries, "Heart Rate")
        dataset.setDrawValues(false)
        dataset.color = android.graphics.Color.DKGRAY

        val barData = BarData(dataset)

        val maxHR = (211 - (0.64 * age).roundToInt()).toFloat()

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (normalisedEntries.isNotEmpty()) {
                        Text(
                            text = "${normalisedEntries.min()} - ${normalisedEntries.max()} BPM",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                navController.navigate("heart_rate_info_screen")
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.DarkGray,
                            ),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "No heart rate data",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
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
                                xAxis.setLabelCount(30, false)
                                xAxis.axisMinimum = 0f
                                xAxis.axisMaximum = 29f
                                xAxis.valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return when (value) {
                                            in 3.5f..4.5f -> "00:00:00"
                                            in 24.5f..25.5f -> "%02d:%02d:%02d".format(
                                                totalTime / 3600,
                                                (totalTime % 3600) / 60,
                                                totalTime % 60
                                            )
                                            else -> ""
                                        }
                                    }
                                }
                                data.barWidth = 0.7f

                                val avgHeartRateLine = LimitLine(averageHeartRate.toFloat(), "$averageHeartRate Avg")
                                avgHeartRateLine.lineColor = Color.Gray.toArgb()
                                avgHeartRateLine.lineWidth = 1f
                                avgHeartRateLine.textColor = Color.Gray.toArgb()
                                avgHeartRateLine.textSize = 10f
                                avgHeartRateLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP

                                axisLeft.addLimitLine(avgHeartRateLine)

                                xAxis.setDrawLabels(true)
                                xAxis.setDrawAxisLine(false)
                                xAxis.setDrawGridLines(false)
                                axisLeft.textColor = android.graphics.Color.WHITE
                                axisRight.isEnabled = false
                                legend.isEnabled = false
                                description.isEnabled = false
                                animateX(2000)

                                val customRenderer =
                                    CustomHeartRateBarChartRenderer(this, animator, viewPortHandler, maxHR)
                                renderer = customRenderer

                                invalidate()
                            }
                        }, modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
                Text(
                    text = "Time",
                    style = TextStyle(fontSize = 8.sp, color = Color.White),
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "HR Range",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No data\nto show",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

