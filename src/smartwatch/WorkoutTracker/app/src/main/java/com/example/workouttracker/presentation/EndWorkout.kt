package com.example.workouttracker.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EndWorkoutPage(navController: NavController, maxHeartRate: Float) {
    val pagerState = rememberPagerState { 3 } // Replace 2 with your actual page count

    val context = LocalContext.current
    val viewModel: HeartRateMonitorViewModel = viewModel()


    LaunchedEffect(Unit){
        viewModel.startHeartRateMonitoring(context, maxHeartRate)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> HRRPage(maxHeartRate, viewModel)
//                0 -> HRRBarChart()
                1 -> HeartRateReport()
                2 -> ReturnHomePage(navController)
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
fun HRRPage(maxHeartRate: Float, viewModel: HeartRateMonitorViewModel) {


    val heartRates by viewModel.heartRates.observeAsState(emptyList())
    val heartRateRecovery by viewModel.heartRateRecovery.observeAsState(0)
    val progress by viewModel.progress.observeAsState(0f)
    val measurementCompleted = progress >= 1f


    val animationDuration = 2000
    val textOpacity by animateFloatAsState(
        targetValue = if (measurementCompleted) 0f else 1f,
        animationSpec = TweenSpec(durationMillis = animationDuration)
    )
    val textTranslationY by animateDpAsState(
        targetValue = if (measurementCompleted) 100.dp else 0.dp,
        animationSpec = TweenSpec(durationMillis = animationDuration)
    )
    val graphTranslationY by animateDpAsState(
        targetValue = if (measurementCompleted) 0.dp else 100.dp,
        animationSpec = TweenSpec(durationMillis = animationDuration)
    )
    val graphOpacity by animateFloatAsState(
        targetValue = if (measurementCompleted) 1f else 0f,
        animationSpec = TweenSpec(durationMillis = animationDuration)
    )

    val circularProgressScale by animateFloatAsState(
        targetValue = if (measurementCompleted) 3f else 1f, // Scale up when completed
        animationSpec = TweenSpec(durationMillis = animationDuration)
    )
    val circularProgressOpacity by animateFloatAsState(
        targetValue = if (measurementCompleted) 0f else 1f, // Fade out when completed
        animationSpec = TweenSpec(durationMillis = animationDuration)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgress(
            progress = progress,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = circularProgressScale,
                    scaleY = circularProgressScale,
                    alpha = circularProgressOpacity
                )
                .fillMaxSize()
                .padding(10.dp),
            color = Color(0xFF9CF2F9),
            strokeWidth = 6.dp
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = !measurementCompleted) {
                Column(
                    modifier = Modifier.alpha(textOpacity).graphicsLayer { translationY = -textTranslationY.toPx() },
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text="Measuring \n Heart Rate Recovery", fontSize = 14.sp, textAlign = TextAlign.Center)
                    Text(text= "${(progress * 100).toInt()}%", fontSize = 64.sp, color = Color(0xFF9CF2F9))
                    if (heartRates.isEmpty()){
                        Text(text = "Current Heart Rate", fontSize = 14.sp, textAlign = TextAlign.Center)
                        Text(text = "Reading...", fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                    else{
                        Text(text="Current Heart Rate", fontSize = 14.sp, textAlign = TextAlign.Center)
                        Text(text="${heartRates.takeLast(1)[0].toInt()} BPM", fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
            AnimatedVisibility(visible = measurementCompleted) {
                Column(
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = graphOpacity
                            translationY = -graphTranslationY.toPx()
                        },
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HRRBarChart()
                }
            }
        }
    }
}

//@Composable
//fun LineChartPage() {
//
//    val entries = arrayListOf<Entry>()
//
//    for (i in 0..60) {
//        val x = 150 - i/2
////        if (i == 0 || i == 60 || i % 10 == 0) {
//            entries.add(Entry(i.toFloat(), x.toFloat()))
////        }
//    }
//
//    val dataset = LineDataSet(entries, "Heart Rate")
//    dataset.setDrawValues(false)
//    dataset.setDrawCircleHole(false)
//    dataset.setDrawCircles(false)
//    dataset.circleRadius = 2f
//    dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
//    dataset.cubicIntensity = 0.2f
//
//    val labels = arrayListOf<String>()
//    labels.add("0s")
//    labels.add("60s")
//
//    val lineData = LineData(dataset)
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "Line Graph")
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .height(100.dp)
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "BPM",
//                modifier = Modifier.graphicsLayer(rotationZ = -90f),
//                style = TextStyle(fontSize = 8.sp, color = Color.White)
//            )
//            AndroidView({ context ->
//                LineChart(context).apply {
//                    data = lineData
//                    xAxis.position = XAxis.XAxisPosition.BOTTOM
//                    xAxis.textColor = android.graphics.Color.WHITE
//                    xAxis.valueFormatter = object : ValueFormatter() {
//                        override fun getFormattedValue(value: Float): String {
//                            return when (value) {
//                                entries.first().x -> "0"
//                                entries.last().x -> "60"
//                                else -> ""
//                            }
//                        }
//                    }
//                    xAxis.setDrawLabels(true)
//                    xAxis.setDrawAxisLine(false)
//                    xAxis.setDrawGridLines(false)
//                    xAxis.setDrawLabels(true)
//                    axisLeft.textColor = android.graphics.Color.WHITE
//                    axisRight.isEnabled = false
//                    legend.isEnabled = false
//                    description.isEnabled = false
//                    animateX(1000)
//                }
//            }, modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight())
//        }
//        Text(
//            text = "Time (s)",
//            style = TextStyle(fontSize = 8.sp, color = Color.White)
//        )
//    }
//}

@Composable
fun ReturnHomePage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Third Page")
        Button(onClick = { navController.navigate("landing_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
            Text(text = "Return to home page")
        }
    }
}
