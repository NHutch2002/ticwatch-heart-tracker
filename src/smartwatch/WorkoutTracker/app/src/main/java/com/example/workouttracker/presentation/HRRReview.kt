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
fun HRRReviewPage(HRRValues: List<Int>?, navController: NavController){

    val safeHRRValues = HRRValues?: listOf(0)

    val splitHRRs = splitListBySeparator(safeHRRValues, -1).reversed()

    val verticalPagerState = rememberPagerState(initialPage = 0)

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
                if (splitHRRs.size > 1){
                    repeat(splitHRRs.size) {index ->
                        PageIndicator(isSelected = verticalPagerState.currentPage == index)
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            if (safeHRRValues.contains(0)){
                Text(
                    text = "Please wait...",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                )
            }

            else if (safeHRRValues.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "HR Recovery",
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

            else {
                VerticalPager(state = verticalPagerState, count = splitHRRs.size) { page ->
                    HRRBarChart(splitHRRs[page], navController, true)
                }
            }
        }
    }
}