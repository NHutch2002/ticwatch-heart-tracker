package com.example.workouttracker.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.wear.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch
import androidx.compose.foundation.selection.selectable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@Composable
fun EnterNamePage(navController: NavController) {
    val name by remember { mutableStateOf("user") }

    val selectedNumber = remember { mutableIntStateOf(0) }
    val numbers = (1..100).toList()

    val db = AppDatabase.getInstance(LocalContext.current)
    val userDao = db.userDao()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Please\nenter your age:", fontSize = 20.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        NumberPicker(
            numbers = numbers,
            onNumberSelected = { selectedNumber.intValue = it },
            selectedNumber = selectedNumber.intValue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    Log.v("EnterNamePage", name)
                    Log.v("EnterNamePage", selectedNumber.intValue.toString())
                    val user = User(name, selectedNumber.intValue, 50)
                    userDao.insertUser(user)
                    val users = userDao.getAllUsers()
                    Log.v("EnterNamePage", "userDao.insertUser(user)")
                    Log.v("EnterNamePage", "userDao.getAllUsers(): $users")
                    withContext(Dispatchers.Main) {
                        while(users.first().isEmpty()){
                            delay(100)
                            Log.v("EnterNamePage", "users.first().isEmpty()")
                        }
                        if (users.first().isNotEmpty()){
                            Log.v("EnterNamePage", "users.first().isNotEmpty()")
                            navController.navigate("landing_page")
                        }
                    }
                }
            }
        ) {
            Text("Submit")
        }
    }
}
@Composable
fun NumberPicker(
    numbers: List<Int>,
    onNumberSelected: (Int) -> Unit,
    selectedNumber: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        LazyColumn(
            modifier = Modifier.height(64.dp)
        ) {
            items(numbers) { number ->
                val isSelected = number == selectedNumber
                val backgroundColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                val contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                Box(
                    modifier = Modifier
                        .selectable(
                            selected = isSelected,
                            onClick = { onNumberSelected(number) }
                        )
                        .background(backgroundColor)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .width(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        style = MaterialTheme.typography.body1,
                        color = contentColor
                    )
                }
            }
        }
    }
}






