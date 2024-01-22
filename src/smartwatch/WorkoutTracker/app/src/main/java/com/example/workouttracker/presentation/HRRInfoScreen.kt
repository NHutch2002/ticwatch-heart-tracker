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
fun HRRInfoScreen(navController: NavController){

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
                repeat(3) {index ->
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
                    .height(18.dp)
                    .background(Color.Transparent)
            )
            Text(
                text = "HRR Zones",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.background(Color.Transparent)
            )
        }

        VerticalPager(
            state = verticalPagerState,
            modifier = Modifier.fillMaxSize(),
            count = 3
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x88ffff00)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 1 - Low HRR",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Recovery of less than 13 BPM\nfrom peak heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level is associated with\npoor fitness and\nathletic performance",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x8892d050)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 2 - Healthy HRR",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Recovery of 13-25 BPM\nfrom peak heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level is associated with\naverage fitness and\nathletic performance",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x88006400)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Zone 3 - High HRR",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Recovery of more than 25 BPM\nfrom peak heart rate",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This level is associated with\nimproved fitness and\nathletic performance",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}