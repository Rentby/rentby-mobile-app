package com.rentby.rentbymobile.data.model

import androidx.annotation.DrawableRes

data class Product(
    val id: String,
    val name: String,
    val price: String,
    val rating: Float,
    val booked: Int,
    @DrawableRes
    val image: Int?
)
