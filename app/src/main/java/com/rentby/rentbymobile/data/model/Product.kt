package com.rentby.rentbymobile.data.model

data class Product(
    val id: String,
    val name: String,
    val rentPrice: Int,
    val imageUrl: String,
    val rating: Float,
    val booked: Int,
    val description: String,
    val sellerId: String
)