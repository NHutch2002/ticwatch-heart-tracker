package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HRRReviewPage(HRRValues: List<Int>){

    val splitHRRs = splitListBySeparator(HRRValues, -1).reversed()

    val verticalPagerState = rememberPagerState(initialPage = 0)

    Log.v("HRRReview", splitHRRs.size.toString())
    Log.v("HRRReview", HRRValues.size.toString())
    Log.v("HRRReview", HRRValues.find { it == -1 }.toString())
    Log.v("HRRReview", splitListBySeparator(HRRValues, -1).size.toString())


    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(splitHRRs.size) {index ->
                    PageIndicator(isSelected = verticalPagerState.currentPage == index)
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (HRRValues.isEmpty()) {
                Text(text = "Please wait...")
            } else {
                VerticalPager(state = verticalPagerState, count = splitHRRs.size) { page ->
                    HRRBarChart(splitHRRs[page])
                }
            }
        }
    }
}