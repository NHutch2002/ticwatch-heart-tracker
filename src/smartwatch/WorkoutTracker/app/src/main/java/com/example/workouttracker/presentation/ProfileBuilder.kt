package com.example.workouttracker.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.first
import java.util.Locale

@Composable
fun DatePickerDialog(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val daysInMonth = selectedDate.month.length(selectedDate.isLeapYear)
    val days = (1..daysInMonth).toList()
    val months = Month.values().toList()
    val currentYear = LocalDate.now().year
    val years = (1900..currentYear).toList().reversed()

    var isDayDropdownExpanded by remember { mutableStateOf(false) }
    var isMonthDropdownExpanded by remember { mutableStateOf(false) }
    var isYearDropdownExpanded by remember { mutableStateOf(false) }

    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 8.dp)
        ){
            Text(text = "Day", fontSize = 10.sp, color = Color.White)
            Button(
                onClick = { isDayDropdownExpanded = true },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray
                )
            ) {
                Text(text = selectedDate.dayOfMonth.toString(), color = Color.White)
            }
            DropdownMenu(
                expanded = isDayDropdownExpanded,
                onDismissRequest = { isDayDropdownExpanded = false }
            ) {
                days.forEach { day ->
                    DropdownMenuItem(onClick = {
                        onDateChange(selectedDate.withDayOfMonth(day))
                        isDayDropdownExpanded = false
                    }) {
                        Text(text = day.toString())
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Month", fontSize = 10.sp, color = Color.White)
            Button(
                onClick = { isMonthDropdownExpanded = true },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray
                )
            )  {
                Text(
                    text = selectedDate.month.name.substring(0, 3).lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    color = Color.White
                )
            }
            DropdownMenu(
                expanded = isMonthDropdownExpanded,
                onDismissRequest = { isMonthDropdownExpanded = false }
            ) {
                months.forEach { month ->
                    DropdownMenuItem(onClick = {
                        onDateChange(selectedDate.withMonth(month.ordinal + 1))
                        isMonthDropdownExpanded = false
                    }) {
                        Text(text = month.name.substring(0, 3).lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
                    }
                }
            }

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = "Year", fontSize = 10.sp, color = Color.White)
            Button(
                onClick = { isYearDropdownExpanded = true },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray
                )
            )  {
                Text(text = selectedDate.year.toString(), color = Color.White)
            }
            DropdownMenu(
                expanded = isYearDropdownExpanded,
                onDismissRequest = { isYearDropdownExpanded = false }
            ) {
                years.forEach { year ->
                    DropdownMenuItem(onClick = {
                        onDateChange(selectedDate.withYear(year))
                        isYearDropdownExpanded = false
                    }) {
                        Text(text = year.toString())
                    }
                }
            }
        }
    }
}



@Composable
fun EnterBirthdayPage(navController: NavController) {
    val name by remember { mutableStateOf("user") }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val onDateChange: (LocalDate) -> Unit = { newDate ->
        selectedDate = newDate
    }

    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Please Enter\nYour Birthday", fontSize = 20.sp, textAlign = TextAlign.Center, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        DatePickerDialog(selectedDate, onDateChange)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val user = User(name, selectedDate, 0)
                    userDao.insertUser(user)
                    val users = userDao.getAllUsers()
                    withContext(Dispatchers.Main) {
                        if (users.first().isNotEmpty()){
                            navController.navigate("profile_weight")
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF49a72f)
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}



//@Composable
//fun NumberPicker(
//    numbers: List<Int>,
//    onNumberSelected: (Int) -> Unit,
//    selectedNumber: Int
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        LazyColumn(
//            modifier = Modifier.height(64.dp)
//        ) {
//            items(numbers) { number ->
//                val isSelected = number == selectedNumber
//                val backgroundColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
//                val contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
//                Box(
//                    modifier = Modifier
//                        .selectable(
//                            selected = isSelected,
//                            onClick = { onNumberSelected(number) }
//                        )
//                        .background(backgroundColor)
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                        .width(20.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = number.toString(),
//                        style = MaterialTheme.typography.body1,
//                        color = contentColor
//                    )
//                }
//            }
//        }
//    }
//}






