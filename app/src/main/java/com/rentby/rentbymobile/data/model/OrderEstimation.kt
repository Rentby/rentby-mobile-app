package com.rentby.rentbymobile.data.model

data class OrderEstimation(
    val productId: String,
    val productName: String,
    val rentStart: String,
    val rentEnd: String,
    val rentPrice: Int,
    val rentTotal: Int,
    val deposit: Int,
    val serviceFee: Int,
    val orderTotal: Int
)
