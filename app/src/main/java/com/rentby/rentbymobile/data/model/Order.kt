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
    val productName: String,
    val rentStart: String,
    val rentEnd: String,
    val rentPrice: String,
    val pickupLocation: String,
    val serviceFee: String,
    val isRated: Boolean,

    @DrawableRes
    val image: Int?
)
