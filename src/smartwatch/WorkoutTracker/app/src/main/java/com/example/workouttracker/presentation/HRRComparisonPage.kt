package com.example.workouttracker.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HRRComparisonPage(navController: NavController, hrr1String: String, hrr2String: String){
    val hrr1 = hrr1String.replace("[", "").replace("]", "").split(",").map { it.trim().toInt() }
    val hrr2 = hrr2String.replace("[", "").replace("]", "").split(",").map { it.trim().toInt() }

    val pagerState = rememberPagerState {4}


    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> HRRCompareGraph(hrr1, hrr2)
                1 -> HRRBarChart(hrr1, navController, false)
                2 -> HRRBarChart(hrr2, navController, false)
                3 -> ReturnHomePage(navController, hrr1)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    repeat(pagerState.pageCount) { index ->
                        PageIndicator(isSelected = pagerState.currentPage == index)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

}


@Composable
fun HRRCompareGraph(hrr1: List<Int>, hrr2: List<Int>) {

    val entries1 = normaliseEntries(hrr1)
    val entries2 = normaliseEntries(hrr2)

    val dataset1 = BarDataSet(entries1, "HRR1")
    val dataset2 = BarDataSet(entries2, "HRR2")

    dataset1.setDrawValues(false)
    dataset1.color = 0x88a70005.toInt()
    dataset2.setDrawValues(false)
    dataset2.color = 0x880500a7.toInt()

    val barData = BarData(dataset1, dataset2)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "HRR Compare",
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
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(color = Color(0xFFa70005), radius = size.minDimension / 2)
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${hrr1.first().toInt() - hrr1.last().toInt()}",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "vs",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(color = Color(0xFF0500a7), radius = size.minDimension / 2)
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${hrr2.first().toInt() - hrr2.last().toInt()} BPM",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
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
                                CustomHRRCompareBarChartRenderer(
                                    this,
                                    animator,
                                    viewPortHandler,
                                )
                            renderer = customRenderer

                            invalidate()
                        }
                    }, modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Text(
                text = "Time",
                style = TextStyle(fontSize = 8.sp, color = Color.White),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

}


private fun normaliseEntries(heartRateRecoverySamples: List<Int>): ArrayList<BarEntry> {
    val totalReadings = heartRateRecoverySamples.size
    val bucketInterval = (totalReadings.minus(2)).div(28)
    val normalisedEntries = arrayListOf<Int>()
    val excessReadingsPerBucket = (totalReadings.minus(2)).rem(28)

    var currentIndex = 1


    normalisedEntries.add(heartRateRecoverySamples.first())


    for (i in 1..28){
        val start = currentIndex
        val end = start + bucketInterval + if (i <= excessReadingsPerBucket) 1 else 0
        val average = heartRateRecoverySamples.subList(start, end).average()
        normalisedEntries.add(average.toInt())
        currentIndex = end
    }


    normalisedEntries.add(heartRateRecoverySamples.last())


    val entries = arrayListOf<BarEntry>()
    for (i in 0..29){
        entries.add(BarEntry(i.toFloat(), normalisedEntries[i].toFloat()))
    }

    return entries
}

@Composable
fun ReturnHomePage(navController: NavController, hrr1: List<Int>){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("choose_compare/${hrr1}") },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray
            ),
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(text = "Return to\nCompare")
    }
}