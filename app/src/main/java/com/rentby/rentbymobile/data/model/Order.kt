package com.rentby.rentbymobile.data.model
import androidx.annotation.DrawableRes

data class Order (
    val id: String,
    val status: Int,
    val orderTime: String,
    val pickupTime: String,
    val returnTime: String,
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
    val lateCharge: Float,

    @DrawableRes
    val image: Int?
)
