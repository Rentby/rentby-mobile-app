package com.rentby.rentbymobile.helper

import android.os.Build
import androidx.annotation.RequiresApi
import com.rentby.rentbymobile.data.response.RentEnd
import com.rentby.rentbymobile.data.response.RentStart
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

fun millisecondsToDateString(millis: Long): String {
    val date = Date(millis)
    val timeZone = TimeZone.getDefault()
    val sdf = SimpleDateFormat("MMMM d, yyyy 'at' h:mm:ss a 'UTC'XXX", Locale.getDefault())
    sdf.timeZone = timeZone
    return sdf.format(date)
}

fun dateToDay(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val date = inputFormat.parse(dateString)
    return date?.let { outputFormat.format(it) } ?: ""
}

fun formatDateToReadable(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val date = inputFormat.parse(dateString)
    return date?.let { outputFormat.format(it) } ?: ""
}

fun calculateDayFromDate(startDateString: String, endDateString: String): Int {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    try {
        val startDate = sdf.parse(startDateString)
        val endDate = sdf.parse(endDateString)

        if (startDate != null && endDate != null) {
            val startMillis = startDate.time
            val endMillis = endDate.time

            val daysDifference = ((endMillis - startMillis) / (1000 * 60 * 60 * 24)).toInt()

            return daysDifference + 1 // Adding 1 to include both start and end dates
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return 0 // Default return if there's an error or invalid input
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertRentTimestamp(rent: RentStart?): String {
    return if (rent != null) {
        val seconds = rent.seconds ?: 0
        val nanoseconds = rent.nanoseconds ?: 0
        val instant = Instant.ofEpochSecond(seconds.toLong(), nanoseconds.toLong())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.of("GMT+08:00"))
        formatter.format(instant)
    } else {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertRentTimestamp(rent: RentEnd?): String {
    return if (rent != null) {
        val seconds = rent.seconds ?: 0
        val nanoseconds = rent.nanoseconds ?: 0
        val instant = Instant.ofEpochSecond(seconds.toLong(), nanoseconds.toLong())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.of("GMT+08:00"))
        formatter.format(instant)
    } else {
        ""
    }
}
