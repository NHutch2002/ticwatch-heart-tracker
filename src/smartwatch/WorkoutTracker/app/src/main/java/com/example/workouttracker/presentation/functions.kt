package com.example.workouttracker.presentation

fun splitListBySeparator(list: List<Int>, separator: Int): List<List<Int>> {
    var index = 0
    return list.groupBy { if (it == separator) index++ else index }
        .values
        .map { it.filter { element -> element != separator } }
        .filter { it.isNotEmpty() }
}