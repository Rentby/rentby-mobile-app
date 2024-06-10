package com.rentby.rentbymobile.data.model

data class Booking (
    val productId: String,
    val productName: String,
    val rentStart: String,
    val rentEnd: String,
    val rentPrice: String,
    val rentTotal: String,
    val deposit: String,
    val serviceFee: String,
    val orderTotal: String
)