package com.example.workouttracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeartRateInfoScreen(navController: NavController) {

    val verticalPagerState = rememberPagerState(initialPage = 0)

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(Color.Transparent)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(6) {index ->
                    PageIndicator(isSelected = verticalPagerState.currentPage == index)
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .background(Color.Transparent)
            )
            Text(
                text = "Heart Rate Zones",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.background(Color.Transparent)
            )
        }

        VerticalPager(
            state = verticalPagerState,
            modifier = Modifier.fillMaxSize(),
            count = 6
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x88990099)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 1 - Sedentary",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "< 50% of max heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level represents a\nhealthy resting heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x8800b0f0)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 2 - Very Light",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "50-60% of max heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level improves overall\nhealth and helps recovery",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x8892d050)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 3 - Light",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "60-70% of max heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level improves basic\nendurance and fat burning",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                3 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x88ffff00)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 4 - Moderate",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "70-80% of max heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level improves aerobic\nfitness",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                4 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x88ffc000)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 5 - Hard",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "80-90% of max heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level increases maximum\nperformance capacity",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                5 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x88a70005)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 6 - Extreme",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "90-100% of max heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level develops maximum\nperformance and speed",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

//
//LazyColumn{
//    item {
//        Text(
//            text = "Heart Rate Zones",
//            fontSize = 24.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//    item {
//        Text(
//            text = "Zone 1: 50-60% of max heart rate",
//            fontSize = 18.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//    item {
//        Text(
//            text = "Zone 2: 60-70% of max heart rate",
//            fontSize = 18.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//    item {
//        Text(
//            text = "Zone 3: 70-80% of max heart rate",
//            fontSize = 18.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//    item {
//        Text(
//            text = "Zone 4: 80-90% of max heart rate",
//            fontSize = 18.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//    item {
//        Text(
//            text = "Zone 5: 90-100% of max heart rate",
//            fontSize = 18.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}