package com.rentby.rentbymobile.data.model

import com.rentby.rentbymobile.data.response.RentStart

data class OrderItem(
    val status: String,
    val orderId: String,
    val productName: String,
    val imageUrl: String,
    val rentStart: String,
    val rentEnd: String,
    val orderTotal: Int
)