package com.example.workouttracker.presentation


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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter


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
                0 -> HRRPage(navController, maxHeartRate, viewModel)
                1 -> TestSecondPage()
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
fun HRRPage(navController: NavController, maxHeartRate: Float, viewModel: HeartRateMonitorViewModel) {


    val heartRates by viewModel.heartRates.observeAsState(emptyList())
    val heartRateRecovery by viewModel.heartRateRecovery.observeAsState(0)
    val progress by viewModel.progress.observeAsState(0f)



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgress(
            progress = progress,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            color = Color(0xFF9CF2F9),
            strokeWidth = 6.dp
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (progress < 1){
                Text(text="Measuring \n Heart Rate Recovery", fontSize = 14.sp, textAlign = TextAlign.Center)
                Text(text= "${(progress * 100).toInt()}%", fontSize = 64.sp, color = Color(0xFF9CF2F9))
                if (heartRates.isEmpty()){
                    Text(text = "Current Heart Rate: \n Reading...", fontSize = 14.sp, textAlign = TextAlign.Center)
                }
                else{
                    Text(text="Current Heart Rate: \n ${heartRates.takeLast(1)[0].toInt()} BPM", fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }
            else{
                Button(onClick = { navController.navigate("landing_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
                }
                Text(text = "Return to home page")
                Text("Heart Rate Recovery: $heartRateRecovery")
            }
        }
    }
}

@Composable
fun TestSecondPage() {

    val entries = arrayListOf<Entry>()

    for (i in 1..60) {
        val x = 150 - i/2
        if (i == 1 || i == 60 || i % 10 == 0) {
            entries.add(Entry(i.toFloat(), x.toFloat()))
        }
    }

    val dataset = LineDataSet(entries, "Heart Rate")
    dataset.setDrawValues(false)

    val labels = arrayListOf<String>()
    labels.add("0s")
    labels.add("60s")

    val lineData = LineData(dataset)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Second Page")
        AndroidView({ context ->
            LineChart(context).apply {
                data = lineData
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = android.graphics.Color.WHITE
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value) {
                            1f -> "0s"
                            60f -> "60s"
                            else -> ""
                        }
                    }
                }
                xAxis.setDrawLabels(true)
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawGridLines(false)
                axisLeft.textColor = android.graphics.Color.WHITE
                axisRight.textColor = android.graphics.Color.WHITE
                legend.isEnabled = false
                description.isEnabled = false

//                description.text = "# of times Alice called Bob"
//                description.textColor = android.graphics.Color.WHITE
                animateX(1000)
            }
        }, modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(150.dp)
            .padding(16.dp))
    }
}

@Composable
fun ReturnHomePage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Third Page")
        Button(onClick = { navController.navigate("landing_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9CF2F9))) {
            Text(text = "Return to home page")
        }
    }
}
