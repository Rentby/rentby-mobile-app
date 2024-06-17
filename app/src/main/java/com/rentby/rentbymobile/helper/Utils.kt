package com.rentby.rentbymobile.helper

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatStringtoRP(nominal: String): String {
    return "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal.toInt())
}

fun formatInttoRp(nominal: Int): String {
    return "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal)
}

fun formatStringtoMoney(nominal: String): String {
    return NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal.toInt())
}

fun formatInttoMoney(nominal: Int): String {
    return NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal)
}


fun calculateDay(start: Long, end: Long): Int {
    return ((end - start) / (1000 * 60 * 60 * 24)).toInt() + 1
}

fun millisecondsToDate(date: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val resultDate = Date(date)
    return sdf.format(resultDate)
}

fun millisecondsToDay(date: Long): String {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    val resultDate = Date(date)
    return sdf.format(resultDate)
}

fun dateToMilliseconds(dateString: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = sdf.parse(dateString)
    return date?.time ?: 0L
}

fun dateToDay(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val date = inputFormat.parse(dateString)
    return date?.let { outputFormat.format(it) } ?: ""
}