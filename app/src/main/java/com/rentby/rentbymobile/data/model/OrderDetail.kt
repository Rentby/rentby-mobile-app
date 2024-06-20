package com.rentby.rentbymobile.data.model

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import com.rentby.rentbymobile.data.response.OrderDetailResponse
import com.rentby.rentbymobile.data.response.RentEnd
import com.rentby.rentbymobile.data.response.RentStart
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class OrderDetail(
    val id: String,
    val status: Int,
    val orderTime: String,
    val pickupTime: String,
    val returnTime: String,
    val snapToken: String,
    val payment: String,
    val sellerId: String,
    val sellerName: String,
    val pickupLocation: String,
    val productName: String,
    val rentStart: String,
    val rentEnd: String,
    val rentDuration: Int,
    val rentPrice: Float,
    val rentTotal: Float,
    val deposit: Float,
    val serviceFee: Float,
    val isRated: Boolean,

    val lateDuration: Int,
    val lateCharge: Int,

    val imageUrl: String
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromResponse(response: OrderDetailResponse): OrderDetail {
            return OrderDetail(
                id = response.orderId ?: "",
                status = response.status?.toIntOrNull() ?: 0,
                orderTime = response.orderTime ?: "",
                pickupTime = response.pickupTime ?: "",
                returnTime = response.returnTime ?: "",
                snapToken = response.snapToken ?: "",
                payment = response.paymentMethod ?: "",
                sellerId = response.sellerId ?: "",
                sellerName = response.sellerName ?: "",
                pickupLocation = response.pickupLocation ?: "",
                productName = response.productName ?: "",
                rentStart = convertRentTimestamp(response.rentStart),
                rentEnd = convertRentTimestamp(response.rentEnd),
                rentDuration = parseDuration(response.rentDuration) ?: 0,
                rentPrice = response.rentPrice?.toFloat() ?: 0f,
                rentTotal = response.rentTotal?.toFloat() ?: 0f,
                deposit = response.deposit?.toFloat() ?: 0f,
                serviceFee = response.serviceFee?.toFloat() ?: 0f,
                isRated = response.isRated?.toBoolean() ?: false,
                lateDuration = response.lateDuration ?: 0,
                lateCharge = response.lateCharge ?: 0,
                imageUrl = response.imageUrl ?: ""
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun convertRentTimestamp(rent: RentStart?): String {
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
        private fun convertRentTimestamp(rent: RentEnd?): String {
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

        private fun parseDuration(duration: String?): Int? {
            // This function converts a duration string like "1 Hari" to an integer representing the number of days.
            // You can expand this to handle more complex durations if needed.
            return duration?.split(" ")?.firstOrNull()?.toIntOrNull()
        }
    }
}
