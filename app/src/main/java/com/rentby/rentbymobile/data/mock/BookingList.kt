package com.rentby.rentbymobile.data.mock

import com.rentby.rentbymobile.data.model.Booking
import com.rentby.rentbymobile.helper.calculateDay
import com.rentby.rentbymobile.helper.millisecondsToDate

object BookingList {
    fun getBooking(): List<Booking> {
        return listOf(
            Booking(
                "1",
                "Tenda Dome Kapasitas 4 Orang Waterproof Portable",
                "2024-06-11",
                "2024-06-11",
                "50000",
                "50000",
                "5000",
                "1000",
                "56000"
            )
        )
    }

    fun generateBooking(productId: String, rentStart: Long, rentEnd: Long): Booking? {
        val product = ProductList.getProducts().find { it.id == productId }
        return product?.let {
            val productName = it.name
            val rentPrice = it.price
            val duration = calculateDay(rentStart, rentEnd)
            val rentTotal = rentPrice.toInt() * duration
            val deposit = (rentTotal * 0.1).toInt()
            val serviceFee = 1000
            val orderTotal = rentTotal + deposit + serviceFee

            Booking(
                productId,
                productName,
                millisecondsToDate(rentStart),
                millisecondsToDate(rentEnd),
                rentPrice,
                rentTotal.toString(),
                deposit.toString(),
                serviceFee.toString(),
                orderTotal.toString()
            )
        }
    }
}